package com.hitachi.schedule.controller.exception;

import com.hitachi.schedule.controller.actionform.GCAXS010Form;
import com.hitachi.schedule.controller.common.GCConstGlobals;
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

    @ExceptionHandler({ErrorInfoGM.class, ErrorDownload.class, ErrorTimeOut.class})
    public String handlerErrorInfoGM(HttpServletRequest request,
                                     Model model,
                                     RuntimeException e) {
        GCAXS010Form outForm = new GCAXS010Form();
        String errLevel = null;
        String errMsg = null;
        String errScreenId = null;
        String errScreenName = null;
        if (e instanceof ErrorInfoGM) {
            errLevel = "業務エラー";
            ErrorInfoGM eigm = (ErrorInfoGM) e;
            errScreenId = eigm.getErrScreenId();
            outForm.setErrScreenIdLabel("発生画面ID");
            outForm.setErrScreenNameLabel("発生画面名");
            errScreenName = GamenInfoConfig.getScreenNameById(errScreenId);
            errMsg = eigm.getErrMsg();
        } else if (e instanceof ErrorDownload) {
            errLevel = "ダウンロードエラー";
            errMsg = messageUtil.getMessage(GCConstGlobals.GSXA_DOWNLOAD_ERROR);
            Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
            errScreenId = "506";
            errScreenName = messageUtil.getMessage(errScreenId);
        } else if (e instanceof ErrorTimeOut) {
            errLevel = "タイムアウトエラー";
            errMsg = messageUtil.getMessage(GCConstGlobals.GCXA_TIMEOUT_ERROR);
            errScreenId = "504";
            errScreenName = messageUtil.getMessage(errScreenId);
        }

        log.info("***{}が発生しました。URL:{} ERROR:{}****",
                errLevel, request.getRequestURL(), errMsg);

        outForm.setErrLevel(errLevel);
        outForm.setErrMsg(errMsg);
        outForm.setErrScreenId(errScreenId);
        outForm.setErrScreenName(errScreenName);
        outForm.setErrorTimeDate(LocalDate.now().toString());
        outForm.setContMsg(messageUtil.getMessage(GCConstGlobals.GCXA_CONMSG));
        model.addAttribute("form", outForm);
        return "GCXAS010";
    }

}