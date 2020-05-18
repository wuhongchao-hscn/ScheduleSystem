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