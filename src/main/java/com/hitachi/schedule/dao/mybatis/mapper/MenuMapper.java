package com.hitachi.schedule.dao.mybatis.mapper;


import com.hitachi.schedule.dao.mybatis.pojo.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuMapper {
    public List<Menu> listAllMenu();
}