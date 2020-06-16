package com.hitachi.schedule.controller.param;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ImgUploadResult implements Serializable {

    private int errno;

    private List<String> data;
}
