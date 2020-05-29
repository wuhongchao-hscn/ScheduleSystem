package com.hitachi.schedule.controller.checker;

import com.hitachi.schedule.config.component.CommonUtil;
import com.hitachi.schedule.config.exception.ErrorInfoGM;
import com.hitachi.schedule.controller.actionform.GSACS040Form;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class GSACS040Checker extends CommonChecker {
    @Autowired
    private CommonUtil commonUtil;

    public boolean gmCheck(
            Model model,
            GSACS040Form form) {
        log.info("業務チェックを行います。");
        try {
            do_check_password_repassword(form);
        } catch (ErrorInfoGM e) {
            form.setErrItemId(e.getErrScreenId());
            commonUtil.doRtnPre(model, form, e.getErrMsg(), e.isMsgGetFlg());
            return true;
        }
        log.info("項目チェックには間違いがない。");
        return false;
    }

    private void do_check_password_repassword(GSACS040Form form) {
        String strUserPassword = form.getStrUserPassword();
        String strUserPasswordReInput = form.getStrUserPasswordReInput();

        if (!strUserPassword.equals(strUserPasswordReInput)) {
            List<String> item_id = Arrays.asList("strUserPassword", "strUserPasswordReInput");
            throw new ErrorInfoGM(commonUtil.getStringFromList(item_id), "GSACM010E", true);
        }
    }
}
