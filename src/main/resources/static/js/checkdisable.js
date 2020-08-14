window.onload = function() {
  var allcheckobj = document.getElementById("disableCheck");
  allcheckobj.addEventListener('change', function () {
	var targetdisable = document.getElementById("targetDisable");
	targetdisable.value="password";
	targetdisable.disabled=document.getElementById("disableCheck").checked;
  });
}