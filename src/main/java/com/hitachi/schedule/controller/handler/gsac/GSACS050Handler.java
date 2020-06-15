package com.hitachi.schedule.controller.handler.gsac;

import com.hitachi.schedule.config.common.GXConst;
import com.hitachi.schedule.config.common.SessionUtil;
import com.hitachi.schedule.config.component.CommonUtil;
import com.hitachi.schedule.config.component.MessageReadUtil;
import com.hitachi.schedule.controller.actionform.GSACS050Form;
import com.hitachi.schedule.controller.checker.GSACS050Checker;
import com.hitachi.schedule.controller.param.SelectInfo;
import com.hitachi.schedule.dao.mybatis.pojo.Shkin;
import com.hitachi.schedule.dao.mybatis.pojo.User;
import com.hitachi.schedule.dao.mybatis.pojo.UserRl;
import com.hitachi.schedule.service.GSACScheduleF;
import com.hitachi.schedule.service.GSAXScheduleFileF;
import com.hitachi.schedule.service.param.UserFindParam;
import com.hitachi.schedule.service.param.UserInsertResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class GSACS050Handler {
    @Autowired
    private MessageReadUtil messageUtil;
    @Autowired
    private GSACScheduleF gsacService;
    @Autowired
    private GSACS050Checker checker;
    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private GSAXScheduleFileF gsaxFileService;

    private static final String RTN_STR_OK = "redirect:/GSACS010Display";

    @GetMapping("/GSACS050Detail")
    public String GSACS050Detail(
            HttpServletRequest request,
            String userId) {
        log.info("利用者リンクを押下しました。");

        UserFindParam ufp = new UserFindParam();
        ufp.setStrUserId(userId);
        Integer startNo = 1;
        Integer endNo = startNo + GXConst.GSAA_PROP_GSACT020_DISPLAY_SIZE - 1;
        ufp.setStartNo(startNo);
        ufp.setEndNo(endNo);

        SessionUtil.setGsacSearchParam(request, ufp);

        SessionUtil.saveSessionValue(
                request,
                GXConst.GSAA_PROP_GSACT020_USER_ID,
                userId
        );
        return "redirect:/GSACS030Display";
    }

    @PostMapping("/GSACS050Back")
    public String GSACS050Back(HttpServletRequest request) {
        log.info("戻るボタンを押下しました。");
        return RTN_STR_OK;
    }

    @PostMapping("/GSACS050Register")
    @ResponseBody
    public Map<String, Object> GSACS050Register(
            @Valid GSACS050Form form,
            BindingResult br,
            HttpServletRequest request) {
        log.info("登録実行ボタンを押下しました。");
        Map<String, Object> result = new HashMap<>();

        if (checker.formCheck(result, form, br)) {
            return result;
        }

        if (checker.gmCheck(result, form)) {
            return result;
        }

        String loginUserId = SessionUtil.getUserId(request);
        UserInsertResult uir = doInsert(form, loginUserId);

        Map<String, Object> success = new HashMap<>();

        String user_id = uir.getUser_id();
        String shkin_id = uir.getShkin_id();
        success.put("userId", user_id);
        success.put("message", messageUtil.getMessage("GSACM031I", user_id, shkin_id));
        result.put("success", success);
        return result;
    }

    @GetMapping("/getSelect/{jui_ssk_id}")
    @ResponseBody
    public List<SelectInfo> getImg(@PathVariable("jui_ssk_id") String jui_ssk_id) {
        log.info("プルダウン取得ボタンを押下しました。");
        List<SelectInfo> sskInfo = gsacService.getSskInfo(jui_ssk_id);
        return sskInfo;
    }

    private UserInsertResult doInsert(GSACS050Form form, String loginUserId) {
        int ex_key = GXConst.GS_PROP_INIT_EX_KEY;
        User user = new User();
        user.setUser_password(form.getStrUserPassword());
        user.setUser_delete_flag(GXConst.GSAA_PROP_GSACT040_SEARCH_FLG);

        String userImage = gsaxFileService.saveFile(
                GXConst.GSAB_MONGODB_COLLECTION_NAME_USER,
                form.getImageFile());
        user.setUser_image(userImage);

        user.setUser_update_uid(loginUserId);
        user.setUser_ex_key(ex_key);

        List<String> rlIdList = form.getRlIdList();
        String rlId = commonUtil.getStringFromList(rlIdList);
        UserRl userRl = new UserRl();
        userRl.setRl_id(rlId);
        userRl.setUser_rl_update_uid(loginUserId);
        userRl.setUser_rl_ex_key(ex_key);

        Shkin shkin = new Shkin();
        shkin.setSsk_id(form.getStrSsk());
        shkin.setShkin_smi(form.getStrShkinSmi());
        shkin.setShkin_update_uid(loginUserId);
        shkin.setShkin_ex_key(ex_key);

        return gsacService.insertUserInfo(user, userRl, shkin);
    }
}
