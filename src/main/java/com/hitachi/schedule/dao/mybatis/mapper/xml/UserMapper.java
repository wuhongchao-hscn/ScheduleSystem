package com.hitachi.schedule.dao.mybatis.mapper.xml;


import com.hitachi.schedule.dao.mybatis.param.UserNameForSearch;
import com.hitachi.schedule.dao.mybatis.pojo.User;
import com.hitachi.schedule.service.param.UserDeleteParam;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserMapper {
    public User findUserById(String user_id);

    public List<User> findUserByShkin(UserNameForSearch unfs);

    public Integer countUserByShkin(UserNameForSearch unfs);

    public void insertUser(User user);

    public void updateUser(User user);

    public void deleteUser(int id);

    int findUserCountById(String userId);

    int findUserExKeyById(String userId);

    int updateUserList(UserDeleteParam udp);
}