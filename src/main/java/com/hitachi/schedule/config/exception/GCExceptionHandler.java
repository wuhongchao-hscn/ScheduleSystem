package com.hitachi.schedule.config.exception;

import com.hitachi.schedule.config.common.GXConst;
import com.hitachi.schedule.config.component.MessageReadUtil;
import com.hitachi.schedule.controller.actionform.GCAXS010Form;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

@Controller
@Slf4j
public class GCExceptionHandler implements ErrorController {
    @Autowired
    private MessageReadUtil messageUtil;
    @Value("${server.error.path)")
    private static String errorPath;

    @RequestMapping("/error")
    public String handleError(Model model,
                              HttpServletRequest request) {
        String errMsg = messageUtil.getMessage(GXConst.GCXA_SYSTEM_ERROR);
        String errLevel = "システムエラー";
        log.info("***{}が発生しました。URL:{} ERROR:{}****",
                errLevel, request.getRequestURL(), errMsg);
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        GCAXS010Form outForm = new GCAXS010Form();
        outForm.setErrLevel(errLevel);
        if (null != status) {
            String statusCode = status.toString();
            outForm.setErrScreenId(statusCode);
            outForm.setErrScreenName(messageUtil.getMessage(statusCode));
            outForm.setErrMsg(errMsg);
        }
        outForm.setErrorTimeDate(LocalDate.now().toString());
        outForm.setContMsg(messageUtil.getMessage(GXConst.GCXA_CONMSG));
        model.addAttribute("form", outForm);
        return "GCXAS010";
    }

    @Override
    public String getErrorPath() {
        return errorPath;
    }
}
