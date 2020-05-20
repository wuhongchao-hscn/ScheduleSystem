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

        $(this).hide();
        $(this).next().addClass('d-inline');

        return ajaxGet($(this), url, articleId, fun);
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

function listCommentData(data, articleId) {
    let itemId = "comment" + articleId + "Div";
    let item = $('#' + itemId);

    if (isNotExist(data, item)) {
        return;
    }
    let options = editHeadHtml(data, itemId, articleId);

    options += '<div class="card-body">';
    options += editBodyHtml(data);
    options += '</div>';

    options +=
        editFooterHtml(data) +
        '</div>';

    item.after(options);

    $("span[name='commentTimeSort']").click(function () {
        let articleId = $(this).attr('value');
        let url = "/GSABSComments/" + articleId + '?sortParam=0';
        let fun = 'listCommentData';

        $('#comment' + articleId + "Div").next().remove();

        return ajaxGet($(this), url, articleId, fun);
    });

    $("span[name='commentDefaultSort']").click(function () {
        let articleId = $(this).attr('value');
        let url = "/GSABSComments/" + articleId;
        let fun = 'listCommentData';

        $('#comment' + articleId + "Div").next().remove();

        return ajaxGet($(this), url, articleId, fun);
    });
}

function isNotExist(data, item) {
    let commentCount = data.commentCount;
    if (commentCount == 0) {
        let options =
            '<div class="card w-75">' +
            ' <div class="card-header bg-white">' +
            '    还没有评论' +
            ' </div>' +
            editFooterHtml(data) +
            '</div>';

        item.after(options);
        return true;
    }
    return false;
}

function editHeadHtml(data, itemId, articleId) {
    let commentCount = data.commentCount;
    let levelFlg = data.levelFlg
    let spanName = levelFlg ? "commentTimeSort" : "commentDefaultSort";
    let sortName = levelFlg ? "时间" : "默认";

    let options =
        '<div class="card w-75" id="' + itemId + 'Display">' +
        ' <div class="card-header bg-white">' +
        '  <div class="row justify-content-between">' +
        '   <div class="col-4">' +
        '    ' + commentCount + ' 条评论' +
        '   </div>' +
        '   <div class="col-4 text-right">' +
        '    <span name="' + spanName + '" value="' + articleId + '">' +
        '     <svg class="bi bi-arrow-left-right" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">' +
        '      <path fill-rule="evenodd" d="M10.146 7.646a.5.5 0 01.708 0l3 3a.5.5 0 010 .708l-3 3a.5.5 0 01-.708-.708L12.793 11l-2.647-2.646a.5.5 0 010-.708z" clip-rule="evenodd"/>' +
        '      <path fill-rule="evenodd" d="M2 11a.5.5 0 01.5-.5H13a.5.5 0 010 1H2.5A.5.5 0 012 11zm3.854-9.354a.5.5 0 010 .708L3.207 5l2.647 2.646a.5.5 0 11-.708.708l-3-3a.5.5 0 010-.708l3-3a.5.5 0 01.708 0z" clip-rule="evenodd"/>' +
        '      <path fill-rule="evenodd" d="M2.5 5a.5.5 0 01.5-.5h10.5a.5.5 0 010 1H3a.5.5 0 01-.5-.5z" clip-rule="evenodd"/>' +
        '     </svg>' +
        '     切换为' + sortName + '排序' +
        '    </span>' +
        '   </div>' +
        '  </div>' +
        ' </div>';

    return options;
}

function editBodyHtml(data) {
    if (!data.levelList) {
        return;
    }
    let objList = data.levelList
    let levelFlg = data.levelFlg
    let options = '';
    let level = '';
    for (let i = 0;
         i < objList.length;
         i++
    ) {
        let levelNow = objList[i].level;
        if (levelFlg && level != levelNow) {
            let levelName = 1 == levelNow ? "精选评论" : "评论";
            options += '<p class="bg-light">' + levelName + '</p>';
            level = levelNow;
        }

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

function editFooterHtml(data) {
    let options = '<div class="card-footer bg-white">';
    if (data.pageCnt) {
        let pageCnt = data.pageCnt;
        let pageNow = data.pageNow

        let prefixPageIndex = pageNow - 1;
        let suffixpageIndex = pageCnt - pageNow - 1;

        if (prefixPageIndex < 2) {
            for (let index = 1; index <= prefixPageIndex; index++) {
                options += getPageBtnItem(false, index);
            }
        } else {
            options += getPageBtnItem(false, 1);
            options += getPageBtnItem(false, "...");
            options += getPageBtnItem(false, prefixPageIndex);
        }
        options += getPageBtnItem(true, pageNow);

        if (suffixpageIndex < 2) {
            for (let index = 1; index <= prefixPageIndex; index++) {
                options += getPageBtnItem(false, index);
            }
        } else {
            options += getPageBtnItem(false, pageNow + 1);
            options += getPageBtnItem(false, "...");
            options += getPageBtnItem(false, pageCnt);
        }
    } else {
        options +=
            ' <div class="input-group">' +
            '  <input type="text" class="form-control" aria-label="Dollar amount (with dot and two decimal places)">' +
            '  <div class="input-group-append">' +
            '   <span class="input-group-text">' +
            '    <svg class="Zi Zi--Emotion" fill="currentColor" viewBox="0 0 24 24" width="24" height="24">' +
            '     <path d="M7.523 13.5h8.954c-.228 2.47-2.145 4-4.477 4-2.332 0-4.25-1.53-4.477-4zM12 21a9 9 0 1 1 0-18 9 9 0 0 1 0 18zm0-1.5a7.5 7.5 0 1 0 0-15 7.5 7.5 0 0 0 0 15zm-3-8a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3zm6 0a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3z"></path>' +
            '    </svg>' +
            '   </span>' +
            '   <button type="button" class="btn btn-sm btn-primary">发布</button>' +
            '  </div>' +
            ' </div>';
    }

    options +=
        '</div>';
    return options;
}

function getPageBtnItem(ableFlg, comment) {
    return '<button type="button" class="btn btn-link"' + (ableFlg ? ' disabled' : '') + '>' + comment + '</button>';
}