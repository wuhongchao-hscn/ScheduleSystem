package com.hitachi.schedule.mybatis.mapper;


import com.hitachi.schedule.mybatis.pojo.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuMapper {
    public List<Menu> listAllMenu();
}