package com.leyoumall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @ClassName LyPage
 * @Description:
 * @Author wangJ1e
 * @Date 2019-08-17
 * @Version V1.0
 **/
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class LyPage {
    public static void main(String[] args) {
        SpringApplication.run(LyPage.class,args);
    }
}
