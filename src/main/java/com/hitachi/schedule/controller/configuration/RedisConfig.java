package com.hitachi.schedule.controller.configuration;


import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;
import java.lang.reflect.Method;

@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    /***
     *  这个东西就是用来针对object查询生成缓存key的一个key生成器,更具key来判断是否命中
     *  这里生成的规则是类名加方法名加参数的字符串
     */

    @Bean
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... objects) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());//拿到类名
                sb.append(method.getName());//拿到方法名
                //添加参数名
                for (Object object : objects) {
                    sb.append(object.toString());
                }

                return sb.toString();//类名方法名参数名连起来的字符串
            }
        };
    }

    //    /***
    //     * 缓存管理器，这里是包裹了redisTemplate的缓存管理器
    //     * @param redisTemplate
    //     * @return
    //     */
    //    @Bean
    //    public CacheManager cacheManager(RedisTemplate redisTemplate) {
    //        return new RedisCacheManager(redisTemplate);
    //    }

    @Bean
    public RedisTemplate<String, Serializable> redisTemplate(LettuceConnectionFactory connectionFactory) {
        // 默认情况下的模板只能支持 RedisTemplate<String,String>，只能存入字符串，
        // 很多时候，我们需要自定义 RedisTemplate ，设置序列化器，这样我们可以很方便的操作实例对象。如下所示：
        RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }
}
