package com.hitachi.schedule.controller.handler.gsac;

import com.hitachi.schedule.config.common.GXConst;
import com.hitachi.schedule.config.common.SessionUtil;
import com.hitachi.schedule.config.component.CommonUtil;
import com.hitachi.schedule.config.component.MessageReadUtil;
import com.hitachi.schedule.controller.actionform.GSACS030Form;
import com.hitachi.schedule.service.GSACScheduleF;
import com.hitachi.schedule.service.param.UserDeleteParam;
import com.hitachi.schedule.service.param.UserFindParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Controller
@Slf4j
public class GSACS030Handler {
    @Autowired
    private MessageReadUtil messageUtil;
    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private GSACScheduleF gsacService;

    private static final String RTN_STR_OK = "redirect:/GSACS020Display";


    @PostMapping("/GSACS030Update")
    public String GSACS030Update() {
        log.info("更新ボタンを押下しました。");
        return "redirect:/GSACS040Display";
    }

    @PostMapping("/GSACS030Delete")
    public String GSACS030Delete(
            GSACS030Form form,
            RedirectAttributes redirectModel,
            HttpServletRequest request) {
        log.info("削除実行ボタンを押下しました。");

        UserFindParam ufp = SessionUtil.getGsacSearchParam(request);

        String deleteUserId = ufp.getDeleteUserId();
        List<String> userIdList = commonUtil.getListFromString(deleteUserId);

        String loginUserId = SessionUtil.getUserId(request);
        int deleteCnt = doDelete(form, userIdList, loginUserId);

        redirectModel.addFlashAttribute("message", messageUtil.getMessage("GSACM013I", deleteCnt));
        return RTN_STR_OK;
    }

    @PostMapping("/GSACS030Back")
    public String GSACS030Back(HttpServletRequest request) {
        log.info("戻るボタンを押下しました。");
        SessionUtil.removeSessionValue(request,
                Arrays.asList(
                        GXConst.GSAA_PROP_GSACT020_USER_ID,
                        GXConst.GSAA_PROP_GSACT030_USER_KEKA,
                        GXConst.GSAA_PROP_GSAAT040_EX_KEY
                ));
        return "redirect:/GSACS020Display";
    }

    private int doDelete(GSACS030Form form, List<String> userIdList, String loginUserId) {
        UserDeleteParam udp = new UserDeleteParam();
        udp.setUserIdList(userIdList);
        udp.setUser_delete_flag(GXConst.GSAA_PROP_GSACT040_DELETE_FLG);
        udp.setUser_update_uid(loginUserId);

        return gsacService.deleteUserList(udp);
    }
}
