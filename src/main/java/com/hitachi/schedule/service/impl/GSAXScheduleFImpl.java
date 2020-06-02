package com.hitachi.schedule.service.impl;

import com.hitachi.schedule.config.component.CommonUtil;
import com.hitachi.schedule.controller.param.MenuInfo;
import com.hitachi.schedule.controller.param.UserHeadInfo;
import com.hitachi.schedule.dao.mybatis.mapper.xml.*;
import com.hitachi.schedule.dao.mybatis.pojo.Menu;
import com.hitachi.schedule.dao.mybatis.pojo.Shkin;
import com.hitachi.schedule.dao.mybatis.pojo.Ssk;
import com.hitachi.schedule.dao.mybatis.pojo.User;
import com.hitachi.schedule.service.GSAXScheduleF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class GSAXScheduleFImpl implements GSAXScheduleF {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SskMapper sskMapper;
    @Autowired
    private UserRlMapper userRlMapper;
    @Autowired
    private RlMapper rlMapper;
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private CommonUtil commonUtil;

    @Override
    public User findByUserId(String user_id) {
        User user = userMapper.findUserById(user_id);
        return user;
    }

    @Override
    public UserHeadInfo getUserHeadInfo(User user) {
        String user_id = user.getUser_id();
        Shkin shkin = user.getShkin();
        String ssk_id = shkin.getSsk_id();
        String shkin_name = shkin.getShkin_smi();
        String roles = userRlMapper.getUserRlIdByUserId(user_id);

        UserHeadInfo uhi = new UserHeadInfo();
        uhi.setSsk_name(join_ssk(ssk_id));
        uhi.setShjin_smi(shkin_name);
        uhi.setRoles(roles);

        List<String> allRlName = rlMapper.getAllRlNameByUserId(user_id);
        uhi.setRolesName(commonUtil.getStringFromListBySpace(allRlName));

        uhi.setUserImg(user.getUser_image());
        return uhi;
    }

    @Override
    public String join_ssk(String ssk_id) {
        List<String> ssk_name_list = new ArrayList<>();
        Ssk ssk = sskMapper.findSskById(ssk_id);

        ssk_name_list.add(ssk.getSsk_name());
        while (null != ssk.getJui_ssk_id()) {
            ssk = sskMapper.findSskById(ssk.getJui_ssk_id());
            ssk_name_list.add(ssk.getSsk_name());
        }

        Collections.reverse(ssk_name_list);
        return commonUtil.getStringFromListBySpace(ssk_name_list);
    }

    @Override
    public List<MenuInfo> getMenuInfoList(Set<GrantedAuthority> userRoleList) {
        List<Menu> db_rtn = menuMapper.listAllMenu();
        List<MenuInfo> menuInfoList = new ArrayList<>();
        for (Menu obj : db_rtn) {
            MenuInfo menuInfo = new MenuInfo();
            menuInfo.setHrefUrl(obj.getHref_url());
            menuInfo.setDivName(obj.getDiv_name());
            menuInfo.setDtName(obj.getDt_name());
            menuInfo.setDdValue(obj.getDd_value());
            menuInfo.setDisplayFlg(getDisplayFlg(userRoleList, obj.getGyoumu_name()));
            menuInfoList.add(menuInfo);
        }
        return menuInfoList;
    }

    @Override
    public Set<String> getUserRoles(String user_id) {
        List<String> db_rtn = rlMapper.getUserRolesByUserId(user_id);

        List<String> rtn_obj = rlMapper.getUserRolesByUserId(user_id);
        return commonUtil.getSetFromList(db_rtn);
    }

    private boolean getDisplayFlg(Set<GrantedAuthority> userRoleList, String uri) {
        for (GrantedAuthority obj : userRoleList) {
            if ("/".concat(uri).startsWith(obj.getAuthority())) {
                return true;
            }
        }
        return false;
    }
}
