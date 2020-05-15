package com.hitachi.schedule.mybatis.mapper;


import com.hitachi.schedule.mybatis.pojo.Kigst;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface KigstMapper {
    public String findKigstNameById(String kigst_id);

    public int findKigstCount(String kigst_id);

    public List<Kigst> getAllKigst();
}