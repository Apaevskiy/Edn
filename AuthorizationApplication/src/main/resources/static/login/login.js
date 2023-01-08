$(document).ready(function () {
    $(".dws-form").on("click", ".tab", function () {
        $(".dws-form").find(".active").removeClass("active");
        $("#"+$(this).attr("form_id")).addClass("active");
        $(this).addClass("active");
    });
});

function validLogin(form) {
    let fail = false;

    let email = form.email.value;
    let password = form.password.value;
    let adr_pattern = /[0-9a-z_-]+@[0-9a-z_-]+\.[a-z]{2,5}/i;

    if (email === "" || email === " ") {
        fail = "Вы не ввели email-адрес";
    } else if (adr_pattern.test(email) === false) {
        fail = "Вы ввели email-адрес неправильно";
    } else if (password === "" || password === " ") {
        fail = "Вы не ввели пароль";
    }

    if (fail) {
        alert(fail);
    } else {
        request("login " + email + " " + password)
    }
}

function validRegistration(form) {
    let fail = false;

    let email = form.email.value;
    let checkBox = form.registrationConditions.value;
    let adr_pattern = /[0-9a-z_-]+@[0-9a-z_-]+\.[a-z]{2,5}/i;

    if (!email || email === "") {
        fail = "Вы не ввели email-адрес";
    } else if (adr_pattern.test(email) === false) {
        fail = "Вы ввели email-адрес неправильно";
    } else if (checkBox === false) {
        fail = "Вы не приняли условия соглашения";
    }

    if (fail) {
        alert(fail);
    } else {
        request("registration " + email + " " + name + " " + password)
    }
}

function validRecoveryPassword(form) {
    let fail = false;

    let email = form.email.value;
    let adr_pattern = /[0-9a-z_-]+@[0-9a-z_-]+\.[a-z]{2,5}/i;

    if (!email || email === "") {
        fail = "Вы не ввели email-адрес";
    } else if (adr_pattern.test(email) === false) {
        fail = "Вы ввели email-адрес неправильно";
    }

    if (fail) {
        alert(fail);
    } else {
        request("recovery " + email)
    }
}

function request(message) {
    let xhr = new XMLHttpRequest();
    xhr.open('POST', '/login', true);
    xhr.send(message);
}