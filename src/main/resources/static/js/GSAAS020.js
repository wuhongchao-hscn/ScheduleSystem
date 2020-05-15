$(document).ready(function () {
    let radioCheckFlg = false;

    $('.checkRadio').each(function () {
        radioCheckFlg = true;
        return false;
    });

    if (!radioCheckFlg) {
        $("#checkRadio1").click();
    }

})