package com.hitachi.schedule.service.impl;


import com.hitachi.schedule.controller.component.CommonUtil;
import com.hitachi.schedule.controller.param.SecurityUser;
import com.hitachi.schedule.mybatis.pojo.User;
import com.hitachi.schedule.service.GSAXScheduleF;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;


@Component
public class GSAXSLoginService implements UserDetailsService {
    @Autowired
    private GSAXScheduleF gsaxService;
    @Autowired
    private CommonUtil commonUtil;

    @Override
    public UserDetails loadUserByUsername(String user_id) throws UsernameNotFoundException {
        // 根据user_id检索数据库
        User user = gsaxService.findByUserId(user_id);
        if (user == null) {
            throw new UsernameNotFoundException("user_id " + user_id + " not found");
        }
        // SecurityUser实现UserDetails并将User的密码加密处理
        SecurityUser user_rtn = new SecurityUser();
        BeanUtils.copyProperties(user, user_rtn);
        user_rtn.setUser_password(new BCryptPasswordEncoder().encode(user_rtn.getUser_password()));

        Set<String> userRoles = gsaxService.getUserRoles(user_id);
        user_rtn.setUserRoleList(commonUtil.getAuthoritySet(userRoles));

        return user_rtn;
    }
}

