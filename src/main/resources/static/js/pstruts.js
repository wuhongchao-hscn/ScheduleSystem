//## (c) Hitachi, Ltd. 2010. All rights reserved.
//## e-CANDO/Cosminexus PLayerTemplate for Struts
//## TemplateVer.2015/11/26(05-04)
//## pstruts.js

/** 二重送信チェック用イベントID格納Map */
var eventIdMap = {};


/*
 * Submitボタンに代わってButtonでformを送信します。
 * 送信時に任意パラメタを送信するか、しないかで呼び出す
 * メソッドを切り替えます。
 * 送信する場合は、HTML上にタグライブラリから出力される
 * addParameter()メソッドを呼び出します。
 * 
 * @param obj 押下されたボタンのオブジェクト
 * @return void
 */
function submitForm(obj) {
    if (typeof addParameter == 'function') {
        //任意パラメタを送信する場合
        addParameter(obj);
    } else {
        //任意パラメタを送信しない場合
        submitFormAddParameter(obj);
    }
}


/*
 * Submitボタンに代わってButtonでformを送信します。
 * 送信時に、押下されたボタンのイベントの情報をセットします。
 * 第二引数に送信パラメタの配列があれば送信します。
 * 
 * @param obj 押下されたボタンのオブジェクト
 * @param params サーバに送信するパラメタの配列(aaa=XXX,bbb=YYY)
 * @return void
 */
function submitFormAddParameter(obj, params) {

    //イベント情報のエレメントをFormに追加
    var eve = document.createElement("input");
    eve.setAttribute("type", "hidden");
    eve.setAttribute("name", obj.name);
    eve.setAttribute("value", obj.value);
    obj.form.appendChild(eve);

    //送信パラメタのエレメント格納用配列
    var paramElementList;

    //送信パラメタのエレメントをFormに追加
    if (arguments.length == 2) {

        paramElementList = [];

        var cnt = 0;
        for (i = 0; i < params.length; i = i + 2) {

            var key = params[i];
            var value = params[i + 1];

            var element = document.createElement("input");
            element.setAttribute("type", "text");
            element.setAttribute("name", key);
            element.setAttribute("value", value);
            obj.form.appendChild(element);

            paramElementList[cnt] = element;
            cnt++;

        }
    }

    //サーバに送信
    obj.form.submit();

    //イベント情報のエレメントをFormから削除
    obj.form.removeChild(eve);

    //送信パラメタのエレメントをFormから削除
    if (paramElementList != null) {
        for (i = 0; i < paramElementList.length; i++) {
            obj.form.removeChild(paramElementList[i]);
        }
    }

}


/**
 *二重送信チェックを行います。
 *一度目の送信の場合はtureを、二度目の送信の場合はfalseを返します。
 *
 * @param obj 押下されたボタンのオブジェクト
 * @return 二重送信チェックの結果(true/false)
 */

function submitCheck(obj) {
    if (eventIdMap[obj.name] == obj.name) {
        return false;
    } else {
        eventIdMap[obj.name] = obj.name;
        return true;
    }
}


