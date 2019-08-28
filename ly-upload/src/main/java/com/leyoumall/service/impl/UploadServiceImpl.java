package com.leyoumall.service.impl;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyoumall.common.enums.ExceptionEnum;
import com.leyoumall.common.exception.LyException;
import com.leyoumall.config.UploadProperties;
import com.leyoumall.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName UploadService
 * @Description:
 * @Author Administrator
 * @Date 2019-08-05
 * @Version V1.0
 **/
@Service
@Slf4j
@EnableConfigurationProperties(UploadProperties.class)
public class UploadServiceImpl implements UploadService {

    @Autowired
    private UploadProperties uploadProperties;

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    /**
     * @MethodName: uploadImage
     * @Description: 上传图片
     * @Param: [file]
     * @Return: java.lang.String
     * @Author: Administrator
     * @Date: 2019-08-05
     **/
    @Override
    public String uploadImage(MultipartFile file) {
        try {
            // 1、图片信息校验
            // 1)校验文件类型
            String type = file.getContentType();
            if (!uploadProperties.getAllowTypes().contains(type)) {
                log.info("上传失败，文件类型不匹配：{}", type);
                throw new LyException(ExceptionEnum.INVALID_FILE_TYPE);
            }
            // 2)校验图片内容   如果不是图片，ImageIO 得到null
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                log.info("上传失败，文件内容不符合要求");
                throw new LyException(ExceptionEnum.INVALID_FILE_TYPE);
            }
            //截取字符串最后一个符号之后的所有
            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(),".");
            StorePath Storepath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), extension, null);
            String url = uploadProperties.getBaseUrl() + Storepath.getFullPath();
            return url;
        } catch (Exception e) {
            log.error("文件上传失败");
            throw new LyException(ExceptionEnum.UPLOAD_FILE_ERROR);
        }
    }
}
