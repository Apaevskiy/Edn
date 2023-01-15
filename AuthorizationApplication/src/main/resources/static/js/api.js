function sendHashes(button){
    button.disabled = true;
    let hashes =  document.getElementById('hashesField').value;
    let xhr = new XMLHttpRequest();
    xhr.open('POST', '/api/applications', true);
    xhr.setRequestHeader("Content-Type", "application/json");
    let token = $('#_csrf').attr('content');
    let header = $('#_csrf_header').attr('content');
    xhr.setRequestHeader(header, token);
    let data = {
        hashes : splitMulti(hashes, [',', '\n', ' '])
    };
    xhr.send(JSON.stringify(data));
    xhr.onload = function () {
        if (xhr.status === 202) {
            document.getElementById('applicationId').value = xhr.response;
            putMessage('Заявка успешно создана', false);
        } else if (xhr.status === 400){
            putMessage(xhr.response);
        } else {
            putMessage('Ошибка отправки запроса');
        }
        button.disabled = false;
    };
}
function getApplicationById(button){
    button.disabled = true;
    let xhr = new XMLHttpRequest();
    xhr.open('GET', '/api/applications/'+document.getElementById('applicationId').value, true);
    let token = $('#_csrf').attr('content');
    let header = $('#_csrf_header').attr('content');
    xhr.setRequestHeader(header, token);
    xhr.send();
    xhr.onload = function () {
        if (xhr.status === 200) {
            let resultField = document.getElementById('resultField');
            resultField.innerHTML = xhr.response;
            putMessage('Заявка успешно загружена', false);
        } else {
            putMessage('Ошибка отправки запроса');
        }
        button.disabled = false;
    };
}
function splitMulti(str, tokens){
    let tempChar = tokens[0]; // We can use the first token as a temporary join character
    for(let i = 1; i < tokens.length; i++){
        str = str.split(tokens[i]).join(tempChar);
    }
    str = str.split(tempChar);
    return str;
}
function putMessage(message, isError = true) {
    let toast = document.getElementById('toastExample').cloneNode(true);
    toast.removeAttribute('id');
    toast.querySelector('.alert > div').innerHTML = message;
    toast.querySelector('.alert').classList.add(isError ? 'alert-danger' : 'alert-success');
    toast.querySelector('.alert > svg > use').setAttributeNS('http://www.w3.org/1999/xlink', 'xlink:href', isError ? '#exclamation-triangle-fill' : '#check-circle-fill');
    document.getElementById('toastPlace').appendChild(toast);
    toast.classList.add('show');
    setTimeout(
        () => {
            toast.parentElement.removeChild(toast);
        },
        5 * 1000
    );
}