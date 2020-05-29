package com.hitachi.schedule.dao.jpa.dao;

import com.hitachi.schedule.dao.jpa.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface FolderDao extends JpaRepository<Folder, Long> {
    List<Folder> findByUpdateIdOrderByUpdateDateDesc(String userId);
}
