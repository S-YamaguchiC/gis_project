package com.example.linebot.web;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

public class UploadForm implements Serializable {

    private MultipartFile multipartFile;

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }
}
