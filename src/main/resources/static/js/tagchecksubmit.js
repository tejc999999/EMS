function submitTagData(selectObj) {

	// チェック状態
    var tagChangeElm = document.createElement('input');
    tagChangeElm.setAttribute('type', 'hidden');
    tagChangeElm.setAttribute('name', 'tagChange');
    tagChangeElm.setAttribute('value', selectObj.checked);
    document.form.appendChild(tagChangeElm);

    // タグ値
    var tagValueElm = document.createElement('input');
    tagValueElm.setAttribute('type', 'hidden');
    tagValueElm.setAttribute('name', 'tagValue');
    tagValueElm.setAttribute('value', selectObj.value);
    document.form.appendChild(tagValueElm);
    // タグ変更要求を送信
    document.form.submit();
}