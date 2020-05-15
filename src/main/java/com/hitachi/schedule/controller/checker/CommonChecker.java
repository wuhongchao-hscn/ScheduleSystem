package com.hitachi.schedule.controller.checker;

import com.hitachi.schedule.controller.actionform.BaseForm;
import com.hitachi.schedule.controller.component.CommonUtil;
import com.hitachi.schedule.controller.component.MessageReadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CommonChecker {
    @Autowired
    MessageReadUtil messageUtil;
    @Autowired
    private CommonUtil commonUtil;

    public boolean formCheck(
            Model model,
            BaseForm form,
            BindingResult br) {
        log.info("フォームチェックを行います。");
        if (!br.hasErrors()) {
            log.info("項目チェックには間違いがない。");
            return false;
        }

        List<FieldError> fieldErrors = br.getFieldErrors();
        List<String> errMsg = new ArrayList<>();
        List<String> errField = new ArrayList<>();
        for (FieldError fe : fieldErrors) {
            errField.add(fe.getField());
            errMsg.add(fe.getDefaultMessage());
        }
        form.setErrItemId(commonUtil.getStringFromList(errField));

        commonUtil.doRtnPre(model, form, errMsg);

        return true;
    }

    public boolean formCheck(
            Map<String, Object> result,
            BaseForm form,
            BindingResult br) {
        log.info("フォームチェックを行います。");
        if (!br.hasErrors()) {
            log.info("項目チェックには間違いがない。");
            return false;
        }

        List<FieldError> fieldErrors = br.getFieldErrors();
        List<String> errMsg = new ArrayList<>();
        List<String> errField = new ArrayList<>();
        for (FieldError fe : fieldErrors) {
            errField.add(fe.getField());
            errMsg.add(fe.getDefaultMessage());
        }

        commonUtil.doRtnPre(result, errField, errMsg);

        return true;
    }
}
