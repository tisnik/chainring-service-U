var drawing_input = null;
var drawing = null;

var scale = 1.0;
var xpos = 0;
var ypos = 0;

var debugMode = true;

var selectedRoom = null;
var counter = 0;

var search_drawing = false;


// dummy logs for Internet Explorer 11:
if (!window.console) console = {log: function() {}};


// prototype for startsWith for Internet Explorer 11
if (!String.prototype.startsWith) {
    String.prototype.startsWith = function(searchString, position){
      position = position || 0;
      return this.substr(position, searchString.length) === searchString;
  };
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

function transformation() {
    return "&x-offset=" + xpos +
           "&y-offset=" + ypos +
           "&scale=" + scale;
}

function selectedRoomInUrl() {
    if (selectedRoom != null) {
        return "&selected=" + selectedRoom;
    }
    else {
        return "";
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

function reloadImage(clickedX, clickedY) {
    var url = rasterDrawingUrl(drawing_id, floor_id, version);
    if (clickedX != null && clickedY != null) {
        url += "&coordsx=" + clickedX + "&coordsy=" + clickedY;
    }
    url += selectedRoomInUrl();
    url += transformation();
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

