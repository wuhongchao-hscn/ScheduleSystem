$(document).ready(function () {
    $("a[name='articleUp']").eq(0).click();

    $("a[name='articleAddHead']").click(function () {
        let replyDiv = $('div[name="articleAnswersReply"]');
        if (!replyDiv.hasClass('d-none')) {
            return false;
        }

        replyDiv.removeClass('d-none');
        $('div[name="articleAnswersBody"]').attr('style', 'height: calc(100vh - 660px);');

        let editor = new window.wangEditor(
            'div[name="articleAnswersReplyHead"]',
            'div[name="articleAnswersReplyBody"]'
        );

        // 自定义菜单配置
        editor.customConfig.menus = [
            'bold', 'italic', 'head', 'quote', 'code', 'list', 'link', 'image'
            // 粗体,  斜体,      标题,    引用,    插入代码, 列表,    插入链接, 插入图片
            // 'fontSize', 'fontName', 'underline', 'strikeThrough', 'foreColor', 'backColor',
            // // 字号,      字体,        下划线,       删除线,           文字颜色,      背景颜色
            // 'justify', 'emoticon', 'table', 'undo', 'redo', 'video',
            // 对齐方式,     表情,        表格,    撤销,    重复,    插入视频,
        ];

        // 自定义菜单文字
        editor.customConfig.lang = {
            '设置标题': 'タイトル設定',
            '正文': '正文',
            '设置列表': 'リスト設定',
            '有序列表': 'ソートあり',
            '无序列表': 'ソートなし',
            '插入代码': 'コード挿入',
            '插入视频': 'ビデオ挿入',
            '插入': '挿入',
            '链接文字': 'リンク文字',
            '链接': 'リンク',
            '网络图片': 'インタネットの図',
            '上传图片': '図アップロード',
            '上传': 'アップロード',
            '创建': '作成'
        }

        // 自定义上传参数
        editor.customConfig.uploadImgParams = {
            // CrsfFilter根据parameter取token
            _csrf: $("input[name='_csrf']").attr("value")
        }
        // 上传图片到服务器
        editor.customConfig.uploadImgServer = '/commonUploadImg';
        // 隐藏“网络图片”tab
        editor.customConfig.showLinkImg = false;
        // 开启debug模式
        editor.customConfig.debug = true;
        // editor.selection.
        // editor创建完成
        editor.create();

        $('a[name="articleAnswersReplyBtn"]').click(function () {
            let inputHtml = editor.txt.html();
            if ('<p><br></p>' == inputHtml) {
                return false;
            }

            let titleId = $('div[name="articleAnswersHead"]').attr('value');
            let url = "/GSABSTitle/" + titleId;
            let data = {inputHtml: inputHtml};
            let fun = 'answerAddSuccess';

            return ajaxPost($(this), url, data, titleId, fun);
        });

        return false;
    });

})
;

function answerAddSuccess(data, titleId) {
    let articleId = data.success;
    if (articleId) {
        window.location.href = articleId;
        return;
    }

    addJsMsg(data.error);
}