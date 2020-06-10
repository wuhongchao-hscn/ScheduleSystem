window.onload = function () {
    bsCustomFileInput.init();
    let error_item_str = $('#errItemId').val();
    if (!error_item_str || error_item_str == '') {
        return;
    }
    let error_item_list = error_item_str.split(",");
    $.each(error_item_list, function (index, value) {
        $('#' + value).attr('class', $('#' + value).attr('error_class'));
    });
}

let firstTimeFlg = true;

function isFirstTime() {
    if (firstTimeFlg == true) {
        firstTimeFlg = false;
        return true;
    }
    return false;
}

function ajaxGet(clickItem, url, itemId, fun) {
    $.ajax({
        url: url,
        method: "GET",
        beforeSend: function (clickItem) {
            $(clickItem).attr("onclick", "javascript:void();");
        },
        success: function (data) {
            eval(fun + "(data, itemId)");
            $(clickItem).removeAttr('onclick');
        },
        error: function (e) {
            console.log(e);
            $(clickItem).removeAttr("onclick");
        }
    });
    return false;
}

function ajaxPost(clickItem, url, data, itemId, fun) {
    $.ajax({
        headers: {'X-XSRF-TOKEN': $("input[name='_csrf']").attr("value")},
        url: url,
        method: "POST",
        data: data,
        beforeSend: function (clickItem) {
            $(clickItem).attr("onclick", "javascript:void();");
        },
        success: function (data) {
            eval(fun + "(data, itemId)");
            $(clickItem).removeAttr('onclick');
        },
        error: function (e) {
            console.log(e);
            $(clickItem).removeAttr("onclick");
        }
    });
    return false;
}

function addJsMsg(msg) {
    $('#GSAXS040').after('<div class="jsMsg"><b>' + msg + '</b></div>');
    setTimeout(removeJsMsg, 1000);
}

function removeJsMsg() {
    $('div.jsMsg').remove();
}