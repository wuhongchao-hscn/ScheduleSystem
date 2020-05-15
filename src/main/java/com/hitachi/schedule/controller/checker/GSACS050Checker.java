package com.hitachi.schedule.controller.checker;

import com.hitachi.schedule.controller.actionform.GSACS050Form;
import com.hitachi.schedule.controller.component.CommonUtil;
import com.hitachi.schedule.controller.exception.ErrorInfoGM;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class GSACS050Checker extends CommonChecker {
    @Autowired
    private CommonUtil commonUtil;

    public boolean gmCheck(
            Map<String, Object> result,
            GSACS050Form form) {
        log.info("業務チェックを行います。");
        try {
            do_check_imageFile(form);

            do_check_password_repassword(form);

            do_check_roles(form);
        } catch (ErrorInfoGM e) {
            commonUtil.doRtnPre(result, e.getErrScreenId(), e.getErrMsg(), e.isMsgGetFlg());
            return true;
        }
        log.info("項目チェックには間違いがない。");
        return false;
    }

    private void do_check_imageFile(GSACS050Form form) {
        MultipartFile imageFile = form.getImageFile();
        String image_name = imageFile.getOriginalFilename();

        if (StringUtils.isEmpty(image_name)) {
            throw new ErrorInfoGM("imageFile", "GSACM028E", true);
        }

        image_name = image_name.toLowerCase();
        if (!image_name.endsWith("jpg") && !image_name.endsWith("png")) {
            throw new ErrorInfoGM("imageFile", "GSACM029E", true);
        }

        if (commonUtil.checkFileSize(imageFile.getSize(), 200, "K")) {
            throw new ErrorInfoGM("imageFile", "GSACM030E", true);
        }
    }

    private void do_check_password_repassword(GSACS050Form form) {
        String strUserPassword = form.getStrUserPassword();
        String strUserPasswordReInput = form.getStrUserPasswordReInput();

        if (!strUserPassword.equals(strUserPasswordReInput)) {
            List<String> item_id = Arrays.asList("strUserPassword", "strUserPasswordReInput");
            throw new ErrorInfoGM(commonUtil.getStringFromList(item_id), "GSACM010E", true);
        }
    }

    private void do_check_roles(GSACS050Form form) {
        List<String> rlIdList = form.getRlIdList();

        if (rlIdList.isEmpty()) {
            throw new ErrorInfoGM("rlIdList", "GSACM011E", true);
        }
    }
}
