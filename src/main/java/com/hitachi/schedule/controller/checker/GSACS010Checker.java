package com.hitachi.schedule.controller.checker;

import com.hitachi.schedule.controller.actionform.GSACS010Form;
import com.hitachi.schedule.controller.common.StringUtil;
import com.hitachi.schedule.controller.component.CommonUtil;
import com.hitachi.schedule.controller.exception.ErrorInfoGM;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class GSACS010Checker extends CommonChecker {
    @Autowired
    private CommonUtil commonUtil;

    public boolean gmCheck(
            Model model,
            GSACS010Form form) {
        log.info("業務チェックを行います。");
        try {

            String strTabFlag = form.getStrTabFlag();
            if (!"tab2".equals(strTabFlag)) {
                do_check_tab1(form);
            } else {
                do_check_tab2(form);
            }
        } catch (ErrorInfoGM e) {
            form.setErrItemId(e.getErrScreenId());
            commonUtil.doRtnPre(model, form, e.getErrMsg(), e.isMsgGetFlg());
            return true;
        }
        log.info("項目チェックには間違いがない。");
        return false;
    }

    private void do_check_tab1(GSACS010Form form) {
        String strUserId = form.getStrUserId();
        if (!StringUtil.eiSuJiCheck(strUserId)) {
            String item_id = "strUserId";
            String errMsg = messageUtil.getMessage("GCXAM065E", "利用者ID", "半角英数字");
            throw new ErrorInfoGM(item_id, errMsg, false);
        }
    }

    private void do_check_tab2(GSACS010Form form) {
        String strShkinId = form.getStrShkinId();
        String strShkinSmi = form.getStrShkinSmi();
        if (!StringUtils.isEmpty(strShkinId) && !StringUtil.eiSuJiCheck(strShkinId)) {
            String item_id = "strShkinId";
            String errMsg = messageUtil.getMessage("GCXAM065E", "職員ID", "半角英数字");
            throw new ErrorInfoGM(item_id, errMsg, false);
        }

        if (!StringUtils.isEmpty(strShkinSmi) && !StringUtil.fullCharCheck(strShkinSmi)) {
            String item_id = "strShkinSmi";
            String errMsg = messageUtil.getMessage("GCXAM026E", "氏名");
            throw new ErrorInfoGM(item_id, errMsg, false);
        }

        if (StringUtils.isEmpty(strShkinId) && StringUtils.isEmpty(strShkinSmi)) {
            List<String> item_id = Arrays.asList("strShkinId", "strShkinSmi");
            String errMsg = messageUtil.getMessage("GCXAM065E", "職員IDまたは氏名", "値");
            throw new ErrorInfoGM(commonUtil.getStringFromList(item_id), errMsg, false);
        }


    }
}
