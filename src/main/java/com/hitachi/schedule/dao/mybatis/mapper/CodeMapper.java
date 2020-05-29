package com.hitachi.schedule.dao.mybatis.mapper;


import com.hitachi.schedule.dao.mybatis.pojo.Code;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CodeMapper {
    public List<Code> getCodeInfoByBnri(String code_bnri);

    String getCodeNameById(int code_id);
}