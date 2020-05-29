package com.hitachi.schedule.service;

import com.hitachi.schedule.mongodb.FileDocument;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface GSAXScheduleFileF {

    @Transactional
    String saveFile(String collectionName, MultipartFile imageFile);

    @Transactional
    void removeFile(String id, String collectionName, boolean isDeleteFile);

    Optional<FileDocument> getFileById(String id, String collectionName, Integer width, Integer height);

    FileDocument getFileByMd5(String md5, String collectionName);

    List<FileDocument> listFilesByPage(int pageIndex, int pageSize, String collectionName);
}
