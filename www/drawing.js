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
var attributeToHighlight = null;

var roomInfoVisible = true;
var roomListVisible = true;
var filtersVisible = true;
var blipVisible = false;

var debugMode = true;

var selectedRoom = null;
var counter = 0;

setCookie("attribute", "");
setCookie("rooms", "");


// dummy logs for Internet Explorer 11:
if (!window.console) console = {log: function() {}};


// prototype for startsWith for Internet Explorer 11
if (!String.prototype.startsWith) {
    String.prototype.startsWith = function(searchString, position){
      position = position || 0;
      return this.substr(position, searchString.length) === searchString;
  };
}


function checkBoxValue(id) {
    return document.getElementById(id).checked;
}

function setCookie(name,value) {
    var date = new Date();
    var days = 10;
    date.setTime(date.getTime() + (days*24*60*60*1000));
    var expires = "; expires=" + date.toUTCString();
    document.cookie = name + "=" + (value || "")  + expires + "; path=/";
}

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

function onMouseWheel(ev) {
    var e = window.event || ev; // old IE support
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
    e.innerHTML = value;
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
                window.alert("Load failed");
            }
        }
    };
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
    var url = "";
    var highlightRoomType = checkBoxValue("room-type-checkbox");
    var highlightRoomCapacity = checkBoxValue("room-capacity-checkbox");
    var highlightRoomOccupation = checkBoxValue("room-ocupation-checkbox");
    var highlightRoomOccupationBy = checkBoxValue("room-occupied-by-checkbox");
    if (highlightRoomType || highlightRoomCapacity || highlightRoomOccupation || highlightRoomOccupationBy) {
        var firstItem = true;
        url += "&highlight=";
        if (highlightRoomType) {
            url += "room_type";
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
    else {
        drawingElement.attachEvent("onmousewheel", onMouseWheel);
    }
}

function addAttributeToHighlight() {
    if (attributeToHighlight == null) {
        return "";
    }
    else {
        return "&attribute=" + attributeToHighlight;
    }
}

function reloadImage(clickedX, clickedY) {
    var url = rasterDrawingUrl(drawing_id, floor_id, version);
    if (clickedX != null && clickedY != null) {
        url += "&coordsx=" + clickedX + "&coordsy=" + clickedY;
    }
    url += selectedRoomInUrl();
    //url += rasterDrawingHighlight();
    url += transformation();
    url += otherOptions();
    url += addAttributeToHighlight();
    url += "&counter=" + counter;
    random = (Math.random() + 1).toString(36).substring(2);
    url += "&random=" + random;
    counter += 1;
    console.log(url);
    var drawingElement = document.getElementById("drawing");
    drawingElement.src=url;

    registerMouseWheelCallbackFunction(drawingElement);
}

function printSelectedRoom(selectedRoom) {
    var element = document.getElementById("selected_room");
    if (selectedRoom != null) {
        setText(element, selectedRoom);
    }
    else {
        setText(element, "?");
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

            console.log(sapHref);

            // vytvoreni odkazu ve vybranem elementu
            var hrefElement = document.getElementById("sap_href");
            hrefElement.href = sapHref + selectedRoom;
            console.log(hrefElement.href);

            // simulace kliku na odkaz "SAP"
            hrefElement.click();
        }
    };
    // ziskame specialni URL vedouci do SAPu
    console.log(sap_url);
    xmlhttp.open("GET", "/api/v1/sap-href/room", true);
    xmlhttp.send(null);
}

function selectRoomInSap(aoid) {
    console.log("selectRoomInSap");
    console.log(aoid);
    setTimeout("clickOnSapHref();", 500);
}


function setText(element, text) {
    if (element != null) {
        if (typeof element.textContent !== "undefined") {
            element.textContent = text;
        }
        else {
            element.innerText = text;
        }
    }
}

function deleteRoomAttributes() {
    var container = document.getElementById("room_list");
    var rows = container.getElementsByTagName("tr");
    var i;
    for (i = 0; i < rows.length; i+=1) {
        var row = rows[i];
        var col3 = row.children[2];
        if (col3.id != null && col3.id.startsWith("room_")) {
            setText(col3, "");
        }
    }
}

function isAttributeWithStaticValues(attribute_id) {
    if (attribute_id != null) {
        return attribute_id === "uklid" || attribute_id === "typ" || attribute_id === "OB" || attribute_id === "obsazenost" || attribute_id === "smlouva";
    }
    else {
        return false;
    }
}

function isAttributeWithListOfValues(attribute_id) {
    if (attribute_id != null) {
        return attributeToHighlight === "projekt" || attributeToHighlight === "ucel";
    }
    else {
        return false;
    }
}

function valueShowOrHide(id) {
    var full_id = "enable_value_" + id;
    var checkbox = document.getElementById(full_id);
    if (checkbox.checked) {
        setCookie("value_" + id, 1);
    }
    else {
        setCookie("value_" + id, 0);
    }
    reloadImage(null, null);
}


function colorBox(color, text, id) {
    var full_id = "enable_value_" + id;
    var box = "<div class='color-box' style='opacity:0.5;filter:alpha(opacity=50);background-color: " + color + "; display:inline-block'></div>";
    var check = "<input type='checkbox' name='" + full_id + "' id='" + full_id + "' checked='checked' onclick='valueShowOrHide(" + id +")' />";
    setCookie("value_" + id, 1);
    return box + check + text + "<br/>";
}

function showLegendForAttribute(attribute) {
    var element = document.getElementById("legenda");
    var html = "";

    switch (attribute) {
    case "DS":
    case "smlouva":
        html =  colorBox("rgb( 70,  70, 240)", "Krátkodobé pronájmy", 0);
        html += colorBox("rgb(220, 220,  70)", "Dlouhodobé pronájmy", 1);
        break;
    case "OB":
    case "obsazenost":
        html =  colorBox("rgb(100, 100, 100)", "Nepronajímatelné", 0);
        html += colorBox("rgb( 40,  40, 200)", "Interní", 1);
        html += colorBox("rgb(240,  20, 20)", "Pronajímatelné - obsazené", 2);
        html += colorBox("rgb( 20, 240, 20)", "Pronajímatelné - neobsazené", 3);
        break;
    case "typ":
        html =  colorBox("rgb(  0,  0,150)", "Kancelář", 0);
        html += colorBox("rgb(  0,150,  0)", "Ateliér", 1);
        html += colorBox("rgb(  0,150,150)", "Dílna", 2);
        html += colorBox("rgb(150,  0,  0)", "Sklad", 3);
        html += colorBox("rgb(150,  0,150)", "Technický prostor", 4);
        html += colorBox("rgb(150,150,  0)", "Schody", 5);
        html += colorBox("rgb(150,150,150)", "Garáž", 6);
        html += colorBox("rgb(  0,  0,250)", "WC", 7);
        html += colorBox("rgb(  0,250,  0)", "Sprchy", 8);
        html += colorBox("rgb(  0,250,250)", "Šatna", 9);
        html += colorBox("rgb(250,  0,  0)", "Úklidová místnost", 10);
        html += colorBox("rgb(250,  0,250)", "Chodba", 11);
        html += colorBox("rgb(250,250,  0)", "Sociální zázemí", 12);
        html += colorBox("rgb(250,250,250)", "Umývárna", 13);
        html += colorBox("rgb(150,150,150)", "Kuchyň", 14);
        html += colorBox("rgb( 50, 50, 50)", "Jídelna", 15);
        break;
    case "uklid":
    case "UK":
        html =  colorBox("rgb(150,150,150)", "Četnost úklidu-bez úklidu", 0);
        html += colorBox("rgb(  0, 50,  0)", "Četnost úklidu-1xtýdně", 1);
        html += colorBox("rgb(  0,150,  0)", "Četnost úklidu-2xtýdně", 2);
        html += colorBox("rgb(  0,250,  0)", "Četnost úklidu-3xtýdně", 3);
        html += colorBox("rgb( 50,  0,  0)", "Četnost úklidu-4xtýdně", 4);
        html += colorBox("rgb(150,  0,  0)", "Četnost úklidu-5xtýdně", 5);
        html += colorBox("rgb(250,  0,  0)", "Četnost úklidu-6x týdně", 6);
        html += colorBox("rgb(  0,  0, 50)", "Četnost úklidu-7x týdně", 7);
        html += colorBox("rgb(  0,  0,150)", "Četnost úklidu-1x 14dní", 8);
        html += colorBox("rgb(  0,  0,250)", "Četnost úklidu-1x měsíc", 9);
        html += colorBox("rgb(  0,250,250)", "Četnost úklidu-Ostatní", 10);
        break;
    case "ucel":
    case "UP":
        html =  colorBox("rgb(  0,  0,150)", "kancelář  ", 0);
        html += colorBox("rgb(  0,150,  0)", "sklad/rekvizitárna", 1);
        html += colorBox("rgb(  0,150,150)", "dílna/patinerna", 2);
        html += colorBox("rgb(150,  0,  0)", "chodba", 3);
        html += colorBox("rgb(150,  0,150)", "kamerová místnost", 4);
        html += colorBox("rgb(150,150,  0)", "herecká šatna", 5);
        html += colorBox("rgb(150,150,150)", "make-up", 6);
        html += colorBox("rgb(  0,  0,250)", "kostymérna", 7);
        html += colorBox("rgb(  0,250,  0)", "kuchyňka", 8);
        html += colorBox("rgb(  0,250,250)", "catering", 9);
        html += colorBox("rgb(250,  0,  0)", "ateliér", 10);
        html += colorBox("rgb(250,  0,250)", "sociální zázemí", 11);
        html += colorBox("rgb(250,250,  0)", "WC", 12);
        html += colorBox("rgb(250,250,250)", "schody, výtah ", 13);
        html += colorBox("rgb(150,150,150)", "kuchyň", 14);
        html += colorBox("rgb( 50, 50, 50)", "jídelna", 15);
        html += colorBox("rgb(250,250,  0)", "garáž", 16);
        html += colorBox("rgb(250,250,250)", "střecha, anténa", 17);
        html += colorBox("rgb(150,150,150)", "ostatní", 18);
        break;
    }

    element.innerHTML = html;
}

function showLegendForAttributeList(attribute_list) {
    var palette = [
        "rgb(150, 150,  40)",
        "rgb( 40, 250,  40)",
        "rgb( 40, 250, 250)",
        "rgb( 40,  40, 250)",
        "rgb(250,  40, 250)",
        "rgb(250,  40,  40)",
        "rgb( 40,  40,  40)",
        "rgb(120, 120, 120)",
        "rgb(240, 240, 240)"
    ];

    var element = document.getElementById("legenda");
    var html = "";

    var i;
    for (i = 0; i < attribute_list.length; i+=1) {
        var color = palette[i % palette.length];
        html += colorBox(color, attribute_list[i], i);
    }

    element.innerHTML = html;
}

function onRoomAttributesReceived(data) {
    var attributes = JSON.parse(data);
    var attribute_list = [];
    var prop;
    var i;
    for(i=0; i < attributes.length; i++) {
        var attribute = attributes[i];
        var room = attribute["AOID"];
        var key = attribute["key"];
        var value = attribute["value"];
        //console.log(room);
        //console.log(value);
        var elementId = "room_" + room + "_attribute_value";
        var element = document.getElementById(elementId);
        setText(element, value);
        attribute_list.push(value);
    }

    if (isAttributeWithListOfValues(attributeToHighlight)) {
        attribute_list = attribute_list.sort().filter(function(element, index, array) {
             return (index === array.indexOf(element));
        });
        // now the attribute list is sorted and unique
        showLegendForAttributeList(attribute_list);
        console.log(attribute_list);
    }

    reloadImage(null, null);
}


function urlForRoomWithAttributes(attribute, floor_id, valid_from) {
    return "/api/v1/rooms-attribute?floor-id=" + floor_id + "&valid-from=" + valid_from + "&attribute=" + attribute;
}


function setRoomAttributeLabel(label) {
    var element = document.getElementById("room_attribute_label");
    setText(element, label);
}


function onAttributeTypeClicked(attribute_id, attribute_name, floor_id, valid_from) {
    var url = urlForRoomWithAttributes(attribute_id, floor_id, valid_from);
    attributeToHighlight = attribute_id;
    // clear the 3rd column in room table
    deleteRoomAttributes();
    // set the new label (1st row)
    setRoomAttributeLabel(attribute_name);
    // show legend
    if (isAttributeWithStaticValues(attribute_id)) {
        showLegendForAttribute(attribute_id);
    }
    // try to set attributes (2nd... rows)
    callAjax(url, onRoomAttributesReceived);

    // cookies used by raster renderer
    setCookie("attribute", attribute_id);
    setCookie("floor_id", floor_id);
    setCookie("valid_from", valid_from);
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
    // console.log(version);
};

function showHideSomething(elementId, iconId, hide, className) {
    if (hide) {
        document.getElementById(elementId).className = "hidden";
        document.getElementById(iconId).src = "icons/1uparrow.gif";
    }
    else {
        if (className !== null) {
            document.getElementById(elementId).className = className;
        }
        else {
            document.getElementById(elementId).className = "table table-stripped table-hover";
        }
        document.getElementById(iconId).src = "icons/1downarrow.gif";
    }
}

function showHideRoomInfo() {
    roomInfoVisible = !roomInfoVisible;
    showHideSomething("room_info", "show_hide_room_info", !roomInfoVisible, null);
}

function showHideRoomList() {
    roomListVisible = !roomListVisible;
    showHideSomething("room_list", "show_hide_room_list", !roomListVisible, null);
}

function showHideFilters() {
    filtersVisible = !filtersVisible;
    showHideSomething("filters", "show_hide_filters", !filtersVisible, "");
}
