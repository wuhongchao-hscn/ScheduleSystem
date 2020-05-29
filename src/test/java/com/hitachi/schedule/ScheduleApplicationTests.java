package com.hitachi.schedule;

import com.hitachi.schedule.dao.jpa.dao.ArticleDao;
import com.hitachi.schedule.dao.mybatis.mapper.UserMapper;
import com.hitachi.schedule.dao.mybatis.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.Serializable;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ScheduleApplicationTests {

    @Autowired
    private RedisTemplate<String, String> strRedisTemplate;
    @Autowired
    private RedisTemplate<String, Serializable> serializableRedisTemplate;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ArticleDao articleDao;


    @Test
    public void testString() {
//        strRedisTemplate.opsForValue().set("strKey", "zwqh");
        System.out.println(strRedisTemplate.opsForValue().get("strKey"));
    }

    @Test
    public void testSerializable() {
        User user = new User();
        user.setUser_id("1");
        serializableRedisTemplate.opsForValue().set("user", user);
        User user2 = (User) serializableRedisTemplate.opsForValue().get("user");
        System.out.println("user:" + user2.getUser_id());
    }

    @Test
    public void testDoubleDataSource() {
        System.out.println(userMapper.findUserById("1"));
        System.out.println(articleDao.findByArticleId(1));
    }

}