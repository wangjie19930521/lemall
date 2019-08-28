package com.leyoumall.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @ClassName UploadProperties
 * @Description:
 * @Author Administrator
 * @Date 2019-08-05
 * @Version V1.0
 **/
@Data
@ConfigurationProperties(prefix = "ly.upload")
public class UploadProperties {
    private String baseUrl;
    private List allowTypes;
}
