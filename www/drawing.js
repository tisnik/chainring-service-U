var paper = null;
var drawing_input = null;
var drawing = null;

var scale = 1.0;
var xpos = 0;
var ypos = 0;
var grid = false;
var boundary = false;

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

// Initialize container when document is loaded
window.onload = function () {
    downloadDrawingById(drawing_id);
    initializePaper("drawing_canvas", 800, 600);
    var circle = paper.circle(100, 100, 80);
    for (var i = 0; i < 5; i+=1) {
        var multiplier = i*5;
        paper.circle(250 + (2*multiplier), 100 + multiplier, 50 - multiplier);
    }
};

