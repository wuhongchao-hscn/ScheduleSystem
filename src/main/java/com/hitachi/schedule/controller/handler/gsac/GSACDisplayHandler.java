package com.hitachi.schedule.controller.handler.gsac;

import com.hitachi.schedule.config.common.GXConst;
import com.hitachi.schedule.config.common.SessionUtil;
import com.hitachi.schedule.config.component.CommonUtil;
import com.hitachi.schedule.config.component.MessageReadUtil;
import com.hitachi.schedule.controller.actionform.*;
import com.hitachi.schedule.controller.param.UserDetialInfoList;
import com.hitachi.schedule.service.GSACScheduleF;
import com.hitachi.schedule.service.param.UserFindParam;
import com.hitachi.schedule.service.param.UserFindResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;


@Controller
@Slf4j
public class GSACDisplayHandler {
    @Autowired
    private MessageReadUtil messageUtil;
    @Autowired
    private GSACScheduleF gsacService;
    @Autowired
    private CommonUtil commonUtil;

    @GetMapping("/GSACS010Display")
    public String GSACS010Display(Model model, HttpServletRequest request) {
        GSACS010Form outForm = new GSACS010Form();
        commonUtil.doInitGSACS010(outForm);

        Object obj = SessionUtil.getSessionValue(
                request,
                GXConst.GSAA_PROP_GSACT010_KNSK_JUKN
        );
        if (null != obj) {
            UserFindParam ufp = (UserFindParam) obj;
            if (!"tab2".equals(ufp.getStrTabFlag())) {
                outForm.setStrUserId(ufp.getStrUserId());
            } else {
                outForm.setStrTabFlag(ufp.getStrTabFlag());
                outForm.setStrShkinId(ufp.getStrShkinId());
                outForm.setStrShkinSmi(ufp.getStrShkinSmi());
                outForm.setStrKnskShbtCode(ufp.getStrKnskShbtCode());
            }
        }

        model.addAttribute("form", outForm);

        log.info("利用者検索画面を表示します。");
        return "GSACS010";
    }

    @GetMapping("/GSACS020Display")
    public String GSACS020Display(Model model, HttpServletRequest request) {
        GSACS020Form outForm = new GSACS020Form();

        UserFindParam ufp = SessionUtil.getGsacSearchParam(request);

        UserFindResult ufr = gsacService.findUserList(ufp);

        String strTabFlag = ufp.getStrTabFlag();

        outForm.setErrItemId(ufp.getErrItemId());
        ufp.setErrItemId("");
        outForm.setDeleteUserId(ufp.getDeleteUserIdForm());
        ufp.setDeleteUserId("");

        if (!"tab2".equals(strTabFlag)) {
            outForm.setStrUserId(ufp.getStrUserId());
        } else {
            outForm.setStrShkinId(ufp.getStrShkinId());
            String strShkinSmi = ufp.getStrShkinSmi();
            if (!StringUtils.isEmpty(strShkinSmi)) {
                outForm.setStrShkinSmi(strShkinSmi);
                outForm.setStrKnskShbtCode(gsacService.getCodeName(ufp.getStrKnskShbtCode()));
            }
        }

        outForm.setPageList(ufr.getPageList());
        outForm.setPageNow(ufr.getPageNow());
        outForm.setHitRangeLabel(ufr.getHitRangeLabel());
        outForm.setUserList(ufr.getUserList());

        ufp.setEndNo(ufr.getEndNo());
        SessionUtil.setGsacSearchParam(request, ufp);
        SessionUtil.setGsacSearchResult(request, ufr);

        model.addAttribute("form", outForm);

        log.info("利用者検索結果一覧画面を表示します。");
        return "GSACS020";
    }

    @GetMapping("/GSACS030Display")
    public String GSACS030Display(Model model, HttpServletRequest request) {
        GSACS030Form outForm = new GSACS030Form();
        List<String> userIdList = null;
        String userId = SessionUtil.getSessionValueString(
                request,
                GXConst.GSAA_PROP_GSACT020_USER_ID
        );

        if (!StringUtils.isEmpty(userId)) {
            userIdList = Arrays.asList(userId);
        } else {
            outForm.setUpdateFlg(false);
            model.addAttribute("warning", messageUtil.getMessage("GSACM006I"));

            UserFindParam ufp = SessionUtil.getGsacSearchParam(request);

            String deleteUserId = ufp.getDeleteUserId();
            userIdList = commonUtil.getListFromString(deleteUserId);
        }

        List<UserDetialInfoList> udil = gsacService.findUserDetialList(userIdList);
        outForm.setUserDetialList(udil);
        SessionUtil.setGsacUserDetialList(request, udil);


        model.addAttribute("form", outForm);

        log.info("利用者詳細画面を表示します。");
        return "GSACS030";
    }

    @GetMapping("/GSACS040Display")
    public String GSACS040Display(Model model, HttpServletRequest request) {
        if (!model.containsAttribute("form")) {
            GSACS040Form outForm = new GSACS040Form();
            commonUtil.doInitGSACS040(outForm);
            String userId = SessionUtil.getSessionValueString(
                    request,
                    GXConst.GSAA_PROP_GSACT020_USER_ID
            );

            List<String> userIdList = Arrays.asList(userId);
            List<UserDetialInfoList> udil = gsacService.findUserDetialList(userIdList);
            UserDetialInfoList obj = udil.get(0);
            outForm.setStrUserId(obj.getUserId());
            outForm.setStrShkinId(obj.getStrShkinId());
            outForm.setStrShkinSmi(obj.getStrShkinSmi());
            outForm.setRlIdList(commonUtil.getListFromString(obj.getStrRlId()));
            outForm.setStrImageRadio("5");

            SessionUtil.saveSessionValue(
                    request,
                    GXConst.GSAA_PROP_GSACT040_EX_KEY,
                    obj.getUser_ex_key()
            );

            model.addAttribute("form", outForm);
        }

        log.info("利用者更新画面を表示します。");
        return "GSACS040";
    }

    @GetMapping("/GSACS050Display")
    public String GSACS050Display(Model model, HttpServletRequest request) {

        GSACS050Form outForm = new GSACS050Form();
        commonUtil.doInitGSACS050(outForm);

        model.addAttribute("form", outForm);

        log.info("利用者登録画面を表示します。");
        return "GSACS050";
    }
}
