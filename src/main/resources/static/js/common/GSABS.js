const gifArr = ['欢呼', '干杯', '熬夜', '超得意', '吃瓜', '吃惊', '哈哈', '爱心', '摊手',
    '机智', '害羞', '棒', '安慰', '超开心', '不抬杠', '赞同', '冷静一下',
    '疑惑', '思考', '呼叫管家', '小建议', '是不是', '哼'];

$(document).ready(function () {
    let parentItem = $("div.card-body.overflow-auto");

    $('#articleScrollDiv').scroll(function () {
        let popover = $('.popover');

        if (popover.length > 0) {

            let location = popover.css('transform').replace(/[^0-9\-,]/g, '').split(',')[13];
            if (location < 70) {
                popover.addClass('d-none');
            } else {
                popover.removeClass('d-none');
            }
        }
    });

    ////////////////////// 文章列表区域 //////////////////////
    // 阅读全文-文章
    parentItem.delegate("a[name='articleUp']", "click", function () {
        let articleId = $(this).attr('value');
        let url = "/GSABSArticle/" + articleId;
        let articleItemId = 'article' + articleId;
        let fun = 'listArticleData';

        return ajaxGet($(this), url, articleItemId, fun);
    });

    // 阅读全文-标题
    parentItem.delegate("a[name='titleUp']", "click", function () {
        let titleId = $(this).attr('value');
        let url = "/GSABSTitle/" + titleId;
        let titleItemId = 'title' + titleId;
        let fun = 'listArticleData';

        return ajaxGet($(this), url, titleItemId, fun);
    });

    // 文章赞同
    parentItem.delegate("a[name='articleAgree']", "click", function () {
        let articleId = $(this).attr('value');
        let agreeSpanItemId = 'article' + articleId + 'AgreeSpan';
        let agreeParam = getAgreeParam(articleId, true);

        let url = "/GSABSArticleAgree/" + articleId + '?agreeParam=' + agreeParam;
        let fun = "agreeUpdate";

        return ajaxGet($(this), url, agreeSpanItemId, fun);

    });

    // 文章反对
    parentItem.delegate("a[name='articleDisAgree']", "click", function () {
        let articleId = $(this).attr('value');
        let agreeSpanItemId = 'article' + articleId + 'AgreeSpan';
        let agreeParam = getAgreeParam(articleId, false);

        let url = "/GSABSArticleAgree/" + articleId + '?agreeParam=' + agreeParam;
        let fun = "agreeUpdate";

        return ajaxGet($(this), url, agreeSpanItemId, fun);
    });

    // 文章反对
    parentItem.delegate("a[name='articleCountUp']", "click", function () {
        let articleId = $(this).attr('value');
        let url = "/GSABSComments/" + articleId;
        let fun = 'listCommentData';

        $(this).hide();
        $(this).next().addClass('d-inline');

        return ajaxGet($(this), url, articleId, fun);
    });

    // 文章评论展开
    parentItem.delegate("a[name='articleList']", "click", function () {
        let itemId = $(this).attr('id');
        itemId = itemId.substr(0, itemId.length - 4);
        let item = $('#' + itemId)
        item.show();
        // 让滚动条回到打开之前的状态
        parentItem.delegate("#articleScrollDiv").scrollTop(item.offset().top);
        $('#' + itemId + 'Display').remove();
        $(this).addClass('d-inline');
        return false;
    });

    // 文章评论收起
    parentItem.delegate("a[name='articleCountList']", "click", function () {
        let articleId = $(this).attr('value');
        let itemId = "comment" + articleId + "Div";

        $(this).removeClass('d-inline');
        $(this).prev().show();

        $('#' + itemId).next().remove();

    });

    // 文章分享
    parentItem.delegate("a[name='shareLinkCopy']", "click", function () {
        let value = $(this).attr('value');
        let hrefUrl = $(this).attr('href')
        let copyValue = value + "\r\n" + hrefUrl;

        let oInput = document.createElement('textarea');
        oInput.value = copyValue;
        document.body.appendChild(oInput);
        oInput.select();
        document.execCommand("Copy");
        oInput.style.display = 'none';

        $('#GSAXS040').after('<div class="copyMsg"><b>链接复制成功</b></div>');

        setTimeout(removeCopyMsg, 500);
        return false;
    });

    // 文章收藏
    parentItem.delegate("a[name='articleCollect']", "click", function () {
        let articleId = $(this).attr('value');
        $('#createFolderBtn').val(articleId);
        $('#collectModal').modal('show');
        return false;
    });

    // 文章喜欢、取消喜欢
    parentItem.delegate("a[name='articleLike']", "click", function () {
        let articleId = $(this).attr('value');
        let url = "/GSABSLikes/" + articleId;
        let fun = 'updateLikesArea';

        return ajaxGet($(this), url, this, fun);
    });

    ////////////////////// 收藏夹一览页面 //////////////////////
    // 收藏夹一览页面表示事件
    $('#collectModal').on('show.bs.modal', function (event) {
        showCollectModal();
    });

    // 收藏夹一览页面关闭事件
    $('#createFolderModal').on('hide.bs.modal', function (event) {
        $('#collectTitle').val('');
        $('#collectContent').val('');
        $('#levelPublic').click();
        $('#createFolderBtn').attr('disabled', 'disabled');
    });

    // 收藏夹一览页面关闭按钮
    parentItem.delegate(".modalCloseButton", "click", function () {
        $('#collectModal').modal('hide');
        $('#createFolderModal').modal('hide');
    });

    // 收藏夹一览页面创建收藏夹按钮
    parentItem.delegate("#moveToFolder", "click", function () {
        $('#collectModal').modal('hide');
        $('#createFolderModal').modal('show');
    });

    // 收藏、取消收藏
    parentItem.delegate("button[name='collectArticle']", "click", function () {
        let articleId = $('#moveToFolder').val();
        let folderId = $(this).val();

        let url = "/GSABSCollect/" + articleId + '/' + folderId;
        let fun = 'updateCollectModal';

        ajaxGet($(this), url, this, fun);
    });

    ////////////////////// 收藏夹创建页面 //////////////////////
    // 收藏夹标题事件
    parentItem.delegate("#collectTitle").change(function () {
        if ($(this).val()) {
            $('#createFolderBtn').removeAttr('disabled');
        } else {
            $('#createFolderBtn').attr('disabled', "");
        }
    });

    // 收藏夹创建页面返回按钮
    parentItem.delegate("#backToCollect", "click", function () {
        $('#createFolderModal').modal('hide');
        $('#collectModal').modal('show');
    });

    // 收藏夹创建页面确认创建按钮
    parentItem.delegate("#createFolderBtn", "click", function () {
        let title = $('#collectTitle').val();
        let content = $('#collectContent').val();
        let level = $('input:radio[name="collectLevel"]:checked').val();

        let url = "/GSABSFolder?title=" + title + "&content=" + content + "&level=" + level;
        url = encodeURI(url);// 进行utf-8编码
        let fun = 'updateCollectModal';

        return ajaxGet($(this), url, null, fun);
    });

    ////////////////////// 评论一览区域 //////////////////////
    // 按时间排序
    parentItem.delegate("span[name='commentTimeSort']", "click", function () {
        let articleId = $(this).attr('value');
        let url = "/GSABSComments/" + articleId + '?sortParam=0';
        let fun = 'listCommentData';

        $('#comment' + articleId + "Div").next().remove();

        return ajaxGet($(this), url, articleId, fun);
    });

    // 默认排序
    parentItem.delegate("span[name='commentDefaultSort']", "click", function () {
        let articleId = $(this).attr('value');
        let url = "/GSABSComments/" + articleId;
        let fun = 'listCommentData';

        $('#comment' + articleId + "Div").next().remove();

        return ajaxGet($(this), url, articleId, fun);
    });

    // 分页跳转
    parentItem.delegate("button[name='pageLinkDefault']", "click", function () {
        let articleId = $(this).attr('value');
        let pageNow = $(this).attr('page');
        let url = "/GSABSComments/" + articleId + '?pageNow=' + pageNow;
        let fun = 'listCommentData';

        $('#comment' + articleId + "Div").next().remove();

        return ajaxGet($(this), url, articleId, fun);
    });

    // 按时间排序-分页
    parentItem.delegate("button[name='pageLinkTime']", "click", function () {
        let articleId = $(this).attr('value');
        let pageNow = $(this).attr('page');
        let url = "/GSABSComments/" + articleId + '?pageNow=' + pageNow + '&sortParam=0';
        let fun = 'listCommentData';

        $('#comment' + articleId + "Div").next().remove();

        return ajaxGet($(this), url, articleId, fun);
    });

    // 评论发布按钮活性控制
    parentItem.delegate("input[name='commentInput']", "change", function () {
        let btn = $(this).next().find("button[name='commentAdd']");
        if ($(this).val()) {
            btn.removeAttr('disabled');
        } else {
            btn.attr('disabled', "");
        }
    });

    // 评论回复
    parentItem.delegate("a[name='commentReply']", "click", function () {
        let spantext = $(this).find("span");
        if ("回复" == spantext.text()) {
            let articleId = $(this).attr('value');
            let parentId = $(this).attr('parentid');
            let placeholder = "回复 " + $(this).attr('username');
            $(this).parent().after(commentAddItem(articleId, parentId, placeholder));
            spantext.text('取消回复');
        } else {
            $(this).parent().next().remove();
            spantext.text('回复');
        }

        return false;
    });

    // 评论发布
    parentItem.delegate("button[name='commentAdd']", "click", function () {
        let articleId = $(this).attr('value');
        let parentId = $(this).attr('parentid');
        let content = $(this).parent().parent().find("input[name='commentInput']").val();
        let url = "/GSABSComment/" + articleId + '?content=' + content;
        if (parentId) {
            url += "&parentId=" + parentId;
        }
        url = encodeURI(url);// 进行utf-8编码
        let fun = 'addCommentData';

        return ajaxGet($(this), url, this, fun);
    });

    // 表情列表展开
    parentItem.delegate('[data-toggle="popover"]', "click", function () {
        let describedby = $(this).attr('aria-describedby');

        if (describedby) {
            popoverDispose($(this));
            return;
        }

        $(this).find("svg:first").attr('fill', 'blue');

        let content = '';
        for (let e in gifArr) {
            let item = gifArr[e];
            content +=
                ' <div class="text-center float-left p-1">' +
                '  <img width="45px" height="45px" src="/images/gif/' + item + '.gif" alt="' + item + '"/>' +
                '  <p class="text-secondary font-weight-light"><small>' + item + '</small></p>' +
                '</div>';
        }

        let template =
            '<div class="popover h-25" role="tooltip">' +
            ' <div class="arrow"></div>' +
            ' <div class="popover-body overflow-auto h-100" name="gifItem"></div>' +
            '</div>';

        $(this).popover({
            animation: true, // 淡入淡出
            content: content,// 内容
            html: true, // 允许html
            template: template,
            container: '#articleScrollDiv',
            // trigger: 'focus',
        });

        $(this).popover('show');
    });

    // 表情选择
    parentItem.delegate('div.float-left', "click", function () {
        let img = $(this).find('img');
        let imgSrc = img.attr('src');
        let imgAlt = img.attr('alt');
        let popoverDiv = $(this).parent().parent();
        let emotionBtn = $('button[aria-describedby="' + popoverDiv.attr('id') + '"]');
        let articleId = emotionBtn.attr('value');
        emotionBtn.attr("disabled", "");


        let content =
            '<img width="140px" height="140px" src="' + imgSrc + '" alt="' + imgAlt + '"/>' +
            '<p class="text-secondary text-center">' + imgAlt + '</p>' +
            '<button class="popoverCloseButton" type="button" value="' + articleId + '">' +
            ' <svg xmlns="http://www.w3.org/2000/svg" fill="black" viewBox="0 0 24 24" width="20" height="20">' +
            '  <path fill-rule="evenodd" d="M 13.486 12 l 5.208 -5.207 a 1.048 1.048 0 0 0 -0.006 -1.483 a 1.046 1.046 0 0 0 -1.482 -0.005 L 12 10.514 L 6.793 5.305 a 1.048 1.048 0 0 0 -1.483 0.005 a 1.046 1.046 0 0 0 -0.005 1.483 L 10.514 12 l -5.208 5.207 a 1.048 1.048 0 0 0 0.006 1.483 a 1.046 1.046 0 0 0 1.482 0.005 L 12 13.486 l 5.207 5.208 a 1.048 1.048 0 0 0 1.483 -0.006 a 1.046 1.046 0 0 0 0.005 -1.482 L 13.486 12 Z">' +
            '  </path>' +
            ' </svg>' +
            '</button>';

        popoverDiv.find('.popover-body').html(content);
    });

    // 表情移除
    parentItem.delegate('button.popoverCloseButton', "click", function () {
        let articleId = $(this).attr('value');
        let btnItem = $('button[data-toggle="popover"][value="' + articleId + '"]');

        popoverDispose(btnItem);
    });
});

function listArticleData(data, itemId) {
    let item = $('#' + itemId);

    let content = data.content;
    let options = '';
    if (content) {
        // Article
        options +=
            '<div class="media w-75" id="' + itemId + 'Display">' +
            ' <div class="media-body">';

        let userInfo = data.userInfo;
        if (userInfo) {
            let agree = item.next().find('span[name="agree"]').text();
            options +=
                '<img src="/commonGetImg/' + userInfo.user_image + '?param=user&height=20&width=20"' +
                '     width="20x" height="20px" class="rounded-circle" alt="利用者画像"/>' +
                '<span>' + userInfo.shkin_smi + '</span>' +
                '<br/>' +
                '<span class="font-weight-light">' + agree + '人赞成了该回答</span>' +
                '<br/>';
        }

        options +=
            '  <pre>' + content + '</pre>' +
            ' </div>' +
            '</div>';
    } else {
        // Title
        options += '<pre>' + data + '</pre>';
    }

    item.after(options);
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

    if (isCommentExist(data, item, articleId)) {
        let options = editHeadHtml(data, itemId, articleId);

        options += editBodyHtml(data, articleId);

        options +=
            editFooterHtml(data, articleId) +
            '</div>';

        item.after(options);
    }
}

function isCommentExist(data, item, articleId) {
    let commentCount = data.commentCount;
    if (commentCount == 0) {
        let options =
            '<div class="card w-75" id="comment' + articleId + 'DivDisplay">' +
            ' <div class="card-header border-bottom-0 bg-white">' +
            '    <span name="commentCountHeader"><b>还没有评论</b></span>' +
            ' </div>' +
            ' <div class="card-body d-none" name="comment' + articleId + 'Body">' +
            ' </div>' +
            editFooterHtml(data, articleId) +
            '</div>';

        item.after(options);
        return false;
    }
    return true;
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
        '    <span name="commentCountHeader">' + commentCount + '</span> 条评论' +
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

function editBodyHtml(data, articleId) {
    let objList = data.levelList;
    let levelFlg = data.levelFlg;

    let options = '<div class="card-body" name="comment' + articleId + 'Body">';
    options += makeCommentHtml(objList, levelFlg, articleId);
    options += '</div>';

    return options;
}

function makeCommentHtml(objList, levelFlg, articleId) {
    let options = '';
    let level = '';
    for (let i = 0; i < objList.length; i++) {
        let obj = objList[i];
        let levelNow = objList[i].level;
        if (levelFlg && level != levelNow) {
            let levelName = 1 == levelNow ? "精选评论" : "评论";
            options += '<p class="bg-light">' + levelName + '</p>';
            level = levelNow;
        } else if (0 < i) {
            options += '<hr/>';
        }

        options += editLineHtml(obj, articleId);

        if (0 < objList[i].childs.length) {
            options += '<div class="pad-left-40"><hr/>';
            options += makeCommentHtml(objList[i].childs, false, articleId);
            options += '</div>';
        }
    }
    return options;
}

function editLineHtml(obj, articleId) {
    let options =
        '<div name="line' + obj.id + 'Div">' +
        ' <div class="row no-gutters">' +
        '  <div class="col-md-8">' +
        '   <img src="/images/carousel3.jpg" width="20px" height="20px"/>' +
        '   ' + obj.userName +
        '  </div>' +
        '  <div class="col-md-4 text-right text-secondary">' + obj.dateLong + '</div>' +
        ' </div>' +
        ' <div class="row no-gutters">' +
        '  <div class="col-md-8">' +
        '   <pre>' + obj.comment + '</pre>' +
        '  </div>' +
        '  <div class="col-md-4 text-right text-secondary">' +
        '   <a class="text-secondary" name="commentReply"' +
        '      value="' + articleId + '" parentId="' + obj.id + '" username="' + obj.userName + '">' +
        '    <svg fill="currentColor" viewBox="0 0 24 24" width="16" height="16" style="margin-right: 5px;">' +
        '     <path d="M22.959 17.22c-1.686-3.552-5.128-8.062-11.636-8.65-.539-.053-1.376-.436-1.376-1.561V4.678c0-.521-.635-.915-1.116-.521L1.469 10.67a1.506 1.506 0 0 0-.1 2.08s6.99 6.818 7.443 7.114c.453.295 1.136.124 1.135-.501V17a1.525 1.525 0 0 1 1.532-1.466c1.186-.139 7.597-.077 10.33 2.396 0 0 .396.257.536.257.892 0 .614-.967.614-.967z" fill-rule="evenodd">' +
        '     </path>' +
        '    </svg>' +
        '    <span>回复</span>' +
        '   </a>' +
        '  </div>' +
        ' </div>' +
        '</div>';
    return options;
}

function editFooterHtml(data, articleId) {
    let levelFlg = data.levelFlg
    let options = '<div class="card-footer bg-white">';
    if (data.pageCnt) {
        let pageCnt = data.pageCnt;
        let pageNow = data.pageNow

        options += getPageBtnItemByPage(articleId, levelFlg, 1 == pageNow, pageNow - 1, "<small>上一页</small>");
        if (pageNow <= 3) {
            for (let index = 1; index < pageNow; index++) {
                options += getPageBtnItem(articleId, levelFlg, false, index);
            }
        } else {
            options += getPageBtnItem(articleId, levelFlg, false, 1);
            options += getPageBtnItem(articleId, levelFlg, true, "...");
            options += getPageBtnItem(articleId, levelFlg, false, pageNow - 1);
        }
        options += getPageBtnItem(articleId, levelFlg, true, pageNow);

        if (pageNow + 2 >= pageCnt) {
            for (let index = pageNow + 1; index <= pageCnt; index++) {
                options += getPageBtnItem(articleId, levelFlg, false, index);
            }
        } else {
            options += getPageBtnItem(articleId, levelFlg, false, pageNow + 1);
            options += getPageBtnItem(articleId, levelFlg, true, "...");
            options += getPageBtnItem(articleId, levelFlg, false, pageCnt);
        }
        options += getPageBtnItemByPage(articleId, levelFlg, pageCnt == pageNow, pageNow + 1, "<small>下一页</small>");
    } else {
        options += commentAddItem(articleId, null, "写下你的评论");
    }

    options +=
        '</div>';
    return options;
}

function commentAddItem(articleId, parentId, placeholder) {
    let btnHtml = '<button type="button" class="btn btn-sm btn-primary" name="commentAdd" disabled value="' + articleId + '"';
    if (parentId) {
        btnHtml += 'parentid="' + parentId + '"';
    }

    btnHtml += '>发布</button>';

    let options =
        '<div class="input-group">' +
        ' <input type="text" class="form-control" name="commentInput" placeholder="' + placeholder + '">' +
        ' <div class="input-group-append">' +
        '  <button class="input-group-text" type="button" data-toggle="popover" value="' + articleId + '" data-placement="bottom">' +
        '   <svg class="Zi Zi--Emotion" fill="currentColor" viewBox="0 0 24 24" width="24" height="24">' +
        '    <path d="M7.523 13.5h8.954c-.228 2.47-2.145 4-4.477 4-2.332 0-4.25-1.53-4.477-4zM12 21a9 9 0 1 1 0-18 9 9 0 0 1 0 18zm0-1.5a7.5 7.5 0 1 0 0-15 7.5 7.5 0 0 0 0 15zm-3-8a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3zm6 0a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3z"></path>' +
        '   </svg>' +
        '  </button>' +
        '  ' + btnHtml +
        ' </div>' +
        '</div>';
    return options;
}

function getPageBtnItem(articleId, levelFlg, disableFlg, comment) {
    return getPageBtnItemByPage(articleId, levelFlg, disableFlg, comment, comment);
}

function getPageBtnItemByPage(articleId, levelFlg, disableFlg, page, comment) {
    let options = '<button type="button" class="btn btn-link"';
    if (levelFlg) {
        options += ' name="pageLinkDefault"';
    } else {
        options += ' name="pageLinkTime"';
    }

    if (disableFlg) {
        options += ' disabled';
    }

    options += ' value="' + articleId + '" page="' + page + '">' + comment + '</button>';

    return options;
}

function removeCopyMsg() {
    $('div.copyMsg').remove();
}

function updateLikesArea(data, item) {
    if (0 == data) {
        $(item).children("span").text("喜欢");
    } else {
        $(item).children("span").text("取消喜欢");
    }
}

function showCollectModal() {
    let articleId = $('#createFolderBtn').val();
    let url = "/GSABSCollectList/" + articleId;
    let fun = 'listCollectModal';

    ajaxGet($(this), url, articleId, fun);
}

function listCollectModal(data, itemId) {
    let modal = $('#collectModal');
    let options = '<div class="container-fluid">';
    let activeId = data.activeId;
    let folders = data.folders;


    for (let i = 0; i < folders.length; i++) {
        let obj = folders[i];
        options += editCollectHtml(obj, activeId)
    }
    options += '</div>';

    modal.find('.modal-body').html(options);
    $('#moveToFolder').val(itemId);
    modal.modal('show');

}

function editCollectHtml(obj, activeId) {
    let btnName = '';
    let btnClass = 'btn btn-sm w-75 ';
    if (obj.folderId == activeId) {
        btnClass += 'btn-secondary';
        btnName = "已收藏";
    } else {
        btnClass += 'btn-outline-primary';
        btnName = "收藏";
    }

    return '<div class="row">' +
        ' <div class="col-md-4">' +
        '  <b>' + obj.title + '</b>' +
        '  <br/><span class="text-secondary text-sm-left"><span name="collectedCnt">' + obj.cnt + '</span>&nbsp;条内容</span>' +
        ' </div>' +
        ' <div class="col-md-3 ml-auto text-right">' +
        '  <button type="button" name="collectArticle" class="' + btnClass + '" value="' + obj.folderId + '">'
        + btnName +
        '  </button>' +
        ' </div>' +
        '</div>' +
        '<hr/>';
}

function updateCollectModal(data, itemId) {
    $('#createFolderModal').modal('hide');
    $('#collectModal').modal('show');
    showCollectModal();
}

function addCommentData(data, item) {
    let articleId = $(item).attr('value');
    let parentId = $(item).attr('parentid');
    let diplayItem = $("#comment" + articleId + "DivDisplay");

    let commentCountHeader = diplayItem.find("span[name='commentCountHeader']")
    let cnt = commentCountHeader.text();
    if ($.isNumeric(cnt)) {
        commentCountHeader.text(parseInt(cnt) + 1);
    } else {
        commentCountHeader.html('1');
        commentCountHeader.append('条评论');
    }

    if (parentId) {
        let parentItem = diplayItem.find("div[name='line" + parentId + "Div']");
        let options = '<hr/>' + editLineHtml(data, articleId);

        if (parentItem.next().hasClass("pad-left-40")) {
            parentItem.next().append(options);
        } else {
            options = '<div class="pad-left-40">' + options + '</div>';
            parentItem.after(options);
        }

        parentItem.find("a[name='commentReply']:first").click();
    } else {
        let toItem = diplayItem.find("div[name='comment" + articleId + "Body']");
        toItem.next().find("input[name='commentInput']").val('');

        if ($.isNumeric(cnt)) {
            let options = '<hr/>' + editLineHtml(data, articleId);
            toItem.append(options);
        } else {
            toItem.prev().removeClass('border-bottom-0')
            toItem.removeClass('d-none');
            let options = editLineHtml(data, articleId);
            toItem.append(options);
        }
    }
}

function popoverDispose(btnItem) {
    btnItem.popover('dispose');
    btnItem.find("svg:first").attr('fill', 'currentColor');
    btnItem.removeAttr('aria-describedby');
    btnItem.removeAttr("disabled");
}