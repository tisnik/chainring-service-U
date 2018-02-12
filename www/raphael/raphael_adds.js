Raphael.fn.circularArc = function(x, y, radius, startAngle, endAngle) {
    var startX = x+radius*Math.cos(startAngle*Math.PI/180); 
    var startY = y-radius*Math.sin(startAngle*Math.PI/180);
    var endX = x+radius*Math.cos(endAngle*Math.PI/180); 
    var endY = y-radius*Math.sin(endAngle*Math.PI/180);

    var largeArcFlag = endAngle - startAngle <= 180 ? "0" : "1";

    var d = [
        "M", startX, startY, 
        "A", radius, radius, 0, largeArcFlag, 0, endX, endY
    ].join(" ");
    return this.path(d); //.attr("stroke", "#0000FF");
};

