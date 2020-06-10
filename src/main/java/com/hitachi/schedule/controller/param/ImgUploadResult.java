package com.hitachi.schedule.controller.param;

import lombok.Data;

import java.util.List;

@Data
public class ImgUploadResult {
    
    private int errno;

    private List<String> data;
}
