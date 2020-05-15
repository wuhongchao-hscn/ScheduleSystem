package com.hitachi.schedule.controller.exception;

import com.hitachi.schedule.controller.actionform.GCAXS010Form;
import com.hitachi.schedule.controller.common.GCConstGlobals;
import com.hitachi.schedule.controller.common.SessionUtil;
import com.hitachi.schedule.controller.component.MessageReadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    MessageReadUtil messageUtil;

    @RequestMapping("/error")
    public String handleError(Model model,
                              HttpServletRequest request) {
        log.info("システムエラーが発生しました。");
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        GCAXS010Form outForm = new GCAXS010Form();
        outForm.setErrLevel("システムエラー");
        if (null != status) {
            String statusCode = status.toString();
//         404 HttpStatus.NOT_FOUND.value()
//         500 HttpStatus.INTERNAL_SERVER_ERROR.value()
            outForm.setErrScreenId("エラーコード：" + statusCode);
            outForm.setErrScreenName("エラー内容：" + messageUtil.getMessage(statusCode));
            outForm.setErrMsg(messageUtil.getMessage(GCConstGlobals.GCXA_SYSTEM_ERROR));
        }
        outForm.setErrorTimeDate(LocalDate.now().toString());
        outForm.setContMsg(messageUtil.getMessage(GCConstGlobals.GCXA_SYSTEM_CONMSG));
        model.addAttribute("form", outForm);
        SessionUtil.clearSessionValue(request);
        return "GCXAS010";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
