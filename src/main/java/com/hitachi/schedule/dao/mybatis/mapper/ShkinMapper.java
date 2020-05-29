package com.hitachi.schedule.dao.mybatis.mapper;


import com.hitachi.schedule.dao.mybatis.pojo.Shkin;
import org.springframework.stereotype.Repository;


@Repository
public interface ShkinMapper {
    void insertShkin(Shkin shkin);

    String findShkinName(String userId);
}