function clearFields() {
    document.getElementById("roleSearchField").selectedIndex = 0;
    document.getElementById("emailSearchField").value = '';
}

$(function () {
    $("#usersTable").on("click", "td.username", function () {
        getUserByUsername($(this).text());
    });
});


function openEmptyModel() {
    openModal(null);
}

function openModal(user) {
    console.log(user);
    $('#newPasswordField').val(null);
    $('#newPasswordConfirmField').val(null);
    if (user != null) {
        $('#editIdField').val(user.id);
        $('#editUsernameField').val(user.username);
        $("[name=role]").val([user.role.id]);
        $("[name=active]").val([user.active]);
        $('#addUserButton').addClass('d-none');
        $('#deleteUserButton').removeClass('d-none');
        $('#saveUserButton').removeClass('d-none');
    } else {
        $('#editIdField').val(null);
        $('#editUsernameField').val(null);
        $('#roleRadio').val(null);
        $('#addUserButton').removeClass('d-none');
        $('#deleteUserButton').addClass('d-none');
        $('#saveUserButton').addClass('d-none');
    }
    $('#editUserModal').modal();
}

function getUserByUsername(username) {
    let xhr = new XMLHttpRequest();
    xhr.open('GET', '/users/' + username);
    xhr.send();

    xhr.onload = function () {
        if (xhr.status === 200) {
            openModal(JSON.parse(xhr.response));
        } else {
            putMessage('Ошибка отправки запроса');
        }
    };
}

function addUser() {
    editUser('PUT');
}

function deleteUser() {
    editUser('DELETE');
}

function updateUser() {
    editUser('PATCH');
}

function editUser(method, successFunction) {
    let form = document.forms['editUserForm'];
    let formData = new FormData(form);

    let xhr = new XMLHttpRequest();
    xhr.open(method, '/users', true);
    xhr.send(formData);

    xhr.onload = function () {
        if (xhr.status !== 200) {
            if (xhr.status === 400) {
                JSON.parse(xhr.response).forEach(element => putMessage(element));
            } else {
                putMessage('Ошибка отправки запроса');
            }
        } else {
            putMessage(JSON.parse(xhr.response), false);
            successFunction();
        }
    };
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