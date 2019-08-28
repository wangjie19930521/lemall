package com.leyoumall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ClassName LyUpload
 * @Description:
 * @Author Administrator
 * @Date 2019-08-05
 * @Version V1.0
 **/
@SpringBootApplication
@EnableDiscoveryClient
public class LyUpload {
    public static void main(String[] args) {
        SpringApplication.run(LyUpload.class);
    }
}
