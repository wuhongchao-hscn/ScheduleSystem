package com.hitachi.schedule.mybatis.mapper;


import com.hitachi.schedule.mybatis.pojo.Ssk;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SskMapper {
    public Ssk findSskById(String ssk_id);

    public List<Ssk> listAllSskByNull();

    public List<Ssk> listAllSskByJuiSskId(String jui_ssk_id);
}