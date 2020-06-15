package com.hitachi.schedule.controller.handler.gsac;

import com.hitachi.schedule.config.common.GXConst;
import com.hitachi.schedule.config.common.SessionUtil;
import com.hitachi.schedule.config.component.CommonUtil;
import com.hitachi.schedule.controller.actionform.GSACS010Form;
import com.hitachi.schedule.controller.checker.GSACS010Checker;
import com.hitachi.schedule.service.param.UserFindParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@Slf4j
public class GSACS010Handler {
    @Autowired
    private GSACS010Checker checker;
    @Autowired
    private CommonUtil commonUtil;

    private static final String RTN_STR_NG = "GSACS010";
    private static final String RTN_STR_OK = "redirect:/GSACS020Display";

    @PostMapping("/GSACS010Search")
    public String GSACS010Search(
            @Valid GSACS010Form form,
            BindingResult br,
            Model model,
            HttpServletRequest request) {
        log.info("検索ボタンを押下しました。");
        commonUtil.doInitGSACS010(form);
        if (checker.gmCheck(model, form)) {
            return RTN_STR_NG;
        }

        String strUserId = form.getStrUserId();
        String strShkinId = form.getStrShkinId();
        String strShkinSmi = form.getStrShkinSmi();
        String strKnskShbtCode = form.getStrKnskShbtCode();
        String strTabFlag = form.getStrTabFlag();

        UserFindParam ufp = new UserFindParam();
        ufp.setStrUserId(strUserId);
        ufp.setStrShkinId(strShkinId);
        ufp.setStrShkinSmi(strShkinSmi);
        ufp.setStrKnskShbtCode(strKnskShbtCode);
        ufp.setStrTabFlag(strTabFlag);
        Integer startNo = 1;
        Integer endNo = startNo + GXConst.GSAA_PROP_GSACT020_DISPLAY_SIZE - 1;
        ufp.setStartNo(startNo);
        ufp.setEndNo(endNo);

        SessionUtil.setGsacSearchParam(request, ufp);

        return RTN_STR_OK;
    }

    @PostMapping("/GSACS010Register")
    public String GSACS010Register(HttpServletRequest request) {
        log.info("新規登録ボタンを押下しました。");
        return "redirect:/GSACS050Display";
    }
}
