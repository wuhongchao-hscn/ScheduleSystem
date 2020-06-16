package com.hitachi.schedule.config.configuration;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.hitachi.schedule.config.common.GXConst;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.io.Serializable;

@Configuration
@EnableCaching// 开启基于注解的缓存
public class RedisConfig {

    @Bean
    RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        RedisSerializationContext.SerializationPair serializationPair =
                RedisSerializationContext.SerializationPair.fromSerializer(getJsonSerializer());

        RedisCacheConfiguration config = RedisCacheConfiguration
                .defaultCacheConfig()
                .serializeValuesWith(serializationPair);

        return (builder) -> builder
                .withCacheConfiguration(GXConst.GSAX_CACHE_NAME, config)
                .withCacheConfiguration(GXConst.GSAY_CACHE_NAME, config)
                .withCacheConfiguration(GXConst.GSAA_CACHE_NAME, config)
                .withCacheConfiguration(GXConst.GSAB_CACHE_NAME, config)
                .withCacheConfiguration(GXConst.GSAC_CACHE_NAME, config);
    }

    @Bean
    public RedisTemplate<String, Serializable> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setDefaultSerializer(getJsonSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    private Jackson2JsonRedisSerializer getJsonSerializer() {
        ObjectMapper objectMapper = new ObjectMapper();
        // 所有的属性进行json序列化
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 自动进行json序列化转化的类型
        objectMapper.activateDefaultTyping(
                // 不做任何检证
                LaissezFaireSubTypeValidator.instance,
                // 所有非final类型的属性
                ObjectMapper.DefaultTyping.NON_FINAL,
                // list为数组
                JsonTypeInfo.As.WRAPPER_ARRAY);

        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);
        serializer.setObjectMapper(objectMapper);
        return serializer;
    }

}
