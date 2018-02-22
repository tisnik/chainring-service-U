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

function changeXpos(delta) {
    xpos += delta;
}

function changeYpos(delta) {
    ypos += delta;
}

function setXpos(value) {
    xpos = value;
}

function setYpos(value) {
    ypos = value;
}

function changeScaleBy(mag) {
    scale *= mag;
}

function resetScale() {
    scale = 1.0;
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
    changeXpos(-10);
}

function onArrowRightClick() {
    changeXpos(10);
}

function onArrowUpClick() {
    changeYpos(-10);
}

function onArrowDownClick() {
    changeYpos(10);
}

function onCenterViewClick() {
    setXpos(0);
    setYpos(0);
}

function onViewBoundaryClick() {
}

function onViewGridClick() {
}

function setElementValue(elementId, value) {
    var e = document.getElementById(elementId);
    e.innerText = value;
}

function callAjax(url, callback) {
    var xmlHttpRequest = null;
    xmlHttpRequest = new XMLHttpRequest();
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

function onLoadDrawing(raw_data) {
    var lines = raw_data.split("\n");
    drawing_input = lines;
    window.alert(lines.length);
    var length = lines.length;
    for (var i = 0; i < length; i++) {
        var line = lines[i];
    }
}

function onLoadDrawingJSON(raw_data) {
    //drawing = JSON.parse(raw_data)
    if (typeof JSON == 'object') {
        console.log("using JSON.parse() to deserialize drawing");
        drawing = JSON.parse(raw_data);
    }
    else {
        console.log("using eval() to deserialize drawing");
        drawing = eval("(" + raw_data + ")");
    }
    var scales = drawing["scales"];
    var scale = 0, xoffset = 0, yoffset = 0;
    for (i=0; i<scales.length; i++) {
        if ((scales[i]["width"] == 800) && (scales[i]["height"] = 600)) {
            scale = scales[i]["scale"];
            xoffset = scales[i]["xoffset"];
            yoffset = scales[i]["yoffset"];
        }
    }
    console.log("scale", scale);
    console.log("xoffset", xoffset);
    console.log("yoffset", yoffset);
    var entities = drawing["entities"];
    for (i=0; i<entities.length; i++) {
        var entity = entities[i];
        switch (entity["T"]) {
            case "L":
                var x1 = (entity["x1"] + xoffset) * scale;
                var y1 = (entity["y1"] + yoffset) * scale;
                var x2 = (entity["x2"] + xoffset) * scale;
                var y2 = (entity["y2"] + yoffset) * scale;
                var path = "M" + x1 + "," + y1 + " L" + x2 + "," + y2;
                paper.path(path);
                break;
            case "A":
                var x = (entity["x"] + xoffset) * scale;
                var y = (entity["y"] + yoffset) * scale;
                var r = entity["r"] * scale;
                var a1 = entity["a1"];
                var a2 = entity["a2"];
                paper.circularArc(x, y, r, a1, a2);
                break;
            case "T":
                var x = (entity["x"] + xoffset) * scale;
                var y = (entity["y"] + yoffset) * scale;
                var text = entity["text"];
                paper.text(x, y, text).attr("font-size", 7);//.attr("stroke", "#0000FF");
                break;
            default:
                break;
        }
    }
}

function initializePaper(elementId, width, height) {
    paper = Raphael(document.getElementById(elementId), width, height);
    paper.clear();
}

function downloadDrawingById(drawingId) {
    var url = "/vector-drawing?drawing-id=" + drawingId;
    callAjax(url, onLoadDrawing);
}

function downloadJSONDrawingById(drawingId) {
    var url = "/vector-drawing-as-json?drawing-id=" + drawingId;
    console.log("Loading drawing with id=" + drawingId);
    callAjax(url, onLoadDrawingJSON);
}

function downloadJSONDrawingByName(drawingName) {
    var url = "/vector-drawing-as-json?drawing-name=" + drawingName;
    console.log("Loading drawing with name=" + drawingName);
    callAjax(url, onLoadDrawingJSON);
}

function getEvent(e)
{
    return !e ? window.event : e;
}

function rasterDrawingUrl(drawing_id, floor_id, version) {
    return "/raster-drawing?drawing-id=" + raster_drawing_id + "&floor-id=" + floor_id+ "&version=" + version;
}

function onImageClick(obj, e) {
    var evt = getEvent(e);
    var boundingRect = obj.getBoundingClientRect();

    if (evt.pageX)
    {
        clickedX = Math.round(evt.pageX - boundingRect.left);
        clickedY = Math.round(evt.pageY - boundingRect.top);
    }
    else if (evt.clientX)
    {
        clickedX = evt.offsetX;
        clickedY = evt.offsetY;
    }
    //console.log(clickedX, clickedY);
    var url = rasterDrawingUrl(drawing_id, floor_id, version) + "&coordsx=" + clickedX + "&coordsy=" + clickedY;
    //console.log(url);
    document.getElementById('drawing').src=url;
}

function onRoomSelect(aoid) {
    console.log("Selecting room: " + aoid);
    var url = rasterDrawingUrl(drawing_id, floor_id, version) + "&selected=" + aoid;
    document.getElementById('drawing').src=url;
}

// Initialize container when document is loaded
window.onload = function () {
    // drawGrid
    console.log(version);
    if (drawing_id !== null) {
        initializePaper("drawing_canvas", 800, 600);
        downloadJSONDrawingById(drawing_id);
    }
    else if (drawing_name !== null) {
        initializePaper("drawing_canvas", 800, 600);
        downloadJSONDrawingByName(drawing_name);
    }
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

