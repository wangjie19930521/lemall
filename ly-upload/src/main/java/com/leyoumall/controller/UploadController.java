package com.leyoumall.controller;

import com.leyoumall.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName UploadController
 * @Description:
 * @Author Administrator
 * @Date 2019-08-05
 * @Version V1.0
 **/
@RestController
@RequestMapping("upload")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping("/image")
    public ResponseEntity<String> uploadImage(@RequestParam("file")MultipartFile file){
        return ResponseEntity.ok(uploadService.uploadImage(file));
    }
}
