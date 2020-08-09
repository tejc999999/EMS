
window.onload = function() {
  var allcheckobj = document.getElementById("allCheck");
  allcheckobj.addEventListener('change', function () {
	var checkflg = document.getElementById("allCheck").checked;
	var targetquestions = document.getElementsByClassName("targetquestion");
	for (let i = 0; i < targetquestions.length; i++) {
	  targetquestions[i].checked = checkflg;
	}
  });
}