package com.hitachi.schedule.mybatis.mapper;


import com.hitachi.schedule.mybatis.pojo.Rl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RlMapper {
    public List<String> getAllRlNameByUserId(String user_id);

    public List<String> getUserRolesByUserId(String user_id);

    public List<Rl> listAllRl();
}