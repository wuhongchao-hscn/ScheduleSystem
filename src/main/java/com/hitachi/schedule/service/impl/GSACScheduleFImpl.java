package com.hitachi.schedule.service.impl;

import com.hitachi.schedule.controller.common.DateUtil;
import com.hitachi.schedule.controller.common.GCConstGlobals;
import com.hitachi.schedule.controller.component.CommonUtil;
import com.hitachi.schedule.controller.param.SelectInfo;
import com.hitachi.schedule.controller.param.UserDetialInfoList;
import com.hitachi.schedule.controller.param.UserInfoList;
import com.hitachi.schedule.mybatis.mapper.*;
import com.hitachi.schedule.mybatis.param.UserNameForSearch;
import com.hitachi.schedule.mybatis.pojo.*;
import com.hitachi.schedule.service.GSACScheduleF;
import com.hitachi.schedule.service.GSAXScheduleF;
import com.hitachi.schedule.service.param.UserDeleteParam;
import com.hitachi.schedule.service.param.UserFindParam;
import com.hitachi.schedule.service.param.UserFindResult;
import com.hitachi.schedule.service.param.UserInsertResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GSACScheduleFImpl implements GSACScheduleF {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RlMapper rlMapper;
    @Autowired
    private UserRlMapper userRlMapper;
    @Autowired
    private CodeMapper codeMapper;
    @Autowired
    private SskMapper sskMapper;
    @Autowired
    private ShkinMapper shkinMapper;
    @Autowired
    private GSAXScheduleF gsaxService;
    @Autowired
    private CommonUtil commonUtil;


    @Override
    public UserFindResult findUserList(UserFindParam ufp) {
        String strTabFlag = ufp.getStrTabFlag();
        List<UserInfoList> uiList = new ArrayList<>();
        UserFindResult ufr = new UserFindResult();
        if (!"tab2".equals(strTabFlag)) {
            User user = userMapper.findUserById(ufp.getStrUserId());
            ufr.setPageList(null);
            if (null != user) {
                addToUserList(uiList, user);
            }
        } else {
            UserNameForSearch unfs = new UserNameForSearch();
            String strShkinId = ufp.getStrShkinId();
            if (!StringUtils.isEmpty(strShkinId)) {
                unfs.setShkin_id(strShkinId);
            }

            String shkin_smi = ufp.getStrShkinSmi();
            String strKnskShbtCode = ufp.getStrKnskShbtCode();
            if (!StringUtils.isEmpty(shkin_smi)) {
                unfs.setShkin_smi(shkin_smi);
                unfs.setStrKnskShbtCode(strKnskShbtCode);
            }

            Integer allSize = userMapper.countUserByShkin(unfs);

            if (0 != allSize) {
                int pageAll = commonUtil.getPageNo(allSize);
                ufp.setAllPage(pageAll);
                List<String> pageList = new ArrayList<>();
                for (int i = 0; i < pageAll; i++) {
                    pageList.add(String.valueOf(i + 1));
                }
                if (pageList.size() >= 2) {
                    ufr.setPageList(pageList);
                }

                Integer startNo = ufp.getStartNo();
                Integer endNo = ufp.getEndNo();
                if (endNo > allSize) {
                    endNo = allSize;
                }
                unfs.setStartNo(startNo - 1);
                unfs.setSizes(endNo - startNo + 1);
                ufr.setStartNo(startNo);
                ufr.setEndNo(endNo);

                int pageNow = commonUtil.getPageNo(startNo);
                ufr.setPageNow(String.valueOf(pageNow));
                StringBuilder hitRangeLabel = new StringBuilder()
                        .append(allSize)
                        .append("件中　")
                        .append(startNo)
                        .append("～")
                        .append(endNo)
                        .append("件");

                List<User> userList = userMapper.findUserByShkin(unfs);
                int userSize = userList.size();
                for (User obj : userList) {
                    addToUserList(uiList, obj);
                }

                ufr.setHitRangeLabel(hitRangeLabel.toString());

            }

        }
        ufr.setUserList(uiList);
        return ufr;
    }

    @Override
    public List<SelectInfo> getCodeInfoByBnri(String code_bnri) {
        List<Code> db_rtn = codeMapper.getCodeInfoByBnri(code_bnri);
        List<SelectInfo> rtn_obj = new ArrayList<>();
        for (Code obj : db_rtn) {
            rtn_obj.add(new SelectInfo(String.valueOf(obj.getId()), obj.getCode_value()));
        }
        return rtn_obj;
    }

    @Override
    public List<SelectInfo> getRlInfo() {
        List<Rl> db_rtn = rlMapper.listAllRl();
        List<SelectInfo> rtn_obj = new ArrayList<>();
        for (Rl obj : db_rtn) {
            rtn_obj.add(new SelectInfo(String.valueOf(obj.getRl_id()), obj.getRl_name()));
        }
        return rtn_obj;
    }

    @Override
    public String getCodeName(String code_id) {
        int id = Integer.parseInt(code_id);
        String rtn = codeMapper.getCodeNameById(id);
        return rtn.length() > 18
                ? rtn.substring(0, 18) + GCConstGlobals.GSAA_FUGO_SCHEDULE_LIST_DISPLAY
                : rtn;
    }

    @Override
    public List<UserDetialInfoList> findUserDetialList(List<String> userIdList) {
        List<UserDetialInfoList> udiList = new ArrayList<>();
        for (String userId : userIdList) {
            User user = userMapper.findUserById(userId);
            if (null != user) {
                addToUserDetialList(udiList, user);
            }
        }
        return udiList;
    }

    @Override
    public void updateUserAndRlInfo(User user, UserRl userRl) {
        String sysDate = DateUtil.getSysDateYmd();
        user.setUser_update_ymd(sysDate);
        userMapper.updateUser(user);

        userRl.setUser_rl_update_ymd(sysDate);
        userRlMapper.insertUserRl(userRl);
    }

    @Override
    public boolean isExistUser(String userId) {
        return userMapper.findUserCountById(userId) > 0;
    }

    @Override
    public int getUserExKeyById(String userId) {
        return userMapper.findUserExKeyById(userId);
    }

    @Override
    public int deleteUserList(UserDeleteParam udp) {
        String sysDate = DateUtil.getSysDateYmd();
        udp.setUser_update_ymd(sysDate);
        return userMapper.updateUserList(udp);
    }

    @Override
    public List<SelectInfo> getSskInfo(String jui_ssk_id) {
        List<Boolean> cLi = Arrays.asList(null == jui_ssk_id, "null".equals(jui_ssk_id));
        List<Ssk> db_rtn = cLi.contains(true) ? sskMapper.listAllSskByNull() : sskMapper.listAllSskByJuiSskId(jui_ssk_id);
        List<SelectInfo> rtn_obj = new ArrayList<>();
        rtn_obj.add(new SelectInfo("", "(未選択)"));
        for (Ssk obj : db_rtn) {
            rtn_obj.add(new SelectInfo(String.valueOf(obj.getSsk_id()), obj.getSsk_name()));
        }
        if (1 == rtn_obj.size()) {
            return null;
        }
        return rtn_obj;
    }

    @Override
    public UserInsertResult insertUserInfo(User user, UserRl userRl, Shkin shkin) {
        String sysDate = DateUtil.getSysDateYmd();

        shkin.setShkin_update_ymd(sysDate);
        shkinMapper.insertShkin(shkin);
        String shkin_id = shkin.getShkin_id();

        user.setUser_update_ymd(sysDate);
        user.setShkin_id(shkin_id);
        userMapper.insertUser(user);
        String user_id = user.getUser_id();

        userRl.setUser_rl_update_ymd(sysDate);
        userRl.setUser_id(user_id);
        userRlMapper.insertUserRl(userRl);

        UserInsertResult uir = new UserInsertResult();
        uir.setUser_id(user_id);
        uir.setShkin_id(shkin_id);
        return uir;
    }

    @Override
    public String getAllRlName(String userId) {
        List<String> allRlName = rlMapper.getAllRlNameByUserId(userId);
        return commonUtil.getStringFromListBySpace(allRlName);
    }

    private void addToUserList(List<UserInfoList> uiList, User user) {
        String user_id = user.getUser_id();
        Shkin shkin = user.getShkin();
        String ssk_id = shkin.getSsk_id();
        String shkin_id = shkin.getShkin_id();
        String shkin_name = shkin.getShkin_smi();

        UserInfoList uil = new UserInfoList();
        uil.setDeleteFlg(user_id);
        uil.setStrShkinId(shkin_id);
        uil.setStrShkinSmi(shkin_name);
        uil.setStrSsk(gsaxService.join_ssk(ssk_id));
        uil.setUserId(user_id);
        uiList.add(uil);
    }

    private void addToUserDetialList(List<UserDetialInfoList> uiList, User user) {
        String user_id = user.getUser_id();
        Shkin shkin = user.getShkin();
        String ssk_id = shkin.getSsk_id();
        String shkin_id = shkin.getShkin_id();
        String shkin_name = shkin.getShkin_smi();
        String userRl = userRlMapper.getUserRlIdByUserId(user_id);
        List<String> allRlName = rlMapper.getAllRlNameByUserId(user_id);

        UserDetialInfoList uil = new UserDetialInfoList();
        uil.setDeleteFlg(user_id);
        uil.setStrShkinId(shkin_id);
        uil.setStrShkinSmi(shkin_name);
        uil.setStrSsk(gsaxService.join_ssk(ssk_id));
        uil.setUserId(user_id);
        uil.setImageSrc(user.getUser_image());
        uil.setStrRlId(userRl);
        uil.setStrAllRlName(commonUtil.getStringFromListBySpace(allRlName));
        uil.setUser_ex_key(user.getUser_ex_key());
        uiList.add(uil);
    }
}
