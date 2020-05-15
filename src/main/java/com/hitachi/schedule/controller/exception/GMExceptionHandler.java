package com.hitachi.schedule.controller.exception;

import com.hitachi.schedule.controller.actionform.GCAXS010Form;
import com.hitachi.schedule.controller.common.GCConstGlobals;
import com.hitachi.schedule.controller.common.SessionUtil;
import com.hitachi.schedule.controller.component.MessageReadUtil;
import com.hitachi.schedule.controller.configuration.GamenInfoConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

@ControllerAdvice
@Slf4j
public class GMExceptionHandler {
    @Autowired
    MessageReadUtil messageUtil;

    @ExceptionHandler({ErrorInfoGM.class})
    public String handlerErrorInfoGM(HttpServletRequest request,
                                     Model model,
                                     ErrorInfoGM e) {
        log.info("業務エラーが発生しました。");
        log.info("********************Throw Exception.url:{} ERROR:{}********************",
                request.getRequestURL(), e.getMessage(), e);

        GCAXS010Form outForm = new GCAXS010Form();
        outForm.setErrLevel("業務エラー");
        String errScreenId = e.getErrScreenId();
        outForm.setErrScreenId("発生画面ID：" + errScreenId);
        outForm.setErrScreenName("発生画面名：" + GamenInfoConfig.getScreenNameById(errScreenId));
        outForm.setErrMsg(e.getErrMsg());
        outForm.setErrorTimeDate(LocalDate.now().toString());
        outForm.setContMsg(messageUtil.getMessage(GCConstGlobals.GCXA_CONMSG));
        model.addAttribute("form", outForm);
        SessionUtil.clearSessionValue(request);
        return "GCXAS010";
    }

    @ExceptionHandler({ErrorDownload.class})
    public String handlerErrorDownload(HttpServletRequest request,
                                       Model model,
                                       ErrorDownload e) {
        log.info("ファイルダウンロードエラーが発生しました。");
        String errMsg = messageUtil.getMessage(GCConstGlobals.GSXA_DOWNLOAD_ERROR);
        log.info("********************Throw Exception.url:{} ERROR:{}********************",
                request.getRequestURL(), errMsg);

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        GCAXS010Form outForm = new GCAXS010Form();
        outForm.setErrLevel("タイムアウトエラー");
        String statusCode = "506";
        outForm.setErrScreenId("エラーコード：" + statusCode);
        outForm.setErrScreenName("エラー内容：" + messageUtil.getMessage(statusCode));
        outForm.setErrMsg(errMsg);
        outForm.setErrorTimeDate(LocalDate.now().toString());
        outForm.setContMsg(messageUtil.getMessage(GCConstGlobals.GCXA_CONMSG));
        model.addAttribute("form", outForm);
        model.addAttribute("error", "ファイルダウンロードエラー");
        SessionUtil.clearSessionValue(request);
        return "GCXAS010";

    }

    @ExceptionHandler({ErrorTimeOut.class})
    public String handlerErrorTimeOut(HttpServletRequest request,
                                      Model model,
                                      ErrorTimeOut e) {
        log.info("セッションタイムアウトエラーが発生しました。");
        String errMsg = messageUtil.getMessage(GCConstGlobals.GCXA_TIMEOUT_ERROR);
        log.info("********************Throw Exception.url:{} ERROR:{}********************",
                request.getRequestURL(), errMsg);

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        GCAXS010Form outForm = new GCAXS010Form();
        outForm.setErrLevel("タイムアウトエラー");
        String statusCode = "504";
        outForm.setErrScreenId("エラーコード：" + statusCode);
        outForm.setErrScreenName("エラー内容：" + messageUtil.getMessage(statusCode));
        outForm.setErrMsg(errMsg);
        outForm.setErrorTimeDate(LocalDate.now().toString());
        outForm.setContMsg(messageUtil.getMessage(GCConstGlobals.GCXA_CONMSG));
        model.addAttribute("form", outForm);
        model.addAttribute("error", "タイムアウトエラー");
        SessionUtil.clearSessionValue(request);
        return "GCXAS010";

    }
}