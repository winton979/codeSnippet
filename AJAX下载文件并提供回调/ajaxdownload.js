function createFormAndSubmit(action,da){
    loading()
	$("#hideForm").remove();
	$("body").append('<form style="display:none;" id="hideForm" action="'+action+'" method="post"></form>');
	var form= $("#hideForm");
	var str="";
	for ( var name in da) {
		var val=da[name];
		if(val==null||val==''){
			continue;
		}
		str+='<input type="text" name="'+name+'" value="'+val+'" >';
	}
	form.append(str);
	/*$.post(action, form.serialize(), function (r) {
        console.log(r)
    })*/
    var xhr = new XMLHttpRequest();
    xhr.open('POST', action, true);
    xhr.responseType = 'blob';
    xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded; charset=UTF-8');
    xhr.onload = function(e) {
        console.log(e)
        loadingHide();
        if (this.status == 200) {
            var blob = new Blob([this.response], {type: 'application/vnd.ms-excel'});
            var downloadUrl = URL.createObjectURL(blob);
            var a = document.createElement("a");
            a.href = downloadUrl;
            a.download = "ª·‘±µÍ∆Ã∑÷Œˆ" + getDate() + ".xlsx";
            document.body.appendChild(a);
            a.click();
        } else {
            alert('Unable to download excel.')
        }
    };
    xhr.send(form.serialize());
}