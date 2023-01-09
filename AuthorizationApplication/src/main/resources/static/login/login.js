$(document).ready(function () {
    $(".dws-form").on("click", ".tab", function () {
        clickOnForm($(this))
    });
});
function clickOnForm(tab){
    $(".dws-form").find(".active").removeClass("active");
    let formId = tab.attr("form_id");
    $("#" + formId).addClass("active");
    tab.addClass("active");
    window.history.replaceState(null, null, "?form=" + formId);
}
