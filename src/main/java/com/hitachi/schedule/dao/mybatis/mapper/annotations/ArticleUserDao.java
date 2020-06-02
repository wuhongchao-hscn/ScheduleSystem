package com.hitachi.schedule.dao.mybatis.mapper.annotations;


import com.hitachi.schedule.dao.mybatis.param.ArticeleUserInfo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ArticleUserDao {

    @Select("SELECT" +
            " a.shkin_smi, b.user_image " +
            "FROM public.SHKIN a, public.USER b " +
            "WHERE a.shkin_id = b.shkin_id " +
            "AND b.user_id=#{userId}")
    public Optional<ArticeleUserInfo> findArticleUserInfo(String userId);
}