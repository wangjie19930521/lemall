package com.leyoumall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.leyoumall.item.mapper")  //  扫描mapper
public class LyItem {
    public static void main(String[] args) {
        SpringApplication.run(LyItem.class);
    }
}
