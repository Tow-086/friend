package com.friendserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement //开启注解方式的事务管理
//@Slf4j
@EnableCaching//开启缓存功能
@EnableScheduling//开启定时任务功能
@EnableAsync  // 启用异步
@EntityScan("com.friendpojo.entity") // 添加此行以确保扫描到实体类
public class FriendServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FriendServerApplication.class, args);
    }

}
