package com.hitachi.schedule.controller.component;

import com.hitachi.schedule.controller.common.SessionUtil;
import com.hitachi.schedule.controller.param.SecurityUser;
import com.hitachi.schedule.controller.param.UserHeadInfo;
import com.hitachi.schedule.service.GSAXScheduleF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UriAccessService {
    @Autowired
    private GSAXScheduleF gsaxService;

    public boolean canAccess(HttpServletRequest request, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal == null) {
            return false;
        }

        if (authentication instanceof AnonymousAuthenticationToken) {
            //check if this uri can be access by anonymous
            return false;
        }

        if (!SessionUtil.isUserDetialExist(request)) {
            SecurityUser user = (SecurityUser) principal;
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
        }

        Set<String> roles = authentication.getAuthorities()
                .stream()
                .map(e -> e.getAuthority())
                .collect(Collectors.toSet());
        String uri = request.getRequestURI();

        for (String s : roles) {
            if (uri.startsWith(s)) {
                return true;
            }
        }

        return false;
    }
}
