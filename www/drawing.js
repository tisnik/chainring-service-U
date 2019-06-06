var paper = null;
var drawing_input = null;
var drawing = null;

var scale = 1.0;
var xpos = 0;
var ypos = 0;
var grid = false;
var boundary = false;
var dimensions= false;
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

var search_drawing = false;

var palette = [
    "rgb(  0,  0,150)",
    "rgb(  0,150,  0)",
    "rgb(  0,150,150)",
    "rgb(150,  0,  0)",
    "rgb(150,  0,150)",
    "rgb(150,150,  0)",
    "rgb(150,150,150)",
    "rgb(  0,  0,250)",
    "rgb(  0,250,  0)",
    "rgb(  0,250,250)",
    "rgb(250,  0,  0)",
    "rgb(250,  0,250)",
    "rgb(250,250,  0)",
    "rgb(250,250,250)",
    "rgb(150,150,150)",
    "rgb( 50, 50, 50)",
    "rgb(250,250,  0)",
    "rgb(250,250,250)",
    "rgb(150,150,150)",
    "rgb( 50, 50, 50)"
];

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

function onViewDimensionsClick() {
    dimensions = !dimensions;
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
                console.log(xmlHttpRequest.status);
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
           "&dimensions=" + dimensions +
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
    random = (Math.random() + 1).toString(36).substring(2);
    url += transformation();
    url += "&random=" + random;

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
    if (search_drawing) {
        url += "&search-drawing=1";
    }
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
            random = (Math.random() + 1).toString(36).substring(2);
            hrefElement.href += "&random=" + random;
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
        return attribute_id === "typ" ||
               attribute_id === "FM" ||
               attribute_id === "OB" || attribute_id === "obsazenost" ||
               attribute_id === "DS" || attribute_id === "smlouva" ||
               attribute_id === "UK" || attribute_id === "uklid" ||
               attribute_id === "UP" || attribute_id === "ucel";
    }
    else {
        return false;
    }
}

function isAttributeWithListOfValues(attribute_id) {
    if (attribute_id != null) {
        return attribute_id === "PR" || attribute_id === "projekt" || attribute_id === "NS";
    }
    else {
        return false;
    }
}

function isAttributeWithRadioButtons(attribute_id) {
    if (attribute_id != null) {
        // MV - Měřidla - voda
        // MT - Měřidla - teplo
        // ME - Měřidla - elektřina
        return attribute_id === "MV" || attribute_id === "MT" || attribute_id == "ME";
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


function radioValueShowOrHide(id) {
    setCookie("radio_value", id.toString());
    reloadImage(null, null);
}


function colorBox(color, text, id, used_values) {
    if (used_values.indexOf(text) > -1) {
        var full_id = "enable_value_" + id;
        var box = "<div class='color-box' style='opacity:0.5;filter:alpha(opacity=50);background-color: " + color + "; display:inline-block'></div>";
        var check = "<input type='checkbox' name='" + full_id + "' id='" + full_id + "' checked='checked' onclick='valueShowOrHide(" + id +")' />";
        var text = "<span class='color-box-text'>" + text + "</span>";
        setCookie("value_" + id, 1);
        return box + check + text + "<br/>";
    }
    else {
        return "";
    }
}


function colorRadioBox(color, text, id) {
    var full_id = "enable_value_" + id;
    var box = "<div class='color-box' style='opacity:0.5;filter:alpha(opacity=50);background-color: " + color + "; display:inline-block'></div>";
    var radio = "<input type='radio' name='meridla' value='" + full_id + "' id='" + full_id + "' onclick='radioValueShowOrHide(" + id +")' />";
    return box + radio + text + "<br/>";
}


function showLegendForAttribute(attribute, used_values) {
    var element = document.getElementById("legenda");
    var html = "";

    // TODO: needs to be refactored
    switch (attribute) {
    case "DS":
    case "smlouva":
        html =  colorBox("rgb( 70,  70, 240)", "Krátkodobé pronájmy", 0, used_values);
        html += colorBox("rgb(220, 220,  70)", "Dlouhodobé pronájmy", 1, used_values);
        break;
    case "OB":
    case "obsazenost":
        html =  colorBox("rgb(100, 100, 100)", "Nepronajímatelné", 0, used_values);
        html += colorBox("rgb( 40,  40, 200)", "Interní", 1, used_values);
        html += colorBox("rgb(240,  20, 20)", "Pronajímatelné - obsazené", 2, used_values);
        html += colorBox("rgb( 20, 240, 20)", "Pronajímatelné - neobsazené", 3, used_values);
        break;
    case "FM":
    case "typ":
        html =  colorBox("rgb(  0,  0,150)", "aula a sál", 100, used_values);
        html += colorBox("rgb(  0,150,  0)", "sál počítačový", 101, used_values);
        html += colorBox("rgb(  0,150,150)", "sál operační", 140, used_values);
        html += colorBox("rgb(150,  0,  0)", "posluchárna", 200, used_values);
        html += colorBox("rgb(150,  0,150)", "posluchárna botanická", 202, used_values);
        html += colorBox("rgb(150,150,  0)", "seminární místnost", 300, used_values);
        html += colorBox("rgb(150,150,150)", "seminárka fyzikální", 304, used_values);
        html += colorBox("rgb(  0,  0,250)", "seminárka biochemická", 317, used_values);
        html += colorBox("rgb(  0,250,  0)", "učebna", 400, used_values);
        html += colorBox("rgb(  0,250,250)", "učebna počítačová", 401, used_values);
        html += colorBox("rgb(250,  0,  0)", "učebna botanická", 402, used_values);
        html += colorBox("rgb(250,  0,250)", "laboratoř", 500, used_values);
        html += colorBox("rgb(250,250,  0)", "laboratoř botanická", 502, used_values);
        html += colorBox("rgb(250,250,250)", "laboratoř chemická", 503, used_values);
        html += colorBox("rgb(150,150,150)", "laboratoř fyzikální", 504, used_values);
        html += colorBox("rgb( 50, 50, 50)", "laboratoř zoologická", 505, used_values);
        html += colorBox("rgb(250,250,  0)", "laboratoř toxikologická", 506, used_values);
        html += colorBox("rgb(250,250,250)", "laboratoř serologická", 507, used_values);
        html += colorBox("rgb(150,150,150)", "laboratoř histologická", 509, used_values);
        html += colorBox("rgb(  0,150,  0)", "laboratoř foto", 510, used_values);
        html += colorBox("rgb(  0,150,150)", "laboratoř alkoholová", 512, used_values);
        html += colorBox("rgb(150,  0,  0)", "laboratoř bakteriologická", 513, used_values);
        html += colorBox("rgb(150,  0,150)", "laboratoř serologická", 514, used_values);
        html += colorBox("rgb(150,150,  0)", "laboratoř mykologická", 515, used_values);
        html += colorBox("rgb(150,150,150)", "laboratoř cytologie", 516, used_values);
        html += colorBox("rgb(  0,  0,250)", "laboratoř biochemická", 517, used_values);
        html += colorBox("rgb(  0,250,  0)", "laboratoř imunologická", 518, used_values);
        html += colorBox("rgb(  0,250,250)", "laboratoř mol. biologie", 519, used_values);
        html += colorBox("rgb(250,  0,  0)", "laboratoř vakcíny ", 520, used_values);
        html += colorBox("rgb(250,  0,250)", "laboratoř antibiotická", 521, used_values);
        html += colorBox("rgb(250,250,  0)", "laboratoř mykobakterie", 522, used_values);
        html += colorBox("rgb(250,250,250)", "laboratoř varna půd", 523, used_values);
        html += colorBox("rgb(150,150,150)", "laboratoř analytická", 526, used_values);
        html += colorBox("rgb( 50, 50, 50)", "laboratoř dezinfekce vod", 527, used_values);
        html += colorBox("rgb(250,250,  0)", "laboratoř kultivace plísní", 528, used_values);
        html += colorBox("rgb(250,250,250)", "laboratoř tkáňové kultury", 529, used_values);
        html += colorBox("rgb(150,150,150)", "laboratoř operační", 540, used_values);
        html += colorBox("rgb(  0,150,  0)", "tělocvična", 600, used_values);
        html += colorBox("rgb(  0,150,150)", "bazén", 700, used_values);
        html += colorBox("rgb(150,  0,  0)", "loděnice", 800, used_values);
        html += colorBox("rgb(150,  0,150)", "sauna", 900, used_values);
        html += colorBox("rgb(150,150,  0)", "ambulance", 1000, used_values);
        html += colorBox("rgb(150,150,150)", "ordinace", 1100, used_values);
        html += colorBox("rgb(  0,  0,250)", "přípravna", 1200, used_values);
        html += colorBox("rgb(  0,250,  0)", "přípravna počítačová", 1201, used_values);
        html += colorBox("rgb(  0,250,250)", "přípravna botanická", 1202, used_values);
        html += colorBox("rgb(250,  0,  0)", "přípravna chemická", 1203, used_values);
        html += colorBox("rgb(250,  0,250)", "přípravna biochemická", 1217, used_values);
        html += colorBox("rgb(250,250,  0)", "drtírna", 1220, used_values);
        html += colorBox("rgb(250,250,250)", "kopírka", 1300, used_values);
        html += colorBox("rgb(150,150,150)", "šatna", 1400, used_values);
        html += colorBox("rgb( 50, 50, 50)", "rehabilitace", 1500, used_values);
        html += colorBox("rgb(250,250,  0)", "vyšetřovna", 1600, used_values);
        html += colorBox("rgb(250,250,250)", "filtr", 1700, used_values);
        html += colorBox("rgb(150,150,150)", "pitevna", 1800, used_values);
        html += colorBox("rgb(  0,150,  0)", "knihovna", 2000, used_values);
        html += colorBox("rgb(  0,150,150)", "studovna", 2100, used_values);
        html += colorBox("rgb(150,  0,  0)", "studovna počítačová", 2101, used_values);
        html += colorBox("rgb(150,  0,150)", "pracovna", 2200, used_values);
        html += colorBox("rgb(150,150,  0)", "pracovna počítačová", 2201, used_values);
        html += colorBox("rgb(150,150,150)", "pracovna botanická", 2202, used_values);
        html += colorBox("rgb(  0,  0,250)", "pracovna biochemická", 2217, used_values);
        html += colorBox("rgb(  0,250,  0)", "kancelář", 2300, used_values);
        html += colorBox("rgb(  0,250,250)", "kancelář počítačová", 2301, used_values);
        html += colorBox("rgb(250,  0,  0)", "kabinet", 2400, used_values);
        html += colorBox("rgb(250,  0,250)", "fytotron", 2500, used_values);
        html += colorBox("rgb(250,250,  0)", "fytotron botanický", 2502, used_values);
        html += colorBox("rgb(250,250,250)", "fytotron chemický", 2503, used_values);
        html += colorBox("rgb(150,150,150)", "fytotron imunologický", 2518, used_values);
        html += colorBox("rgb( 50, 50, 50)", "přípravna", 2700, used_values);
        html += colorBox("rgb(250,250,  0)", "galerie", 2800, used_values);
        html += colorBox("rgb(250,250,250)", "klubovna", 2900, used_values);
        html += colorBox("rgb(150,150,150)", "výstavní prostor", 3000, used_values);
        html += colorBox("rgb(  0,150,  0)", "ateliér", 3100, used_values);
        html += colorBox("rgb(  0,150,150)", "ateliér foto", 3110, used_values);
        html += colorBox("rgb(150,  0,  0)", "zasedací místnost", 3200, used_values);
        html += colorBox("rgb(150,  0,150)", "studio", 3300, used_values);
        html += colorBox("rgb(150,150,  0)", "studio zvukové", 3330, used_values);
        html += colorBox("rgb(150,150,150)", "studio televizní", 3331, used_values);
        html += colorBox("rgb(  0,  0,250)", "studio promítací", 3333, used_values);
        html += colorBox("rgb(  0,250,  0)", "videostřižna", 3334, used_values);
        html += colorBox("rgb(  0,250,250)", "dramaturgie a produkce", 3350, used_values);
        html += colorBox("rgb(250,  0,  0)", "archiv", 3400, used_values);
        html += colorBox("rgb(250,  0,250)", "denní místnost", 3500, used_values);
        html += colorBox("rgb(250,250,  0)", "místnost chladící, mrazící", 3600, used_values);
        html += colorBox("rgb(250,250,250)", "místnost přístroje", 3700, used_values);
        html += colorBox("rgb(150,150,150)", "bufet", 4000, used_values);
        html += colorBox("rgb( 50, 50, 50)", "jídelna", 4100, used_values);
        html += colorBox("rgb(250,250,  0)", "varna", 4200, used_values);
        html += colorBox("rgb(250,250,250)", "výdejna", 4300, used_values);
        html += colorBox("rgb(150,150,150)", "kuchyňka", 4400, used_values);
        html += colorBox("rgb(  0,150,  0)", "restaurace", 4500, used_values);
        html += colorBox("rgb(  0,150,150)", "pokoj", 5000, used_values);
        html += colorBox("rgb(150,  0,  0)", "byt", 5100, used_values);
        html += colorBox("rgb(150,  0,150)", "chatka", 5200, used_values);
        html += colorBox("rgb(150,150,  0)", "vodárna", 5300, used_values);
        html += colorBox("rgb(150,150,150)", "kopírna", 5400, used_values);
        html += colorBox("rgb(  0,  0,250)", "kotelna", 5500, used_values);
        html += colorBox("rgb(  0,250,  0)", "předávací stanice", 5600, used_values);
        html += colorBox("rgb(  0,250,250)", "rozvodna", 5700, used_values);
        html += colorBox("rgb(250,  0,  0)", "strojovna", 5800, used_values);
        html += colorBox("rgb(250,  0,250)", "strojovna vzduchotechniky", 5850, used_values);
        html += colorBox("rgb(250,250,  0)", "sklad", 5900, used_values);
        html += colorBox("rgb(250,250,250)", "sklad počítačový", 5901, used_values);
        html += colorBox("rgb(150,150,150)", "sklad botanický", 5902, used_values);
        html += colorBox("rgb( 50, 50, 50)", "sklad chemický", 5903, used_values);
        html += colorBox("rgb(250,250,  0)", "sklad fyzikální", 5904, used_values);
        html += colorBox("rgb(250,250,250)", "sklad toxikologický", 5906, used_values);
        html += colorBox("rgb(150,150,150)", "sklad biochemický", 5917, used_values);
        html += colorBox("rgb(  0,150,  0)", "sklad expedice", 5924, used_values);
        html += colorBox("rgb(  0,150,150)", "sklad knihy", 5932, used_values);
        html += colorBox("rgb(150,  0,  0)", "sklad-depozitář", 5933, used_values);
        html += colorBox("rgb(150,  0,150)", "provozní místnost", 5950, used_values);
        html += colorBox("rgb(150,150,  0)", "spisovna", 5960, used_values);
        html += colorBox("rgb(150,150,150)", "technologická místnost", 5970, used_values);
        html += colorBox("rgb(  0,  0,250)", "vrátnice", 6000, used_values);
        html += colorBox("rgb(  0,250,  0)", "ústředna", 6100, used_values);
        html += colorBox("rgb(  0,250,250)", "garáž", 6200, used_values);
        html += colorBox("rgb(250,  0,  0)", "dílna", 6300, used_values);
        html += colorBox("rgb(250,  0,250)", "dílna počítačová", 6301, used_values);
        html += colorBox("rgb(250,250,  0)", "prodejna", 6400, used_values);
        html += colorBox("rgb(250,250,250)", "kolárna", 6500, used_values);
        html += colorBox("rgb(150,150,150)", "server", 6600, used_values);
        html += colorBox("rgb( 50, 50, 50)", "CO", 6700, used_values);
        html += colorBox("rgb(250,250,  0)", "tiskárna", 6800, used_values);
        html += colorBox("rgb(250,250,250)", "knihárna", 6900, used_values);
        html += colorBox("rgb( 50,150,150)", "chodba", 7000, used_values);
        html += colorBox("rgb(  0,150,  0)", "schodiště", 7100, used_values);
        html += colorBox("rgb(  0,150,150)", "vstupní hala", 7200, used_values);
        html += colorBox("rgb(150,  0,  0)", "výtahová šachta", 7300, used_values);
        html += colorBox("rgb(150,  0,150)", "zádveří", 7400, used_values);
        html += colorBox("rgb(150,150,  0)", "předsíň", 7500, used_values);
        html += colorBox("rgb(150,150,150)", "WC a sprcha", 8000, used_values);
        html += colorBox("rgb(  0,  0,250)", "úklidová komora", 8100, used_values);
        html += colorBox("rgb(  0,250,  0)", "terasa", 8200, used_values);
        html += colorBox("rgb(  0,250,250)", "střecha", 8300, used_values);
        html += colorBox("rgb(250,  0,  0)", "půda", 8400, used_values);
        html += colorBox("rgb(250,  0,250)", "lyžárna", 8500, used_values);
        html += colorBox("rgb(250,250,  0)", "ostatní", 9900, used_values);
        html += colorBox("rgb(250,250,250)", "TECH. ZÁZEMÍ", 50000, used_values);
        break;
    case "uklid":
    case "UK":
        html =  colorBox("rgb(150,150,150)", "Četnost úklidu-bez úklidu", 0, used_values);
        html += colorBox("rgb(  0, 50,  0)", "Četnost úklidu-1xtýdně", 1, used_values);
        html += colorBox("rgb(  0,150,  0)", "Četnost úklidu-2xtýdně", 2, used_values);
        html += colorBox("rgb(  0,250,  0)", "Četnost úklidu-3xtýdně", 3, used_values);
        html += colorBox("rgb( 50,  0,  0)", "Četnost úklidu-4xtýdně", 4, used_values);
        html += colorBox("rgb(150,  0,  0)", "Četnost úklidu-5xtýdně", 5, used_values);
        html += colorBox("rgb(250,  0,  0)", "Četnost úklidu-6x týdně", 6, used_values);
        html += colorBox("rgb(  0,  0, 50)", "Četnost úklidu-7x týdně", 7, used_values);
        html += colorBox("rgb(  0,  0,150)", "Četnost úklidu-1x 14dní", 8, used_values);
        html += colorBox("rgb(  0,  0,250)", "Četnost úklidu-1x měsíc", 9, used_values);
        html += colorBox("rgb(  0,250,250)", "Četnost úklidu-Ostatní", 10, used_values);
        break;
    case "ucel":
    case "UP":
        html =  colorBox("rgb(  0,  0,150)", "kancelář", 0, used_values);
        html += colorBox("rgb(  0,150,  0)", "sklad/rekvizitárna", 1, used_values);
        html += colorBox("rgb(  0,150,150)", "dílna/patinerna", 2, used_values);
        html += colorBox("rgb(150,  0,  0)", "chodba", 3, used_values);
        html += colorBox("rgb(150,  0,150)", "kamerová místnost", 4, used_values);
        html += colorBox("rgb(150,150,  0)", "herecká šatna", 5, used_values);
        html += colorBox("rgb(150,150,150)", "make-up", 6, used_values);
        html += colorBox("rgb(  0,  0,250)", "kostymérna", 7, used_values);
        html += colorBox("rgb(  0,250,  0)", "kuchyňka", 8, used_values);
        html += colorBox("rgb(  0,250,250)", "catering", 9, used_values);
        html += colorBox("rgb(250,  0,  0)", "ateliér", 10, used_values);
        html += colorBox("rgb(250,  0,250)", "sociální zázemí", 11, used_values);
        html += colorBox("rgb(250,250,  0)", "WC", 12, used_values);
        html += colorBox("rgb(250,250,250)", "schody, výtah ", 13, used_values);
        html += colorBox("rgb(150,150,150)", "kuchyň", 14, used_values);
        html += colorBox("rgb( 50, 50, 50)", "jídelna", 15, used_values);
        html += colorBox("rgb(250,250,  0)", "garáž", 16, used_values);
        html += colorBox("rgb(250,250,250)", "střecha, anténa", 17, used_values);
        html += colorBox("rgb(150,150,150)", "ostatní", 18, used_values);
        break;
    }

    element.innerHTML = html;
}


function showLegendForAttributeList(attribute_list) {
    var element = document.getElementById("legenda");
    var html = "";

    // new code: TODO test
    attribute_list.sort();
    var i;
    for (i = 0; i < attribute_list.length; i+=1) {
        var color = palette[i % palette.length];
        html += colorBox(color, attribute_list[i], i, attribute_list);
    }

    element.innerHTML = html;
}


function showLegendForRadioList(attribute_list) {
    var element = document.getElementById("legenda");
    var html = "";

    var i;
    for (i = 0; i < attribute_list.length; i+=1) {
        var color = palette[i % palette.length];
        html += colorRadioBox(color, attribute_list[i], i);
    }

    element.innerHTML = html;
}


function sortAndUnique(an_array) {
    return an_array.sort().filter(function(element, index, array) {
        return (index === array.indexOf(element));
    });
}

function onPossibleAttributesReceived(data) {
    var attribute_list = JSON.parse(data);

    // show legend
    if (isAttributeWithStaticValues(attributeToHighlight)) {
        showLegendForAttribute(attributeToHighlight, attribute_list);
    }
    else if (isAttributeWithListOfValues(attributeToHighlight)) {
        // now the attribute list is sorted and unique
        showLegendForAttributeList(attribute_list);
    }
    else if (isAttributeWithRadioButtons(attributeToHighlight)) {
        setCookie("radio_value", null);
        showLegendForRadioList(attribute_list);
    }
}

function onRoomAttributesReceived(data) {
    var attributes = JSON.parse(data);
    var prop;
    var i;
    var meridla = isAttributeWithRadioButtons(attributeToHighlight);

    for (i=0; i < attributes.length; i++) {
        var attribute = attributes[i];
        var room = attribute["AOID"];
        var key = attribute["key"];
        var value = attribute["value"];
        var elementId = "room_" + room + "_attribute_value";
        var element = document.getElementById(elementId);
        if (meridla) {
            //var values = value.split(",");
            //var text_content = values.join("\r\n");
            setText(element, value);
            //var j;
            //for (j=0; j < values.length; j++) {
            //    attribute_list.push(values[j]);
            //}
        } else {
            setText(element, value);
            //attribute_list.push(value);
        }
    }

    if (false) {
        setCookie("radio_value", null);
        // sort and unique elements
        //attribute_list = sortAndUnique(attribute_list);

        // console.log(attribute_list);
        //showLegendForRadioList(attribute_list);
    }

    reloadImage(null, null);
}


function urlForRoomWithAttributes(attribute, floor_id, valid_from) {
    return "/api/v1/rooms-attribute?floor-id=" + floor_id + "&valid-from=" + valid_from + "&attribute=" + attribute;
}


function urlForPossibleAttributes(attribute, floor_id, valid_from) {
    return "/api/v1/possible-attributes?floor-id=" + floor_id + "&valid-from=" + valid_from + "&attribute=" + attribute;
}

function setRoomAttributeLabel(label) {
    var element = document.getElementById("room_attribute_label");
    setText(element, label);
}


function onAttributeTypeClicked(attribute_id, attribute_name, floor_id, valid_from) {
    attributeToHighlight = attribute_id;

    // clear the 3rd column in room table
    deleteRoomAttributes();

    // set the new label (1st row)
    setRoomAttributeLabel(attribute_name);

    var url = urlForPossibleAttributes(attribute_id, floor_id, valid_from);
    random = (Math.random() + 1).toString(36).substring(2);
    url += "&random=" + random;

    // try display attribute legend
    callAjax(url, onPossibleAttributesReceived);

    // cookies used by raster renderer
    setCookie("attribute", attribute_id);
    setCookie("floor_id", floor_id);
    setCookie("valid_from", valid_from);

    // read all attributes and fill in the table
    var url = urlForRoomWithAttributes(attribute_id, floor_id, valid_from);
    random = (Math.random() + 1).toString(36).substring(2);
    url += "&random=" + random;

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
    //if (sap_enabled) {
    //    selectRoomInSap(selectedRoom);
    //}
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

function onDrawingIdReceived(data) {
    var drawing_name = JSON.parse(data);
    console.log(drawing_name);
    if (drawing_name === undefined || drawing_name == null) {
        raster_drawing_id = null;
        reloadImage(null, null);
        return;
    }
    var i = drawing_name.lastIndexOf(".")
    raster_drawing_id = drawing_name.substring(0, i);
    console.log(raster_drawing_id);
    search_drawing = true;
    reloadImage(null, null);
}

function onRoomSelected() {
    var building = document.getElementById("buildings").value;
    var room = document.getElementById("rooms").value;
    selectedRoom = room;

    var i = room.lastIndexOf(".")
    var floor = room.substring(0, i)
    floor_id = floor;
    version = "1";

    console.log("Selected building: " + building);
    console.log("Selected floor: " + floor);
    console.log("Selected room: " + room);

    var url = "/api/v1/latest-drawing-for-floor?floor-id=" + floor;
    random = (Math.random() + 1).toString(36).substring(2);
    url += "&random=" + random;
    console.log(url);

    // try display list of rooms
    callAjax(url, onDrawingIdReceived);
}

function onListOfRoomsReceived(data) {
    var room_list = JSON.parse(data);
    if (room_list === undefined || room_list == null || room_list.length <= 0) {
        return;
    }
    if (room_list.status != "ok") {
         console.log("error during API call");
         return;
    }
    // console.log(room_list["building-id"]);
    // console.log(room_list["floor-id"]);
    // console.log(room_list["rooms"]);
    var rooms = room_list["rooms"];

    var select = document.getElementById("rooms");
    select.options.length = 0;
    var i;
    for (i = 0; i < rooms.length; i+=1) {
        var room = rooms[i];
        // console.log(room["AOID"]);
        // console.log(room["Short"]);
        // console.log(room["Label"]);
        select.options[select.options.length] = new Option(room["Short"] + "  " + room["Label"], room["AOID"]);
    }
}

function onListOfBuildingsReceived(data) {
    var building_list = JSON.parse(data);
    if (building_list === undefined || building_list == null || building_list.length <= 0) {
        return;
    }
    if (building_list.status != "ok") {
         console.log("error during API call");
         return;
    }

    var buildings = building_list["buildings"];

    var select = document.getElementById("buildings");
    select.options.length = 0;
    var i;
    select.options[select.options.length] = new Option("", "", true);
    for (i = 0; i < buildings.length; i+=1) {
        var building = buildings[i];
        // console.log(building["AOID"]);
        // console.log(building["Short"]);
        // console.log(building["Label"]);
        select.options[select.options.length] = new Option(building["Short"] + "  " + building["Label"], building["AOID"]);
    }
}

function onBuildingSelected() {
    building = document.getElementById("buildings").value;
    console.log("Selected building: " + building);
    if (building === undefined || building == null || building.length <= 0) {
        return;
    }
    var url = "/api/v1/rooms-for-building?building-id=" + building + "&floor-id=" + building;
    random = (Math.random() + 1).toString(36).substring(2);
    url += "&random=" + random;
    console.log(url);

    // try display list of rooms
    callAjax(url, onListOfRoomsReceived);
}

function readBuildings() {
    var url = "/api/v1/buildings";
    random = (Math.random() + 1).toString(36).substring(2);
    url += "?random=" + random;
    console.log(url);

    // try display list of buildings
    callAjax(url, onListOfBuildingsReceived);
}

