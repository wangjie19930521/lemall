package com.leyoumall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @ClassName LySearchService
 * @Description:
 * @Author wangJ1e
 * @Date 2019-08-15
 * @Version V1.0
 **/
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class LySearchService {
    public static void main(String[] args) {
        SpringApplication.run(LySearchService.class,args);
    }
}
