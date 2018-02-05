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
        if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) {
            callback(xmlHttpRequest.responseText);
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

function initializePaper(elementId, width, height) {
    paper = Raphael(document.getElementById(elementId), width, height);
    paper.clear();
}

function downloadDrawing(drawingId) {
    var url = "/vector-drawing?drawing-id=" + drawingId;
    // window.alert(url);
    callAjax(url, onLoadDrawing);
}

// Initialize container when document is loaded
window.onload = function () {
    downloadDrawing(1);
    initializePaper("drawing_canvas", 800, 600);
    var circle = paper.circle(100, 100, 80);
    for (var i = 0; i < 5; i+=1) {
        var multiplier = i*5;
        paper.circle(250 + (2*multiplier), 100 + multiplier, 50 - multiplier);
    }
};

