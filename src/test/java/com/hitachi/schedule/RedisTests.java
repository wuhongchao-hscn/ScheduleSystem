package com.hitachi.schedule;

import com.hitachi.schedule.dao.mybatis.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTests {
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Test
    public void setStringValue() {
        stringRedisTemplate.opsForValue().append("keys", "zhangsan");
        stringRedisTemplate.opsForValue().append("keys", " ");
        stringRedisTemplate.opsForValue().append("keys", "lisi");
    }

    @Test
    public void getStringValue() {
        System.out.println(stringRedisTemplate.opsForValue().get("keys"));
    }

    @Test
    public void setListValue() {
        User user = new User();
        user.setUser_id("dwawdaw");
        user.setUser_password("password");
        user.setUser_update_uid("abcacw");
        user.setUser_update_ymd("20-9d09r0u");
        redisTemplate.opsForList().leftPushAll("lists1", Arrays.asList("dwada", true, user, 123));
        redisTemplate.opsForList().leftPush("lists2", user);
    }

    @Test
    public void getListValue() {
        Long size1 = redisTemplate.opsForList().size("lists1");
        System.out.println(size1);

        Long size2 = redisTemplate.opsForList().size("lists2");
        System.out.println(size2);

        System.out.println(redisTemplate.opsForList().range("lists1", 0, size1));
        System.out.println(redisTemplate.opsForList().range("lists2", 0, size2));
        System.out.println(redisTemplate.opsForList().range("lists1", 0, -1));
    }

}