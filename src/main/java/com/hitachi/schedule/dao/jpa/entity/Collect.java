package com.hitachi.schedule.dao.jpa.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Collect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private long folderId;

    @Column
    private long articleId;

    @Column
    private Date updateDate;

    @Column
    private String updateId;
}
