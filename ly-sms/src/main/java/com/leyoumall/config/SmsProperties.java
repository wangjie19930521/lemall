package com.leyoumall.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName config
 * @Description:
 * @Author wangJ1e
 * @Date 2019-08-18
 * @Version V1.0
 **/
@Data
@ConfigurationProperties(prefix = "ly.sms")
public class SmsProperties {
    private String accessKeyId;
    private String accessKeySecret;
    private String signName;
    private String verifyCodeTemplate;
}
