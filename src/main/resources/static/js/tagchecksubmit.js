function submitTagData(selectObj) {

	// タグ変更要求を送信
    var tagChangeElm = document.createElement('input');
    tagChangeElm.setAttribute('type', 'hidden');
    tagChangeElm.setAttribute('name', 'tagChange');
    tagChangeElm.setAttribute('value', selectObj.checked);
    document.form.appendChild(tagChangeElm);

	document.form.submit();
}