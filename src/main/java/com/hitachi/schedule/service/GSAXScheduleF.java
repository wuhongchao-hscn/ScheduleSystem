package com.hitachi.schedule.service;

import com.hitachi.schedule.controller.param.MenuInfo;
import com.hitachi.schedule.controller.param.UserHeadInfo;
import com.hitachi.schedule.dao.mybatis.pojo.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Set;

public interface GSAXScheduleF {

    User findByUserId(String user_id);

    UserHeadInfo getUserHeadInfo(User user);

    String join_ssk(String ssk_id);

    List<MenuInfo> getMenuInfoList(Set<GrantedAuthority> userRlList);

    Set<String> getUserRoles(String user_id);
}
