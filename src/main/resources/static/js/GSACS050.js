$(document).ready(function () {
    onLoadGsacs050Form();
});

function getNewSelect(obj) {
    let url = "/getSelect/";
    if (obj) {
        let value = obj.value;
        $(obj).nextAll().remove();
        if ("" == value) {
            return;
        }
        url = url + value;
    } else {
        url = url + null;
    }
    $.ajax({
        url: url,
        type: "GET",
        success: function (data) {
            addOptions(data);
        },
        error: function (e) {
            console.log(e)
        }
    });
}

function doInsert(reInsertFlg) {
    let selectTemp = "";
    $("select[name='strSskTemp']").each(function () {
        if ("" != $(this).val()) {
            selectTemp = $(this).val();
        }
    });

    $("#strSsk").val(selectTemp);

    let formData = new FormData($("form")[0]);
    doPostPre();
    $.ajax({
        url: "/GSACS050Register",
        type: "POST",
        data: formData,
        dataType: 'json',
        cache: false,        // 不缓存数据
        processData: false,  // 不处理数据
        contentType: false,   // 不设置内容类型
        success: function (data) {
            if (doErrorCommon(data)) {
                return false;
            }
            doSuccessCommon(data);
            doSuccessRtn(data, reInsertFlg);
        },
        error: function (eData) {
            console.log(eData);
        }
    });
}

function addOptions(data) {
    if ("" == data) {
        return;
    }
    let options = '<select class="custom-select w-auto" name="strSskTemp" onchange="getNewSelect(this)">';
    if (data.length) {
        for (let i = 0; i < data.length; i++) {
            options += '<option value="' + data[i].key + '" >' + data[i].value + '</option>';
        }
    } else {
        $.each(data, function (key, value) {
            options += '<option value="' + key + '" >' + value + '</option>';
        })
    }
    options += '</select>&nbsp;&nbsp;';

    $('#strSskTd').append(options);
}

function onLoadGsacs050Form() {
    $("input[name='rlId']").first().prop("checked", true);
    getNewSelect(null);
}

function doSuccessRtn(data, reInsertFlg) {
    if (!data.success) {
        return;
    }

    if (reInsertFlg) {
        $('#strSskTd').empty();
        onLoadGsacs050Form();
    } else {
        window.location.href = 'GSACS050Detail?userId=' + data.success.userId;
    }
}