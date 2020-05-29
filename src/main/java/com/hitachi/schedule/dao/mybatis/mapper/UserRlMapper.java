package com.hitachi.schedule.dao.mybatis.mapper;


import com.hitachi.schedule.dao.mybatis.pojo.UserRl;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRlMapper {
    public String getUserRlIdByUserId(String user_id);

    public void updateByUserId(UserRl userRl);

    void insertUserRl(UserRl userRl);
}