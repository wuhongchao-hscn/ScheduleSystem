package com.hitachi.schedule.controller.handler.gsac;

import com.hitachi.schedule.config.common.GCConstGlobals;
import com.hitachi.schedule.config.common.SessionUtil;
import com.hitachi.schedule.config.component.CommonUtil;
import com.hitachi.schedule.config.component.MessageReadUtil;
import com.hitachi.schedule.controller.actionform.GSACS040Form;
import com.hitachi.schedule.controller.checker.GSACS040Checker;
import com.hitachi.schedule.dao.mybatis.pojo.User;
import com.hitachi.schedule.dao.mybatis.pojo.UserRl;
import com.hitachi.schedule.service.GSACScheduleF;
import com.hitachi.schedule.service.GSAXScheduleFileF;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class GSACS040Handler {
    @Autowired
    private MessageReadUtil messageUtil;
    @Autowired
    private GSACS040Checker checker;
    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private GSACScheduleF gsacService;
    @Autowired
    private GSAXScheduleFileF gsaxFileService;

    private static final String RTN_STR_NG = "redirect:/GSACS040Display";
    private static final String RTN_STR_OK = "redirect:/GSACS040Display";


    @PostMapping("/GSACS040Update")
    public String GSACS040Update(
            @Valid GSACS040Form form,
            BindingResult br,
            RedirectAttributes redirectModel,
            HttpServletRequest request) {
        log.info("更新実行ボタンを押下しました。");
        commonUtil.doInitGSACS040(form);

        if (checker.formCheck(redirectModel, form, br)) {
            return RTN_STR_NG;
        }

        if (checker.gmCheck(redirectModel, form)) {
            return RTN_STR_NG;
        }

        String userId = SessionUtil.getSessionValueString(
                request,
                GCConstGlobals.GSAA_PROP_GSACT020_USER_ID
        );
        int user_ex_key_session = SessionUtil.getSessionValueInt(
                request,
                GCConstGlobals.GSAA_PROP_GSACT040_EX_KEY
        );

        if (doUpdatePre(redirectModel, request, form, userId, user_ex_key_session)) {
            return RTN_STR_NG;
        }

        String loginUserId = SessionUtil.getUserId(request);
        return doUpdate(redirectModel, form, request, userId, loginUserId, user_ex_key_session);
    }

    @PostMapping("/GSACS040Back")
    public String GSACS040Back(HttpServletRequest request) {
        log.info("戻るボタンを押下しました。");

        return "redirect:/GSACS030Display";
    }

    private boolean doUpdatePre(
            Model model,
            HttpServletRequest request,
            GSACS040Form form,
            String userId,
            int user_ex_key_session) {

        if (!gsacService.isExistUser(userId)) {
            commonUtil.doRtnPre(model, form, "GSACM016W");
            return true;
        }
        int user_ex_key_db = gsacService.getUserExKeyById(userId);

        if (user_ex_key_session != user_ex_key_db) {
            commonUtil.doRtnPre(model, form, "GSACM018W");
            return true;
        }
        return false;
    }

    private String doUpdate(
            RedirectAttributes redirectModel,
            GSACS040Form form,
            HttpServletRequest request,
            String userId,
            String loginUserId,
            int user_ex_key_session) {
        User user = new User();
        user.setUser_id(userId);
        user.setUser_password(form.getStrUserPassword());
        user.setUser_delete_flag(GCConstGlobals.GSAA_PROP_GSACT040_SEARCH_FLG);
        user.setUser_update_uid(loginUserId);
        user.setUser_ex_key(user_ex_key_session == 999 ? 0 : user_ex_key_session + 1);

        List<String> rlIdList = form.getRlIdList();
        String rlId = commonUtil.getStringFromList(rlIdList);
        UserRl userRl = new UserRl();
        userRl.setRl_id(rlId);
        userRl.setUser_id(userId);
        userRl.setUser_rl_update_uid(loginUserId);
        userRl.setUser_rl_ex_key(GCConstGlobals.GS_PROP_INIT_EX_KEY);

        String strImageRadio = form.getStrImageRadio();
        if ("6".equals(strImageRadio)) {
            String userImage = gsaxFileService.saveFile(
                    GCConstGlobals.GSAB_MONGODB_COLLECTION_NAME_USER,
                    form.getImageFile());
            user.setUser_image(userImage);

        } else if ("7".equals(strImageRadio)) {
            user.setUser_image("");
        }

        gsacService.updateUserAndRlInfo(user, userRl);

        if (userId.equals(loginUserId)) {
            Map<String, Object> gs_info = SessionUtil.getUserDetial(request);

            if ("6".equals(strImageRadio)) {
                gs_info.put("userImg", user.getUser_image());
            } else if ("7".equals(strImageRadio)) {
                gs_info.put("userImg", null);
            }

            gs_info.put("roles", rlId);
            gs_info.put("rolesName", gsacService.getAllRlName(userId));

            SessionUtil.saveUserDetial(request, gs_info);

            if (!rlIdList.contains(GCConstGlobals.GS_PROP_ADMIN_CODE)) {
                String errMsg = messageUtil.getMessage("GSACM027E");
                redirectModel.addFlashAttribute("error", errMsg);
                SessionUtil.clearSessionValue(request);
                return "redirect:/GSAXS010Display";
            }
        }

        redirectModel.addFlashAttribute("warning", messageUtil.getMessage("GSACM015I"));
        return RTN_STR_OK;
    }
}
