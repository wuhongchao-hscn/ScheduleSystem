package com.hitachi.schedule.mybatis.mapper;


import com.hitachi.schedule.mybatis.pojo.Shkin;
import org.springframework.stereotype.Repository;


@Repository
public interface ShkinMapper {
    void insertShkin(Shkin shkin);
}