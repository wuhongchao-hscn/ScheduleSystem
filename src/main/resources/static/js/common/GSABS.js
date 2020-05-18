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

        return ajaxGet($(this), url, articleItemId, fun);
    });

    $("a[name='articleList']").click(function () {
        let itemId = $(this).attr('id');
        itemId = itemId.substr(0, itemId.length - 4);
        $('#' + itemId).show();
        $('#' + itemId + 'Display').remove();
        $(this).addClass('d-inline');
        return false;
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