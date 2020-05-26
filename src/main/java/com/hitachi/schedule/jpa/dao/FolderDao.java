package com.hitachi.schedule.jpa.dao;

import com.hitachi.schedule.jpa.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface FolderDao extends JpaRepository<Folder, Long> {
    List<Folder> findByUpdateId(String userId);
}
