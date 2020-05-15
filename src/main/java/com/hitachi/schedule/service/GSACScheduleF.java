package com.hitachi.schedule.service;

import com.hitachi.schedule.controller.param.SelectInfo;
import com.hitachi.schedule.controller.param.UserDetialInfoList;
import com.hitachi.schedule.mybatis.pojo.Shkin;
import com.hitachi.schedule.mybatis.pojo.User;
import com.hitachi.schedule.mybatis.pojo.UserRl;
import com.hitachi.schedule.service.param.UserDeleteParam;
import com.hitachi.schedule.service.param.UserFindParam;
import com.hitachi.schedule.service.param.UserFindResult;
import com.hitachi.schedule.service.param.UserInsertResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GSACScheduleF {
    UserFindResult findUserList(UserFindParam ufp);

    List<SelectInfo> getCodeInfoByBnri(String code_bnri);

    List<SelectInfo> getRlInfo();

    String getCodeName(String code_id);

    List<UserDetialInfoList> findUserDetialList(List<String> userIdList);

    @Transactional
    void updateUserAndRlInfo(User user, UserRl userRl);

    boolean isExistUser(String userId);

    int getUserExKeyById(String userId);

    @Transactional
    int deleteUserList(UserDeleteParam udp);

    List<SelectInfo> getSskInfo(String jui_ssk_id);

    @Transactional
    UserInsertResult insertUserInfo(User user, UserRl userRl, Shkin shkin);

    String getAllRlName(String userId);
}
