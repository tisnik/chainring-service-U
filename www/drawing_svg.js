var lastEventListener = null;
var lastEmbedSrc = 'bgimage.svg';
var lastEmbed = null;

// dummy logs for Internet Explorer 11:
if (!window.console) console = {log: function() {}};


// prototype for startsWith for Internet Explorer 11
if (!String.prototype.startsWith) {
    String.prototype.startsWith = function(searchString, position){
      position = position || 0;
      return this.substr(position, searchString.length) === searchString;
  };
}

Array.prototype.indexOf = function(obj, start) {
     for (var i = (start || 0), j = this.length; i < j; i++) {
         if (this[i] === obj) { return i; }
     }
     return -1;
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

    if (room === undefined || room == null || room == "") {
        return;
    }
    selectedRoom = room;

    var i = room.lastIndexOf(".")
    var floor = room.substring(0, i)
    floor_id = floor;
    version = "1";

    console.log("Selected building: " + building);
    console.log("Selected floor: " + floor);
    console.log("Selected room: " + room);

    var url = "/api/v1/svg-drawing?building-id=" + building + "&floor-id=" + floor + "&room-id=" + room;
    random = (Math.random() + 1).toString(36).substring(2);
    url += "&random=" + random;

    console.log(url);

    reloadSVG(url);
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

    // first selection
    select.options[select.options.length] = new Option("Vyberte", "", true);

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

    // first selection
    select.options[select.options.length] = new Option("Vyberte", "", true);

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
    if (building === undefined || building == null || building == "" || building.length <= 0) {
        return;
    }
    var url = "/api/v1/rooms-for-building?building-id=" + building + "&floor-id=" + building;
    random = (Math.random() + 1).toString(36).substring(2);
    url += "&random=" + random;
    console.log(url);

    var select = document.getElementById("rooms");
    select.options.length = 0;
    // first selection
    select.options[select.options.length] = new Option("Čekejte prosím", "", true);

    // try display list of rooms
    callAjax(url, onListOfRoomsReceived);
}

function readBuildings() {
    var url = "/api/v1/buildings";
    random = (Math.random() + 1).toString(36).substring(2);
    url += "?random=" + random;
    console.log(url);

    // try to display list of buildings
    callAjax(url, onListOfBuildingsReceived);
}

function initialize() {
    readBuildings();
    lastEmbed = createNewEmbed(lastEmbedSrc);
}

function createNewEmbed(src){
    var embed = document.createElement('embed');
    embed.setAttribute('style', 'width: 1000px; height: 1000px; border:1px solid black;');
    embed.setAttribute('type', 'image/svg+xml');
    embed.setAttribute('src', src);
  
    document.getElementById('drawing-div').appendChild(embed)
  
    lastEventListener = function(){
      svgPanZoom(embed, {
        zoomEnabled: true,
        controlIconsEnabled: true
      });
    }
    embed.addEventListener('load', lastEventListener)
  
    return embed
}

function removeEmbed(){
    // Destroy svgpanzoom
    svgPanZoom(lastEmbed).destroy()
    // Remove event listener
    lastEmbed.removeEventListener('load', lastEventListener)
    // Null last event listener
    lastEventListener = null
    // Remove embed element
    document.getElementById('drawing-div').removeChild(lastEmbed)
    // Null reference to embed
    lastEmbed = null
}


function reloadSVG(url) {
    // Remove last added svg
    removeEmbed()
    lastEmbed = createNewEmbed(url)
}
