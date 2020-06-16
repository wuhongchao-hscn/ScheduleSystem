package com.hitachi.schedule.controller.handler;

import com.hitachi.schedule.config.common.ImgGetUtil;
import com.hitachi.schedule.config.component.CommonUtil;
import com.hitachi.schedule.config.exception.ErrorDownload;
import com.hitachi.schedule.controller.param.ImgUploadResult;
import com.hitachi.schedule.dao.mongodb.FileDocument;
import com.hitachi.schedule.service.GSAYScheduleF;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;


@Controller
@Slf4j
public class CommonFileHandler {

    @Value("${web.default-image.path}")
    private String webDefaultImagePath;

    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private GSAYScheduleF gsaxFileService;

    @GetMapping("/commonGetImg/{id}")
    public void commonGetImg(
            HttpServletResponse response,
            @PathVariable("id") String id,
            Integer width,
            Integer height,
            String param) throws IOException {
        log.info("IMG取得ボタンを押下しました。");

        String collectionName = commonUtil.getCollectionName(param);

        Optional<FileDocument> file = gsaxFileService.getFileById(id, collectionName, width, height);
        byte[] imgData = null;
        if (file.isPresent()) {
            imgData = file.get().getContent();
        } else {
            ClassPathResource classPathResource = new ClassPathResource(webDefaultImagePath);
            File fileNoImg = classPathResource.getFile();
            imgData = Files.readAllBytes(Paths.get(fileNoImg.getAbsolutePath()));
        }
        try {
            ImgGetUtil.setProperties(response);
            ImgGetUtil.doExport(imgData, response.getOutputStream());
        } catch (Exception e) {
            throw new ErrorDownload();
        }
    }

    @GetMapping("/commonGetImg/")
    public void commonGetImg(
            HttpServletResponse response) throws IOException {
        log.info("IMG取得ボタンを押下しました。");

        ClassPathResource classPathResource = new ClassPathResource(webDefaultImagePath);
        File fileNoImg = classPathResource.getFile();

        byte[] imgData = Files.readAllBytes(Paths.get(fileNoImg.getAbsolutePath()));
        try {
            ImgGetUtil.setProperties(response);
            ImgGetUtil.doExport(imgData, response.getOutputStream());
        } catch (Exception e) {
            throw new ErrorDownload();
        }
    }

    @PostMapping("/commonUploadImg")
    @ResponseBody
    public ImgUploadResult commonUploadImg(
            StandardMultipartHttpServletRequest request) {
        log.info("IMGアップロードボタンを押下しました。");
        MultiValueMap<String, MultipartFile> multiFileMap = request.getMultiFileMap();
        ImgUploadResult iur = gsaxFileService.saveImgFile(multiFileMap);
        return iur;
    }
}
