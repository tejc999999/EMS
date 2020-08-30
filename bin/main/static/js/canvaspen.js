window.onload = function() {
  document.getElementById('image').style.display = "none";
  var isDrag = false;
  var x = 0, y = 0;
  var ctx = document.getElementById('canvas').getContext('2d');

  function mousedown(event) {
    ctx.beginPath();
    isDrag = true;
  }
  function mouseup(event) {
    ctx.closePath();
    isDrag = false;
  }
  function mousemove(event) {
    x = event.layerX;
    y = event.layerY;
    if (!isDrag) {
      return;
    }
    ctx.lineTo(x, y);
    ctx.stroke();
  }
  function touchstart(event) {
    ctx.beginPath();
    isDrag = true;
    event.stopPropagation();
  }
  function touchend(event) {
    ctx.closePath();
    isDrag = false;
    event.stopPropagation();
  }
  function touchmove(event) {
    if (event.layerX === undefined) {
      x = event.touches[0].pageX - canvas.offsetLeft;
      y = event.touches[0].pageY - canvas.offsetTop;
    } else{
      x = event.layerX;
      y = event.layerY;
    }
    if (!isDrag) {
      return;
    }
    ctx.lineTo(x, y);
    ctx.stroke();
    event.preventDefault();
    event.stopPropagation();
  }

  let canvas = document.getElementById('canvas');
  canvas.addEventListener("mousedown", mousedown, false);
  canvas.addEventListener("mouseup", mouseup, false);
  canvas.addEventListener("mousemove", mousemove, false);
  canvas.addEventListener("touchstart", touchstart, false);
  canvas.addEventListener("touchend", touchend, false);
  canvas.addEventListener("touchmove", touchmove, false);
	
  canvas_init();
}

function canvas_init() {
  let container = document.getElementById('canvas-container');
  let canvas = document.getElementById('canvas');
  let image = new Image(); 
  let ctx = document.getElementById('canvas').getContext('2d');
  image.src=document.getElementById('image').src;
  canvas.width = container.clientWidth;
  canvas.height = image.height*(canvas.width/image.width);
  ctx.drawImage(image, 0, 0, image.width, image.height,
		 0, 0, canvas.width, image.height*(canvas.width/image.width));
  ctx.rect(0, 0, canvas.width, canvas.height);
  ctx.strokeStyle = "blue";
  ctx.lineWidth = 1;
  ctx.stroke();
  ctx.lineJoin="round";
  ctx.lineCap="round";
  change_pen_color('black');
  document.getElementById('font_bold_pt').innerText=ctx.lineWidth+"pt";
}


function change_pen_color(color) {
  let ctx = document.getElementById('canvas').getContext('2d');
  ctx.strokeStyle = color;
  if(color == "black") {
    document.getElementById('btn_pen_black').style.color="#FFFFFF";
    document.getElementById('btn_pen_blue').style.color="#000000";
    document.getElementById('btn_pen_red').style.color="#000000";
    document.getElementById('btn_pen_black').style.backgroundColor="#000080";
    document.getElementById('btn_pen_blue').style.backgroundColor="#b0c4de";
    document.getElementById('btn_pen_red').style.backgroundColor="#b0c4de";
  } else if(color == "blue") {
    document.getElementById('btn_pen_black').style.color="#000000";
    document.getElementById('btn_pen_blue').style.color="#FFFFFF";
    document.getElementById('btn_pen_red').style.color="#000000";
    document.getElementById('btn_pen_black').style.backgroundColor="#b0c4de";
    document.getElementById('btn_pen_blue').style.backgroundColor="#000080";
    document.getElementById('btn_pen_red').style.backgroundColor="#b0c4de";
  } else if(color == "red") {
    document.getElementById('btn_pen_black').style.color="#000000";
    document.getElementById('btn_pen_blue').style.color="#000000";
    document.getElementById('btn_pen_red').style.color="#FFFFFF";
    document.getElementById('btn_pen_black').style.backgroundColor="#b0c4de";
    document.getElementById('btn_pen_blue').style.backgroundColor="#b0c4de";
    document.getElementById('btn_pen_red').style.backgroundColor="#000080";
  }
}

function canvas_pen_thin() {
  let ctx = document.getElementById('canvas').getContext('2d');
  if(ctx.lineWidth > 1) {
    ctx.lineWidth = ctx.lineWidth - 1;
  }
  document.getElementById('font_bold_pt').innerText=ctx.lineWidth+"pt";
}
function canvas_pen_bold() {
  let ctx = document.getElementById('canvas').getContext('2d');
  if(ctx.lineWidth < 10) {
    ctx.lineWidth = ctx.lineWidth + 1;
  }
  document.getElementById('font_bold_pt').innerText=ctx.lineWidth+"pt";
}