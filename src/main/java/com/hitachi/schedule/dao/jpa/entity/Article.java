package com.hitachi.schedule.dao.jpa.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long articleId;

    @Column
    private long articleTitleId;

    @Column
    private String articleContent;

    @Column
    private Date articleCreateDate;

    @Column
    private String articleCreateId;

    @Column
    private Date articleUpdateDate;

    @Column
    private String articleUpdateId;

    @Column
    private long articleAgree;
}
