package com.leyoumall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ClassName LyUser
 * @Description:
 * @Author wangJ1e
 * @Date 2019-08-18
 * @Version V1.0
 **/
@MapperScan("com.leyoumall.mapper")
@EnableDiscoveryClient
@SpringBootApplication
public class LyUser {
    public static void main(String[] args) {
        SpringApplication.run(LyUser.class,args);
    }
}
