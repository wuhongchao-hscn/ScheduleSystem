const gifArr = ['欢呼', '干杯', '熬夜', '超得意', '吃瓜', '吃惊', '哈哈', '爱心', '摊手',
    '机智', '害羞', '棒', '安慰', '超开心', '不抬杠', '赞同', '冷静一下',
    '疑惑', '思考', '呼叫管家', '小建议', '是不是', '哼'];

let parentItem = $("#articleScrollDiv");

$(document).ready(function () {

    parentItem.scroll(function () {
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
    // 阅读全文
    parentItem.delegate(".articleUp", "click", function () {
        let articleInfo = getArticleInfo(this);
        let articleObj = getArticleInfoObj(articleInfo);

        let url;
        if (articleObj.articleId) {
            // 文章
            url = "/GSABSArticle/" + articleObj.articleId;
        } else {
            // 标题
            url = "/GSABSTitle/" + articleObj.titleId;
        }
        let fun = 'listArticleData';

        return ajaxGet(this, url, articleInfo, fun);
    });

    // 收起
    parentItem.delegate(".articlePackUp", "click", function () {
        let articleInfo = getArticleInfo(this);

        let articleAbbr = articleInfo.find(".articleAbbr");
        articleAbbr.show();
        articleInfo.find(".articleAll").remove();
        // 让滚动条回到打开之前的状态
        parentItem.delegate("#articleScrollDiv").scrollTop(articleInfo.offset().top);
        $(this).addClass('d-none');
        return false;
    });

    // 文章赞同
    parentItem.delegate(".articleAgree,.articleDisAgree", "click", function () {
        let articleInfo = getArticleInfo(this);
        let articleObj = getArticleInfoObj(articleInfo);

        let articleId = articleObj.articleId;
        let agreeParam = getAgreeParam(articleInfo, $(this).hasClass("articleAgree"));

        let url = "/GSABSArticleAgree/" + articleId + '?agreeParam=' + agreeParam;
        let agreeSpan = articleInfo.find(".articleAgree span");
        let fun = "agreeUpdate";

        return ajaxGet(this, url, agreeSpan, fun);

    });

    // 文章评论展开
    parentItem.delegate(".articleCountUp", "click", function () {
        let articleInfo = getArticleInfo(this);
        let articleObj = getArticleInfoObj(articleInfo);

        let url = "/GSABSComments/" + articleObj.articleId;
        let fun = 'listCommentData';

        $(this).hide();
        $(this).next().removeClass('d-none');

        return ajaxGet($(this), url, articleInfo, fun);
    });

    // 文章评论收起
    parentItem.delegate(".articleCountList", "click", function () {
        let articleInfo = getArticleInfo(this);

        $(this).addClass('d-none');
        $(this).prev().show();

        let commentDiv = articleInfo.find(".commentDiv");
        commentDiv.html('');
        commentDiv.addClass("d-none");
    });

    // // 文章分享 第一种方式，通过js实现
    // parentItem.delegate("a[name='shareLinkCopy']", "click", function () {
    //     let value = $(this).attr('value');
    //     let hrefUrl = $(this).attr('href')
    //     let copyValue = value + "\r\n" + hrefUrl;
    //
    //
    //     let oInput = document.createElement('textarea');
    //     oInput.value = copyValue;
    //     document.body.appendChild(oInput);
    //     oInput.select();
    //     document.execCommand("Copy");
    //     $(oInput).remove();
    //
    //     $('#GSAXS040').after('<div class="copyMsg"><b>链接复制成功</b></div>');
    //
    //     setTimeout(removeCopyMsg, 500);
    //     return false;
    // });

    // 文章分享 第二种方式，通过clipboard.js实现
    let clipboard = new ClipboardJS(".shareLinkCopy", {
        text: function (trigger) {
            let articleObj = getArticleInfoObj(trigger);

            let text = articleObj.articleTitle + ' - Ｂ乎 - 博客';
            let protocol = window.location.protocol;
            let host = window.location.host;

            let hrefUrl = protocol + '//' + host + '/GSABSTitles/' + articleObj.titleId + '/' + articleObj.articleId;
            return text + "\r\n" + hrefUrl;
        }
    });

    clipboard.on('success', function (e) {
        e.clearSelection();
        addJsMsg('链接复制成功');
    });

    // 文章收藏
    parentItem.delegate(".articleCollect", "click", function () {
        let articleInfo = getArticleInfo(this);
        let articleObj = getArticleInfoObj(articleInfo);

        let collectModal = $('.collectModal');
        articleInfo.append(collectModal);
        articleInfo.append($('.createFolderModal'));
        collectModal.modal('show');
        return false;
    });

    // 文章喜欢、取消喜欢
    parentItem.delegate(".articleLike", "click", function () {
        let articleInfo = getArticleInfo(this);
        let articleObj = getArticleInfoObj(articleInfo);

        let url = "/GSABSLikes/" + articleObj.articleId;
        let fun = 'updateLikesArea';

        return ajaxGet($(this), url, this, fun);
    });

    ////////////////////// 收藏夹一览页面 //////////////////////
    // 收藏夹一览页面表示事件
    $('.collectModal').on('show.bs.modal', function (event) {
        showCollectModal(this);
    });

    // 收藏夹一览页面关闭事件
    $('.createFolderModal').on('hide.bs.modal', function (event) {
        $('.collectTitle').val('');
        $('.collectContent').val('');
        $('.levelPublic').click();
        $('.createFolderBtn').attr('disabled', 'disabled');
    });

    // 收藏夹一览页面关闭按钮
    parentItem.delegate(".modalCloseButton", "click", function () {
        $('.collectModal').modal('hide');
        $('.createFolderModal').modal('hide');
    });

    // 收藏夹一览页面创建收藏夹按钮
    parentItem.delegate(".moveToFolder", "click", function () {
        $('.collectModal').modal('hide');
        $('.createFolderModal').modal('show');
    });

    // 收藏、取消收藏
    parentItem.delegate(".collectArticle", "click", function () {
        let articleInfo = getArticleInfo(this);
        let articleObj = getArticleInfoObj(articleInfo);
        let articleId = articleObj.articleId;
        let folderId = $(this).val();

        let url = "/GSABSCollect/" + articleId + '/' + folderId;
        let fun = 'updateCollectModal';

        ajaxGet($(this), url, this, fun);
    });

    ////////////////////// 收藏夹创建页面 //////////////////////
    // 收藏夹标题事件
    parentItem.delegate(".collectTitle", "change", function () {
        if ($(this).val()) {
            $('.createFolderBtn').removeAttr('disabled');
        } else {
            $('.createFolderBtn').attr('disabled', "");
        }
    });

    // 收藏夹创建页面返回按钮
    parentItem.delegate(".backToCollect", "click", function () {
        $('.createFolderModal').modal('hide');
        $('.collectModal').modal('show');
    });

    // 收藏夹创建页面确认创建按钮
    parentItem.delegate(".createFolderBtn", "click", function () {
        let title = $('.collectTitle').val();
        let content = $('.collectContent').val();
        let level = $('input:radio[name="collectLevel"]:checked').val();

        let url = "/GSABSFolder?title=" + title + "&content=" + content + "&level=" + level;
        url = encodeURI(url);// 进行utf-8编码
        let fun = 'updateCollectModal';

        return ajaxGet($(this), url, null, fun);
    });

    ////////////////////// 评论一览区域 //////////////////////
    // 按时间排序/默认排序
    parentItem.delegate(".commentSort", "click", function () {
        let articleInfo = getArticleInfo(this);
        let articleObj = getArticleInfoObj(articleInfo);

        let url = "/GSABSComments/" + articleObj.articleId;
        if ($(this).hasClass("commentTimeSort")) {
            // 按时间排序
            url += '?sortParam=0';
        }
        $(this).toggleClass("commentTimeSort");
        let fun = 'listCommentData';

        return ajaxGet(this, url, articleInfo, fun);
    });

    // 分页跳转
    parentItem.delegate(".pageLinkDefault", "click", function () {
        let articleInfo = getArticleInfo(this);
        let articleObj = getArticleInfoObj(articleInfo);

        let articleId = articleObj.articleId;
        let pageNow = $(this).attr('page');
        let url = "/GSABSComments/" + articleId + '?pageNow=' + pageNow;
        if ($(this).hasClass("pageLinkTime")) {
            url += '&sortParam=0';
        }

        let fun = 'listCommentData';

        return ajaxGet($(this), url, articleInfo, fun);
    });

    // 评论发布按钮活性控制
    parentItem.delegate(".commentInput", "change", function () {
        let btn = $(this).next().find(".commentAdd");
        if ($(this).val()) {
            btn.removeAttr('disabled');
        } else {
            btn.attr('disabled', "");
        }
    });

    // 评论回复
    parentItem.delegate(".commentReply", "click", function () {
        let spantext = $(this).find("span");
        if (!spantext.hasClass("replying")) {
            let lineInfo = getLineInfo(this);
            let userName = lineInfo.find(".userName").text();

            let placeholder = "回复 " + userName;
            $(this).parent().after(commentAddItem(placeholder));
            spantext.text('取消回复');
            spantext.addClass('replying');
        } else {
            $(this).parent().next().remove();
            spantext.text('回复');
            spantext.removeClass('replying');
        }

        return false;
    });

    // 评论发布
    parentItem.delegate(".commentAdd", "click", function () {
        let articleInfo = getArticleInfo(this);
        let articleObj = getArticleInfoObj(articleInfo);
        let articleId = articleObj.articleId;

        let lineInfo = getLineInfo(this);
        let parentId = getLineInfoParentId(lineInfo);

        let content = $(this).parent().parent().find(".commentInput").val();

        let imgId = $(this).prev().attr('aria-describedby')
        if (imgId) {
            let imgDiv = $('#' + imgId);
            let img = imgDiv.find('img');
            if (1 == img.length) {
                img.attr('width', '50px');
                img.attr('height', '50px');
                let imgHtml = img.prop('outerHTML');
                content += imgHtml;
                imgDiv.find('button.popoverCloseButton').click();
            } else {
                $(this).prev().click();
            }
        }

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

function getArticleInfo(item) {
    return $(item).parents(".articleInfo");
}

function getArticleInfoObj(info) {
    return info.get(0).articleObj;
}

function getLineInfo(item) {
    return $(item).parents(".lineInfo");
}

function getLineInfoParentId(info) {
    return info.get(0).parentId;
}

function listArticleData(data, item) {
    let itemAbbr = item.find(".articleAbbr");
    let itemPackUp = item.find(".articlePackUp");

    let content = data.content;
    let options = '';
    if (content) {
        // Article
        options +=
            '<div class="media w-75 articleAll">' +
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

    itemAbbr.after(options);
    itemAbbr.hide();
    itemPackUp.removeClass('d-none');
}

function getAgreeParam(articleInfo, agreeFlg) {
    let agreeItem = articleInfo.find(".articleAgree");
    let disAgreeItem = articleInfo.find(".articleDisAgree");
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

function agreeUpdate(data, agreeSpan) {
    agreeSpan.text(data);
}

function listCommentData(data, articleInfo) {
    let commentDiv = articleInfo.find(".commentDiv");
    commentDiv.html('');

    if (!isCommentExist(data, commentDiv)) {
        let headItem = editHeadHtml(data);
        commentDiv.append(headItem);
        let bodyItem = editBodyHtml(data);
        commentDiv.append(bodyItem);
        let footItem = editFooterHtml(data);
        commentDiv.append(footItem);
    }
    commentDiv.removeClass("d-none")
    commentDiv.focus();
}

function isCommentExist(data, commentDiv) {
    let commentCount = data.commentCount;
    if (commentCount == 0) {
        let headItem = $(
            ' <div class="card-header border-bottom-0 bg-white">' +
            '    <span class="commentHeader"><b>还没有评论</b></span>' +
            ' </div>');
        commentDiv.append(headItem);
        let bodyItem = $(
            ' <div class="commentBody card-body d-none">' +
            ' </div>'
        );
        commentDiv.append(bodyItem);
        let footItem = editFooterHtml(data);
        commentDiv.append(footItem);
        return true;
    }
    return false;
}

function editHeadHtml(data) {
    let commentCount = data.commentCount;
    let levelFlg = data.levelFlg
    let sortName = levelFlg ? "时间" : "默认";

    let options =
        '<div class="card-header bg-white">' +
        ' <div class="row justify-content-between">' +
        '  <div class="col-4">' +
        '   <span class="commentCountHeader">' + commentCount + '</span> 条评论' +
        '  </div>' +
        '  <div class="col-4 text-right">' +
        '   <span class="commentSort' + (levelFlg ? " commentTimeSort" : '') + '">' +
        '    <svg class="bi bi-arrow-left-right" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">' +
        '     <path fill-rule="evenodd" d="M10.146 7.646a.5.5 0 01.708 0l3 3a.5.5 0 010 .708l-3 3a.5.5 0 01-.708-.708L12.793 11l-2.647-2.646a.5.5 0 010-.708z" clip-rule="evenodd"/>' +
        '     <path fill-rule="evenodd" d="M2 11a.5.5 0 01.5-.5H13a.5.5 0 010 1H2.5A.5.5 0 012 11zm3.854-9.354a.5.5 0 010 .708L3.207 5l2.647 2.646a.5.5 0 11-.708.708l-3-3a.5.5 0 010-.708l3-3a.5.5 0 01.708 0z" clip-rule="evenodd"/>' +
        '     <path fill-rule="evenodd" d="M2.5 5a.5.5 0 01.5-.5h10.5a.5.5 0 010 1H3a.5.5 0 01-.5-.5z" clip-rule="evenodd"/>' +
        '    </svg>' +
        '    切换为' + sortName + '排序' +
        '   </span>' +
        '  </div>' +
        ' </div>' +
        '</div>';

    return options;
}

function editBodyHtml(data) {
    let objList = data.levelList;
    let levelFlg = data.levelFlg;

    let options = $('<div class="commentBody card-body"></div>');
    makeCommentHtml(options, objList, levelFlg);

    return options;
}

function makeCommentHtml(options, objList, levelFlg) {
    let level = '';
    for (let i = 0; i < objList.length; i++) {
        let articleObj = objList[i];
        let levelNow = objList[i].level;
        if (levelFlg && level != levelNow) {
            let levelName = 1 == levelNow ? "精选评论" : "评论";
            options.append('<p class="bg-light">' + levelName + '</p>');
            level = levelNow;
        } else if (0 < i) {
            options.append('<hr/>');
        }

        options.append(editLineHtml(articleObj));

        if (0 < objList[i].childs.length) {
            let item = $('<div class="pad-left-40"><hr/></div>');
            makeCommentHtml(item, objList[i].childs, false);
            options.append(item);
        }
    }
    return options;
}

function editLineHtml(articleObj) {
    let options = $(
        '<div class="lineInfo">' +
        ' <div class="row no-gutters">' +
        '  <div class="col-md-8">' +
        '   <img src="/images/carousel3.jpg" width="20px" height="20px"/>' +
        '   <span class="userName">' + articleObj.userName + '</span>' +
        '  </div>' +
        '  <div class="col-md-4 text-right text-secondary">' + articleObj.dateLong + '</div>' +
        ' </div>' +
        ' <div class="row no-gutters">' +
        '  <div class="col-md-8">' +
        '   <pre>' + articleObj.comment + '</pre>' +
        '  </div>' +
        '  <div class="col-md-4 text-right text-secondary">' +
        '   <a class="commentReply text-secondary">' +
        '    <svg fill="currentColor" viewBox="0 0 24 24" width="16" height="16" style="margin-right: 5px;">' +
        '     <path d="M22.959 17.22c-1.686-3.552-5.128-8.062-11.636-8.65-.539-.053-1.376-.436-1.376-1.561V4.678c0-.521-.635-.915-1.116-.521L1.469 10.67a1.506 1.506 0 0 0-.1 2.08s6.99 6.818 7.443 7.114c.453.295 1.136.124 1.135-.501V17a1.525 1.525 0 0 1 1.532-1.466c1.186-.139 7.597-.077 10.33 2.396 0 0 .396.257.536.257.892 0 .614-.967.614-.967z" fill-rule="evenodd">' +
        '     </path>' +
        '    </svg>' +
        '    <span>回复</span>' +
        '   </a>' +
        '  </div>' +
        ' </div>' +
        '</div>'
    );
    options.get(0).parentId = articleObj.id;
    return options;
}

function editFooterHtml(data) {
    let levelFlg = data.levelFlg
    let options = $('<div class="lineInfo card-footer bg-white"></div>');
    if (data.pageCnt) {
        let pageCnt = data.pageCnt;
        let pageNow = data.pageNow

        options.append(getPageBtnItemByPage(levelFlg, 1 == pageNow, pageNow - 1, "<small>上一页</small>"));
        if (pageNow <= 3) {
            for (let index = 1; index < pageNow; index++) {
                options.append(getPageBtnItem(levelFlg, false, index));
            }
        } else {
            options.append(getPageBtnItem(levelFlg, false, 1));
            options.append(getPageBtnItem(levelFlg, true, "..."));
            options.append(getPageBtnItem(levelFlg, false, pageNow - 1));
        }
        options.append(getPageBtnItem(levelFlg, true, pageNow));

        if (pageNow + 2 >= pageCnt) {
            for (let index = pageNow + 1; index <= pageCnt; index++) {
                options.append(getPageBtnItem(levelFlg, false, index));
            }
        } else {
            options.append(getPageBtnItem(levelFlg, false, pageNow + 1));
            options.append(getPageBtnItem(levelFlg, true, "..."));
            options.append(getPageBtnItem(levelFlg, false, pageCnt));
        }
        options.append(getPageBtnItemByPage(levelFlg, pageCnt == pageNow, pageNow + 1, "<small>下一页</small>"));
    } else {
        options.append(commentAddItem("写下你的评论"));
    }

    return options;
}

function commentAddItem(placeholder) {
    let options = $(
        '<div class="input-group">' +
        ' <input type="text" class="commentInput form-control" placeholder="' + placeholder + '">' +
        ' <div class="input-group-append">' +
        '  <button class="input-group-text" type="button" data-toggle="popover" data-placement="bottom">' +
        '   <svg fill="currentColor" viewBox="0 0 24 24" width="24" height="24">' +
        '    <path d="M7.523 13.5h8.954c-.228 2.47-2.145 4-4.477 4-2.332 0-4.25-1.53-4.477-4zM12 21a9 9 0 1 1 0-18 9 9 0 0 1 0 18zm0-1.5a7.5 7.5 0 1 0 0-15 7.5 7.5 0 0 0 0 15zm-3-8a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3zm6 0a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3z">' +
        '    </path>' +
        '   </svg>' +
        '  </button>' +
        '  <button type="button" class="commentAdd btn btn-sm btn-primary" disabled>发布</button>' +
        ' </div>' +
        '</div>'
    );
    return options;
}

function getPageBtnItem(levelFlg, disableFlg, comment) {
    return getPageBtnItemByPage(levelFlg, disableFlg, comment, comment);
}

function getPageBtnItemByPage(levelFlg, disableFlg, page, comment) {
    let options = '<button type="button" class="pageLinkDefault';
    if (!levelFlg) {
        options += ' pageLinkTime';
    }
    options += ' btn btn-link"';

    if (disableFlg) {
        options += ' disabled';
    }

    options += ' page="' + page + '">' + comment + '</button>';

    return options;
}

function updateLikesArea(data, item) {
    if (0 == data) {
        $(item).children("span").text("喜欢");
    } else {
        $(item).children("span").text("取消喜欢");
    }
}

function showCollectModal(item) {
    let articleInfo = getArticleInfo(item);
    let articleObj = getArticleInfoObj(articleInfo);
    let url = "/GSABSCollectList/" + articleObj.articleId;
    let fun = 'listCollectModal';

    ajaxGet(this, url, this, fun);
}

function listCollectModal(data, item) {
    let modal = $('.collectModal');
    let options = '<div class="container-fluid">';
    let activeId = data.activeId;
    let folders = data.folders;


    for (let i = 0; i < folders.length; i++) {
        let articleObj = folders[i];
        options += editCollectHtml(articleObj, activeId)
    }
    options += '</div>';

    modal.find('.modal-body').html(options);
    modal.modal('show');
}

function editCollectHtml(articleObj, activeId) {
    let btnName = '';
    let btnClass = 'btn btn-sm w-75 ';
    if (articleObj.folderId == activeId) {
        btnClass += 'btn-secondary';
        btnName = "已收藏";
    } else {
        btnClass += 'btn-outline-primary';
        btnName = "收藏";
    }

    return '<div class="row">' +
        ' <div class="col-md-4">' +
        '  <b>' + articleObj.title + '</b>' +
        '  <br/><span class="text-secondary text-sm-left"><span name="collectedCnt">' + articleObj.cnt + '</span>&nbsp;条内容</span>' +
        ' </div>' +
        ' <div class="col-md-3 ml-auto text-right">' +
        '  <button type="button" class="collectArticle ' + btnClass + '" value="' + articleObj.folderId + '">'
        + btnName +
        '  </button>' +
        ' </div>' +
        '</div>' +
        '<hr/>';
}

function updateCollectModal(data, item) {
    $('.createFolderModal').modal('hide');
    $('.collectModal').modal('show');
    showCollectModal(item);
}

function addCommentData(data, item) {
    let articleInfo = getArticleInfo(item);
    let diplayItem = articleInfo.find(".commentDiv");

    let commentCountHeader = diplayItem.find(".commentCountHeader");
    let cnt = commentCountHeader.text();
    if ($.isNumeric(cnt)) {
        commentCountHeader.text(parseInt(cnt) + 1);
    } else {
        commentCountHeader.html('1');
        commentCountHeader.after('条评论');
    }

    let lineInfo = getLineInfo(item);
    let parentId = getLineInfoParentId(lineInfo);
    if (parentId) {
        let options = editLineHtml(data);

        if (lineInfo.next().hasClass("pad-left-40")) {
            lineInfo.next().append('<hr/>');
            lineInfo.next().append(options);
        } else {
            let item = $('<div class="pad-left-40"></div>');
            item.append('<hr/>');
            item.append(options)
            lineInfo.after(item);
        }

        lineInfo.find(".commentReply:first").click();
    } else {
        let toItem = diplayItem.find(".commentBody");
        toItem.next().find(".commentInput").val('');

        if ($.isNumeric(cnt)) {
            let options = editLineHtml(data);
            toItem.append('<hr/>');
            toItem.append(options);
        } else {
            toItem.prev().removeClass('border-bottom-0')
            toItem.removeClass('d-none');
            let options = editLineHtml(data);
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