var paper = null;
var drawing_input = null;
var drawing = null;

var scale = 1.0;
var xpos = 0;
var ypos = 0;
var grid = false;
var boundary = false;
var clickedX = null;
var clickedY = null;

var roomInfoVisible = true;
var roomListVisible = true;
var filtersVisible = true;
var blipVisible = false;

var debugMode = true;

var selectedRoom = null;
var counter = 0;


function changeXpos(delta) {
    xpos += delta;
    reloadImage(null, null);
}

function changeYpos(delta) {
    ypos += delta;
    reloadImage(null, null);
}

function setXpos(value) {
    xpos = value;
    reloadImage(null, null);
}

function setYpos(value) {
    ypos = value;
    reloadImage(null, null);
}

function changeScaleBy(mag) {
    scale *= mag;
    reloadImage(null, null);
}

function resetScale() {
    scale = 1.0;
    reloadImage(null, null);
}

function onViewMagPlusClick() {
    changeScaleBy(1.1);
}

function onViewMagMinusClick() {
    changeScaleBy(0.9);
}

function onViewMag11Click() {
    resetScale();
}

function onViewMagFitClick() {
    resetScale();
}

function onArrowLeftClick() {
    changeXpos(-20);
}

function onArrowRightClick() {
    changeXpos(20);
}

function onArrowUpClick() {
    changeYpos(-20);
}

function onArrowDownClick() {
    changeYpos(20);
}

function onCenterViewClick() {
    setXpos(0);
    setYpos(0);
}

function onViewBoundaryClick() {
    boundary = !boundary;
    reloadImage(null, null);
}

function disableScrollOnMouseWheel(e) {
    if (e.preventDefault) {
        e.preventDefault();
    } else {
        e.returnValue = false;
    }
}

function onMouseWheel(e) {
    var e = window.event || e; // old IE support
    var delta = Math.max(-1, Math.min(1, (e.wheelDelta || -e.detail)));
    console.log("mouse wheel: " + delta);

    if (delta > 0) {
        onViewMagPlusClick();
    }
    else if (delta < 0) {
        onViewMagMinusClick();
    }

    disableScrollOnMouseWheel(e);

    // no default handler
    return false;
}

function onViewGridClick() {
    grid = !grid;
    reloadImage(null, null);
}

function onViewBlip() {
    blipVisible = !blipVisible;
    reloadImage(null, null);
}

function setElementValue(elementId, value) {
    var e = document.getElementById(elementId);
    e.innerText = value;
}

function getXmlHttpRequest() {
    if (window.XMLHttpRequest) {
        // code for IE7+, Firefox, Chrome, Opera, Safari
        return new XMLHttpRequest();
    }
    else if (window.ActiveXObject) {
        // code for IE6, IE5
        return new ActiveXObject("Microsoft.XMLHTTP");
    }
    else {
        alert("Your browser does not support XMLHTTP!");
        return null;
    }
}

function callAjax(url, callback) {
    var xmlHttpRequest = getXmlHttpRequest();
    xmlHttpRequest.onreadystatechange = function() {
        if (xmlHttpRequest.readyState == 4) {
            if (xmlHttpRequest.status == 200) {
                callback(xmlHttpRequest.responseText);
            } else {
                window.alert("Load failed")
            }
        }
    }
    xmlHttpRequest.open("GET", url, true);
    xmlHttpRequest.send();
}

function getEvent(e)
{
    return !e ? window.event : e;
}

function rasterDrawingUrl(drawing_id, floor_id, version) {
    return "/raster-drawing?drawing-id=" + raster_drawing_id + "&floor-id=" + floor_id+ "&version=" + version;
}

function findRoomUrl(drawing_id, floor_id, version) {
    return "/find-room-on-drawing?drawing-id=" + raster_drawing_id + "&floor-id=" + floor_id+ "&version=" + version;
}

function rasterDrawingHighlight() {
    url = "";
    var highlightRoomType = checkBoxValue("room-type-checkbox");
    var highlightRoomCapacity = checkBoxValue("room-capacity-checkbox");
    var highlightRoomOccupation = checkBoxValue("room-ocupation-checkbox");
    var highlightRoomOccupationBy = checkBoxValue("room-occupied-by-checkbox");
    if (highlightRoomType || highlightRoomCapacity || highlightRoomOccupation || highlightRoomOccupationBy) {
        var firstItem = true;
        url += "&highlight=";
        if (highlightRoomType) {
            url += "room_type"
            firstItem = false;
        }
        if (highlightRoomCapacity) {
            if (!firstItem) {
                url += ",";
            }
            url += "capacity";
            firstItem = false;
        }
        if (highlightRoomOccupation) {
            if (!firstItem) {
                url += ",";
            }
            url += "occupation";
            firstItem = false;
        }
    }
    return url;
}

function transformation() {
    return "&x-offset=" + xpos +
           "&y-offset=" + ypos +
           "&scale=" + scale;
}

function otherOptions() {
    return "&boundary=" + boundary +
           "&grid=" + grid +
           "&blip=" + blipVisible;
}

function selectedRoomInUrl() {
    if (selectedRoom != null) {
        return "&selected=" + selectedRoom;
    }
    else {
        return "";
    }
}

function findRoomOnDrawing(clickedX, clickedY) {
    if (debugMode) {
        console.log(clickedX, clickedY);
    }
    var url = findRoomUrl(drawing_id, floor_id, version) + "&coordsx=" + clickedX + "&coordsy=" + clickedY;
    url += transformation();
    
    if (debugMode) {
        console.log(url);
    }

    var xhr = getXmlHttpRequest();
    xhr.open("GET", url, false);
    xhr.send();

    if (debugMode) {
        console.log(xhr.status);
        console.log(xhr.statusText);
        console.log(xhr.responseText);
    }

    if (xhr.status == 200 && xhr.statusText == "OK") {
        if (xhr.responseText == "") {
            return null;
        }
        else {
            return xhr.responseText;
        }
    }
}

function registerMouseWheelCallbackFunction(drawingElement) {
    if (drawingElement.addEventListener) {
        // IE9, Chrome, Safari, Opera
        drawingElement.addEventListener("mousewheel", onMouseWheel, false);
        // Firefox
        drawingElement.addEventListener("DOMMouseScroll", onMouseWheel, false);
    }
    // IE 6/7/8
    else drawingElement.attachEvent("onmousewheel", onMouseWheel);
}

function reloadImage(clickedX, clickedY) {
    var url = rasterDrawingUrl(drawing_id, floor_id, version);
    if (clickedX != null && clickedY != null) {
        url += "&coordsx=" + clickedX + "&coordsy=" + clickedY;
    }
    url += selectedRoomInUrl();
    url += rasterDrawingHighlight();
    url += transformation();
    url += otherOptions();
    url += "&counter=" + counter;
    counter++;
    console.log(url);
    var drawingElement = document.getElementById('drawing');
    drawingElement.src=url;

    registerMouseWheelCallbackFunction(drawingElement);
}

function printSelectedRoom(selectedRoom) {
    element = document.getElementById("selected_room");
    if (selectedRoom != null) {
        element.innerText = selectedRoom;
    }
    else {
        element.innerText = "?";
    }
}

function clickOnSapHref()
{
    var xmlhttp;
    xmlhttp = getXmlHttpRequest();
    xmlhttp.onreadystatechange=function()
    {
        if (xmlhttp.readyState==4)
        {
            var sapHref = xmlhttp.responseText;
            if (sapHref.indexOf('"') === 0) {
                sapHref = sapHref.substring(1 + sapHref.indexOf('"'), sapHref.lastIndexOf('"'));
            }

            // alert(sapHref);
            console.log(sapHref);

            // vytvoreni odkazu ve vybranem elementu
            var hrefElement = document.getElementById("sap_href");
            hrefElement.href = sapHref + selectedRoom;
            console.log(hrefElement.href);

            // simulace kliku na odkaz "SAP"
            hrefElement.click();
        }
    }
    // ziskame specialni URL vedouci do SAPu
    console.log(sap_url);
    xmlhttp.open("GET", "/api/v1/sap-href/room", true);
    xmlhttp.send(null);
}

function selectRoomInSap(aoid) {
    console.log("selectRoomInSap");
    console.log(aoid);
    setTimeout("clickOnSapHref()", 500);
}

function onImageClick(obj, e) {
    var evt = getEvent(e);
    var boundingRect = obj.getBoundingClientRect();
    var top = (window.pageYOffset || obj.scrollTop)  - (obj.clientTop || 0);
    console.log(top);

    if (evt.pageX)
    {
        clickedX = Math.round(evt.pageX - boundingRect.left);
        clickedY = Math.round(evt.pageY - boundingRect.top) - top;
    }
    else if (evt.clientX)
    {
        clickedX = evt.offsetX;
        clickedY = evt.offsetY;
    }
    selectedRoom = findRoomOnDrawing(clickedX, clickedY);
    printSelectedRoom(selectedRoom);
    if (sap_enabled) {
        selectRoomInSap(selectedRoom);
    }
    reloadImage(clickedX, clickedY);
}

function onRoomSelect(aoid) {
    console.log("Selecting room: " + aoid);
    selectedRoom = aoid;
    printSelectedRoom(selectedRoom);
    if (sap_enabled) {
        selectRoomInSap(selectedRoom);
    }
    reloadImage(null, null);
}

function checkBoxValue(id) {
    return document.getElementById(id).checked;
}

function roomTypeCheckBoxClicked() {
    reloadImage(null, null);
}

function roomCapacityCheckBoxClicked() {
    reloadImage(null, null);
}

function roomOccupationCheckBoxClicked() {
    reloadImage(null, null);
}

function roomOccupiedByCheckBoxClicked() {
    reloadImage(null, null);
}

// Initialize container when document is loaded
window.onload = function () {
    // drawGrid
    console.log(version);
};

function showHideSomething(elementId, iconId, hide) {
    if (hide) {
        document.getElementById(elementId).className = "hidden";
        document.getElementById(iconId).src = "icons/1uparrow.gif";
    }
    else {
        document.getElementById(elementId).className = "table table-stripped table-hover";
        document.getElementById(iconId).src = "icons/1downarrow.gif";
    }
}

function showHideRoomInfo() {
    roomInfoVisible = !roomInfoVisible;
    showHideSomething("room_info", "show_hide_room_info", !roomInfoVisible);
}

function showHideRoomList() {
    roomListVisible = !roomListVisible;
    showHideSomething("room_list", "show_hide_room_list", !roomListVisible);
}

function showHideFilters() {
    filtersVisible = !filtersVisible;
    showHideSomething("filters", "show_hide_filters", !filtersVisible);
}
