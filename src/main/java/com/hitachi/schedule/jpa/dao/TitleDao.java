package com.hitachi.schedule.jpa.dao;

import com.hitachi.schedule.jpa.entity.Title;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TitleDao extends JpaRepository<Title, Long> {

    Title findByTitleId(long titleId);


}
