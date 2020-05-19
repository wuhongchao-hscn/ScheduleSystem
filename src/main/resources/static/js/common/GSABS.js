$(document).ready(function () {
    $("a[name='articleUp']").click(function () {
        let articleId = $(this).attr('value');
        let url = "/GSABSArticle/" + articleId;
        let articleItemId = 'article' + articleId;
        let fun = 'listArticleData';

        return ajaxGet($(this), url, articleItemId, fun);
    });
    $("a[name='titleUp']").click(function () {
        let titleId = $(this).attr('value');
        let url = "/GSABSTitle/" + titleId;
        let titleItemId = 'title' + titleId;
        let fun = 'listArticleData';

        return ajaxGet($(this), url, titleItemId, fun);
    });

    $("a[name='articleAgree']").click(function () {
        let articleId = $(this).attr('value');
        let agreeSpanItemId = 'article' + articleId + 'AgreeSpan';
        let agreeParam = getAgreeParam(articleId, true);

        let url = "/GSABSArticleAgree/" + articleId + '?agreeParam=' + agreeParam;
        let fun = "agreeUpdate";

        return ajaxGet($(this), url, agreeSpanItemId, fun);

    });

    $("a[name='articleDisAgree']").click(function () {
        let articleId = $(this).attr('value');
        let agreeSpanItemId = 'article' + articleId + 'AgreeSpan';
        let agreeParam = getAgreeParam(articleId, false);

        let url = "/GSABSArticleAgree/" + articleId + '?agreeParam=' + agreeParam;
        let fun = "agreeUpdate";

        return ajaxGet($(this), url, agreeSpanItemId, fun);
    });

    $("a[name='articleCountUp']").click(function () {
        let articleId = $(this).attr('value');
        let url = "/GSABSComments/" + articleId;
        let fun = 'listCommentData';
        let itemId = "comment" + articleId + "Div";

        $(this).hide();
        $(this).next().addClass('d-inline');

        return ajaxGet($(this), url, itemId, fun);
    });

    $("a[name='articleList']").click(function () {
        let itemId = $(this).attr('id');
        itemId = itemId.substr(0, itemId.length - 4);
        $('#' + itemId).show();
        $('#' + itemId + 'Display').remove();
        $(this).addClass('d-inline');
        return false;
    });

    $("a[name='articleCountList']").click(function () {
        let articleId = $(this).attr('value');
        let itemId = "comment" + articleId + "Div";

        $(this).removeClass('d-inline');
        $(this).prev().show();

        $('#' + itemId).next().remove();

    });
});

function listArticleData(data, itemId) {
    let item = $('#' + itemId);
    item.after('<pre id="' + itemId + 'Display">' + data + '</pre>');
    item.hide();
    $('#' + itemId + 'List').addClass('d-inline');
}

function getAgreeParam(articleId, agreeFlg) {
    let agreeItemId = 'article' + articleId + 'Agree';
    let disAgreeItemId = 'article' + articleId + 'DisAgree';

    let agreeItem = $('#' + agreeItemId);
    let disAgreeItem = $('#' + disAgreeItemId);

    let className = "btn-info";
    let agreeParam = 0;


    if (agreeItem.hasClass(className)) {
        agreeItem.removeClass(className);
        if (agreeFlg) {
            // ●〇⇒〇〇
            agreeParam = 1;
        } else {
            // ●〇⇒〇●
            agreeParam = 2;
            disAgreeItem.addClass(className);
        }
    } else if (disAgreeItem.hasClass(className)) {
        disAgreeItem.removeClass(className);
        if (agreeFlg) {
            // 〇●⇒●〇
            agreeParam = 3;
            agreeItem.addClass(className);
        } else {
            // 〇●⇒〇〇
            agreeParam = 4;
        }
    } else {
        if (agreeFlg) {
            // 〇〇⇒●〇
            agreeParam = 5;
            agreeItem.addClass(className);
        } else {
            // 〇〇⇒〇●
            agreeParam = 6;
            disAgreeItem.addClass(className);
        }

    }
    return agreeParam;
}

function agreeUpdate(data, itemId) {
    let item = $('#' + itemId);
    item.text(data);
}

function listCommentData(data, itemId) {
    let item = $('#' + itemId);

    let options =
        '<div class="card w-75">';

    if (data.commentCount) {
        options +=
            '<div class="card-header bg-white">' +
            ' <div class="row justify-content-between">' +
            '  <div class="col-4">' +
            '   ' + data.commentCount + ' 条评论' +
            '  </div>' +
            '  <div class="col-4 text-right">' +
            '   <svg class="bi bi-arrow-left-right" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">' +
            '    <path fill-rule="evenodd" d="M10.146 7.646a.5.5 0 01.708 0l3 3a.5.5 0 010 .708l-3 3a.5.5 0 01-.708-.708L12.793 11l-2.647-2.646a.5.5 0 010-.708z" clip-rule="evenodd"/>' +
            '    <path fill-rule="evenodd" d="M2 11a.5.5 0 01.5-.5H13a.5.5 0 010 1H2.5A.5.5 0 012 11zm3.854-9.354a.5.5 0 010 .708L3.207 5l2.647 2.646a.5.5 0 11-.708.708l-3-3a.5.5 0 010-.708l3-3a.5.5 0 01.708 0z" clip-rule="evenodd"/>' +
            '    <path fill-rule="evenodd" d="M2.5 5a.5.5 0 01.5-.5h10.5a.5.5 0 010 1H3a.5.5 0 01-.5-.5z" clip-rule="evenodd"/>' +
            '   </svg>' +
            '   时间排序' +
            '  </div>' +
            ' </div>' +
            '</div>';
    }

    options += '<div class="card-body">';
    let addFlg = data.level1List && data.level0List;
    if (data.level1List) {
        options += editBodyHtml(data.level1List, "精选评论", addFlg);
    }

    if (data.level0List) {
        options += editBodyHtml(data.level0List, "评论", addFlg);
    }
    options += '</div>';

    options +=
        ' <div class="card-footer bg-white">' +
        '  footer' +
        ' </div>' +
        '</div>';

    item.after(options);
}

function editBodyHtml(objList, levelName, addFlg) {
    let options = '';
    if (addFlg) {
        options += '<p class="bg-light">' + levelName + '</p>';
    }

    for (let i = 0; i < objList.length; i++) {
        if (0 < i) {
            options += '<hr/>';
        }
        options +=
            '<div class="row no-gutters">' +
            ' <div class="col-sm-6 col-md-8">' +
            '  <img src="/images/carousel3.jpg" width="20px" height="20px"/>' +
            '  ' + objList[i].userName +
            ' </div>' +
            ' <div class="col-6 col-md-4 text-right">' + objList[i].dateLong + '</div>' +
            '</div>' +
            '<pre>' + objList[i].comment + '</pre>';
    }
    return options;
}