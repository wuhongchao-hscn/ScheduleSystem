package com.hitachi.schedule.controller.checker;

import com.hitachi.schedule.config.component.CommonUtil;
import com.hitachi.schedule.config.exception.ErrorInfoGM;
import com.hitachi.schedule.controller.actionform.GSACS020Form;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.List;

@Slf4j
@Component
public class GSACS020Checker extends CommonChecker {
    @Autowired
    private CommonUtil commonUtil;

    public boolean gmCheck(
            Model model,
            GSACS020Form form,
            List<String> deleteUserIdList,
            String loginUserId) {
        log.info("業務チェックを行います。");
        try {
            do_check_deleteUser(deleteUserIdList);

            do_check_contains_loginUser(deleteUserIdList, loginUserId);
        } catch (ErrorInfoGM e) {
            form.setErrItemId(e.getErrScreenId());
            commonUtil.doRtnPre(model, form, e.getErrMsg(), e.isMsgGetFlg());
            return true;
        }
        log.info("項目チェックには間違いがない。");
        return false;
    }

    private void do_check_deleteUser(List<String> deleteUserIdList) {
        if (deleteUserIdList.isEmpty()) {
            String item_id = "deleteUserId";
            throw new ErrorInfoGM(item_id, "GSACM024E", true);
        }
    }

    private void do_check_contains_loginUser(List<String> deleteUserIdList, String loginUserId) {
        if (deleteUserIdList.contains(loginUserId)) {
            String item_id = "deleteFlg".concat(String.valueOf(deleteUserIdList.indexOf(loginUserId) + 1));
            throw new ErrorInfoGM(item_id, "GSACM020E", true);
        }
    }
}
