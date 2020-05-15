package com.hitachi.schedule.controller.handler.gsax;


import com.hitachi.schedule.controller.common.SessionUtil;
import com.hitachi.schedule.controller.param.SecurityUser;
import com.hitachi.schedule.controller.param.UserHeadInfo;
import com.hitachi.schedule.service.GSAXScheduleF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class GSAXS010Handler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Autowired
    private GSAXScheduleF gsaxService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        String strUserId = user.getUser_id();

        UserHeadInfo uhi = gsaxService.getUserHeadInfo(user);
        Map<String, Object> gs_info = new HashMap<>();
        gs_info.put("sskKnznName", uhi.getSsk_name());
        gs_info.put("shjinSmi", uhi.getShjin_smi());
        gs_info.put("userId", strUserId);
        gs_info.put("roles", uhi.getRoles());
        gs_info.put("rolesName", uhi.getRolesName());
        gs_info.put("userImg", uhi.getUserImg());

        gs_info.put("menuInfoList", gsaxService.getMenuInfoList(user.getUserRoleList()));

        SessionUtil.saveUserDetial(request, gs_info);

//        super.onAuthenticationSuccess(request, response, authentication);// 登录成功后，自动跳转到，之前访问的url的页面
        response.sendRedirect("/GSAXS020Display");// 不管之前的请求，直接跳转到固定的url（"/"）在/
    }
}
