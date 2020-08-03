package com.hitachi.schedule.dao.jpa.dao;

import com.hitachi.schedule.dao.jpa.entity.Title;
import com.hitachi.schedule.dao.jpa.param.TitleIdAndName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;


public interface TitleDao extends JpaRepository<Title, Long> {

    Title findByTitleId(long titleId);

    @Query(value = "SELECT titleId, titleName FROM Title", nativeQuery = true)
    Collection<TitleIdAndName> findAllTitleIdAndTitleName();
}
