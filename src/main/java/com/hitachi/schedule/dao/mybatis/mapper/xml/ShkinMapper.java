package com.hitachi.schedule.dao.mybatis.mapper.xml;


import com.hitachi.schedule.dao.mybatis.pojo.Shkin;
import org.springframework.stereotype.Repository;


@Repository
public interface ShkinMapper {
    void insertShkin(Shkin shkin);
}