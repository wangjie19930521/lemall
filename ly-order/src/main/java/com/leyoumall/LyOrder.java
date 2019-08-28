package com.leyoumall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @ClassName LyOrder
 * @Description:
 * @Author wangJ1e
 * @Date 2019-08-23
 * @Version V1.0
 **/
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@MapperScan("com.leyoumall.mapper")
public class LyOrder {
    public static void main(String[] args) {
        SpringApplication.run(LyOrder.class,args);
    }
}
