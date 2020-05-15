const MENU_ACTION = "/commonMenu";
const LOGOUT_ACTION = "/commonLogout";
const RELOGIN_ACTION = "/commonReloginlink";

function menu() {
    location.href = MENU_ACTION;
}

function logout() {
    location.href = LOGOUT_ACTION;
}

function reloginlinkClick() {
    location.href = RELOGIN_ACTION;
}

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

$(document).ready(function () {
    $("a[name='articleUp']").click(function () {
        let articleId = $(this).attr('value');
        let url = "/GSABSArticle/" + articleId;
        let articleItemId = 'article' + articleId;
        let fun = 'listArticleData';

        return ajaxGet(url, articleItemId, fun);
    });
    $("a[name='titleUp']").click(function () {
        let titleId = $(this).attr('value');
        let url = "/GSABSTitle/" + titleId;
        let titleItemId = 'title' + titleId;
        let fun = 'listArticleData';

        return ajaxGet(url, articleItemId, fun);
    });

    $("a[name='articleList']").click(function () {
        let itemId = $(this).attr('id');
        itemId = itemId.substr(0, itemId.length - 4);
        $('#' + itemId).show();
        $('#' + itemId + 'Display').remove();
        $(this).addClass('d-inline');
        return false;
    });
});

let firstTimeFlg = true;

function isFirstTime() {
    if (firstTimeFlg == true) {
        firstTimeFlg = false;
        return true;
    }
    return false;
}

function changeTab(tabId) {
    let nodes = document.getElementsByTagName("div");
    for (let i = 0; i < nodes.length; i++) {
        if (nodes[i].className == "tab") {
            if (nodes[i].id == tabId) {
                nodes[i].style.display = "block";
            } else {
                nodes[i].style.display = "none";
            }
        }
    }
}

function hCopy(fObj) {
    if (fObj.strScheduleEndHmH.value == "") {
        let strScheduleStartHmH = fObj.strScheduleStartHmH.value;
        let checkScheduleStartHmH = strScheduleStartHmH.match(/^([0-9]|[0-9][0-9])$/);
        if (checkScheduleStartHmH) {
            fObj.strScheduleEndHmH.value = strScheduleStartHmH;
        }
    }
}

function mCopy(fObj) {
    if (fObj.strScheduleEndHmM.value == "") {
        let strScheduleStartHmM = fObj.strScheduleStartHmM.value;
        let checkScheduleStartHmM = strScheduleStartHmM.match(/^([0-9]|[0-9][0-9])$/);
        if (checkScheduleStartHmM) {
            fObj.strScheduleEndHmM.value = strScheduleStartHmM;
        }
    }
}

function setSysDate() {
    let sysDate = new Date();
    $("#strScheduleYykYmdY").val(sysDate.getFullYear());
    $("#strScheduleYykYmdM").val(sysDate.getMonth() + 1);
    $("#strScheduleYykYmdD").val(sysDate.getDate());
}

function doPostPre() {
    $("#GSAXS040").empty();
    let error_item_str = $('#errItemId').val();
    if (!error_item_str || error_item_str == '') {
        return;
    }
    let error_item_list = error_item_str.split(",");
    $.each(error_item_list, function (index, item) {
        $('#' + item).attr('error_class', $('#' + item).attr('class'));
        $('#' + item).attr('class', $('#' + item).attr('error_class'));
    });
    $('#errItemId').val('');
}

function doErrorCommon(data) {
    if (!data.error) {
        return false;
    }
    data = data.error;

    $.each(data.errItemId, function (index, item) {
        $('#' + item).attr('sucessclass', $('#' + item).attr('class'));
        $('#' + item).attr('class', $('#' + item).attr('error_class'));
    });
    $("#errItemId").val(data.errItemId.join(','));

    let errorHtml = '<div class="error">';
    $.each(data.errMsg, function (index, item) {
        errorHtml = errorHtml + '<p>' + item + '</p>';
    });
    errorHtml = errorHtml + '</div>';
    $("#GSAXS040").append(errorHtml);
    return true;
}

function doSuccessCommon(data) {
    if (!data.success) {
        return false;
    }

    data = data.success;
    let messageHtml = '<div class="message"><p>' + data.message + '</p></div>';
    $("#GSAXS040").append(messageHtml);

    doClearForm(true, false);

    return true;
}

function doClearForm(inputFlg, selectFlg) {
    if (inputFlg) {
        let formObj = $("form")[0];
        $(formObj).find(":text, :password, :file").each(function () {
            $(this).val("");
        });
        $(formObj).find(":checkbox").each(function () {
            $(this).prop("checked", false);
        });
    }

    if (selectFlg) {
        let formObj = $("form")[0];
        $(formObj).find("select").each(function () {
            $(this).find('option:first-child').attr('selected', "selected");
        });
    }
}

function articleAgree(articleId, agreeFlg) {

    let agreeItemId = 'article' + articleId + 'Agree';
    let disAgreeItemId = 'article' + articleId + 'DisAgree';
    let agreeSpanItemId = 'article' + articleId + 'AgreeSpan';

    let agreeItem = $('#' + agreeItemId);
    let disAgreeItem = $('#' + disAgreeItemId);
    let agreeSpanItem = $('#' + agreeSpanItemId);

    let className = "temrafafawfawfw";
    let agreeParam = 0;

    if (agreeFlg) {
        let activeFlg = agreeItem.hasClass(className);
        disAgreeItem.removeClass(className)
        if (activeFlg) {
            // agree cancel
            agreeParam = 1;
            agreeItem.removeClass(className);
        } else {
            // agree
            agreeParam = 2;
            agreeItem.addClass(className);
        }
    } else {
        let activeFlg = disAgreeItem.hasClass(className);
        agreeItem.removeClass(className);
        if (activeFlg) {
            // disAgree cancel
            agreeParam = 3;
            disAgreeItem.removeClass(className);
        } else {
            // disAgree
            agreeParam = 4;
            disAgreeItem.addClass(className);
        }
    }

    let url = "/GSABSArticleAgree/" + articleId + '?agreeParam=' + agreeParam;

    return ajaxGet(url, agreeSpanItem, fun);
    return false;
}

function ajaxGet(url, itemId, fun) {
    $.ajax({
        url: url,
        method: "GET",
        success: function (data) {
            eval(fun + "(data, itemId)");
        },
        error: function (e) {
            console.log(e)
        }
    });
    return false;
}

function listArticleData(data, itemId) {
    let item = $('#' + itemId);
    item.after('<pre id="' + itemId + 'Display">' + data + '</pre>');
    item.hide();
    $('#' + itemId + 'List').addClass('d-inline');
}

function agreeUpdate(data, itemId) {
    let item = $('#' + itemId);
    item.text(data);
}