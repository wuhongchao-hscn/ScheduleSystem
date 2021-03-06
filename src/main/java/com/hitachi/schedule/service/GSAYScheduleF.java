package com.hitachi.schedule.service;

import com.hitachi.schedule.controller.param.ImgUploadResult;
import com.hitachi.schedule.dao.mongodb.FileDocument;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface GSAYScheduleF {

    @Transactional
    String saveFile(String collectionName, MultipartFile imageFile);

    @Transactional
    void removeFile(String id, String collectionName, boolean isDeleteFile);

    Optional<FileDocument> getFileById(String id, String collectionName, Integer width, Integer height);

    FileDocument getFileByMd5(String md5, String collectionName);

    List<FileDocument> listFilesByPage(int pageIndex, int pageSize, String collectionName);

    ImgUploadResult saveImgFile(MultiValueMap<String, MultipartFile> multiFileMap);
}
