package com.hitachi.schedule.service.param;

import com.hitachi.schedule.dao.mybatis.pojo.User;
import lombok.Data;

import java.util.List;

@Data
public class UserDeleteParam extends User {
    private List<String> userIdList;
}
