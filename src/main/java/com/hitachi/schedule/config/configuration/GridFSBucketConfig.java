package com.hitachi.schedule.config.configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GridFSBucketConfig {
    //获取配置文件中数据库信息
    @Value("${spring.data.mongodb.database}")
    private String db;

    // GridFSBucket用于打开下载流
    @Bean
    public GridFSBucket gridFsBucket(MongoClient mongoClient) {
        MongoDatabase database = mongoClient.getDatabase(db);
        // 构造函数，默认的存储桶名称：fs
        GridFSBucket gridFSBucket = GridFSBuckets.create(database);
        return gridFSBucket;
    }
}
