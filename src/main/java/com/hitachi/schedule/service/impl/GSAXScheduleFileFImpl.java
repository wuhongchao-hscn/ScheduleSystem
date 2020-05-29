package com.hitachi.schedule.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import com.hitachi.schedule.config.common.GCConstGlobals;
import com.hitachi.schedule.config.exception.ErrorDownload;
import com.hitachi.schedule.dao.mongodb.FileDocument;
import com.hitachi.schedule.service.GSAXScheduleFileF;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.result.DeleteResult;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class GSAXScheduleFileFImpl implements GSAXScheduleFileF {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private GridFSBucket gridFSBucket;

    @Override
    public String saveFile(String collectionName, MultipartFile imageFile) {
        String userImage = null;
        try {
            InputStream is = imageFile.getInputStream();
            // 已存在该文件，则实现秒传
            String md5 = SecureUtil.md5(is);
            FileDocument fileDocument = getFileByMd5(md5, collectionName);
            if (fileDocument != null) {
                userImage = fileDocument.getId();
                return userImage;
            }

            String contentType = imageFile.getContentType();
            fileDocument = new FileDocument();
            fileDocument.setName(imageFile.getOriginalFilename());
            fileDocument.setSize(imageFile.getSize());
            fileDocument.setContentType(contentType);
            fileDocument.setUploadDate(new Date());
            fileDocument.setMd5(md5);
            String suffix = imageFile.getOriginalFilename().substring(imageFile.getOriginalFilename().lastIndexOf("."));
            fileDocument.setSuffix(suffix);


            String gridfsId = IdUtil.simpleUUID();
            // d5的时候，文件read到最后，所以这里需要将文件流重置
            is = imageFile.getInputStream();
            // 文件，存储在GridFS中
            gridFsTemplate.store(is, gridfsId, contentType);
            fileDocument.setGridfsId(gridfsId);

            // 文件信息，存储在集合中
            fileDocument = mongoTemplate.save(fileDocument, collectionName);
            userImage = fileDocument.getId();
        } catch (Exception ex) {
            throw new ErrorDownload();
        }
        return userImage;
    }

    @Override
    public void removeFile(String id, String collectionName, boolean isDeleteFile) {
        FileDocument fileDocument = mongoTemplate.findById(id, FileDocument.class, collectionName);
        if (fileDocument != null) {
            Query query = new Query().addCriteria(Criteria.where("_id").is(id));
            DeleteResult result = mongoTemplate.remove(query, collectionName);
            System.out.println("result:" + result.getDeletedCount());

            if (isDeleteFile) {
                Query deleteQuery = new Query().addCriteria(Criteria.where("filename").is(fileDocument.getGridfsId()));
                gridFsTemplate.delete(deleteQuery);
            }
        }
    }

    @Override
    public Optional<FileDocument> getFileById(String id, String collectionName, Integer width, Integer height) {
        FileDocument fileDocument = mongoTemplate.findById(id, FileDocument.class, collectionName);
        if (fileDocument != null) {
            Query gridQuery = new Query().addCriteria(Criteria.where("filename").is(fileDocument.getGridfsId()));
            try {
                GridFSFile fsFile = gridFsTemplate.findOne(gridQuery);
                GridFSDownloadStream in = gridFSBucket.openDownloadStream(fsFile.getObjectId());
                if (in.getGridFSFile().getLength() > 0) {
                    GridFsResource resource = new GridFsResource(fsFile, in);
                    InputStream is = resource.getInputStream();
                    Thumbnails.Builder<? extends InputStream> builder = Thumbnails.of(is);

                    if (null != width) {
                        builder = builder.width(width);
                    }
                    if (null != height) {
                        builder = builder.height(height);
                    }

                    BufferedImage bufferedImage = builder.asBufferedImage();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(bufferedImage, GCConstGlobals.GSAB_THUMBNAIL_DEFAULT_SUFFIX, baos);
                    fileDocument.setContent(baos.toByteArray());

                    return Optional.of(fileDocument);
                } else {
                    return Optional.empty();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return Optional.empty();
    }

    @Override
    public FileDocument getFileByMd5(String md5, String collectionName) {
        Query query = new Query().addCriteria(Criteria.where("md5").is(md5));
        FileDocument fileDocument = mongoTemplate.findOne(query, FileDocument.class, collectionName);
        return fileDocument;
    }

    @Override
    public List<FileDocument> listFilesByPage(int pageIndex, int pageSize, String collectionName) {
        Query query = new Query().with(Sort.by(Sort.Direction.DESC, "uploadDate"));
        long skip = (pageIndex - 1) * pageSize;
        query.skip(skip);
        query.limit(pageSize);
        Field field = query.fields();
        field.exclude("content");
        List<FileDocument> files = mongoTemplate.find(query, FileDocument.class, collectionName);
        return files;
    }
}
