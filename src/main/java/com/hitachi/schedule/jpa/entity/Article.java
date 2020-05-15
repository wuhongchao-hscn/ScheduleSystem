package com.hitachi.schedule.jpa.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class Article {

    @Id
    @GeneratedValue
    private long articleId;

    @Column
    private long articleTitleId;

    @Column
    private String articleContent;

    @Column
    private Date articleCreateDate;

    @Column
    private String articleCreateUid;

    @Column
    private Date articleUpdateDate;

    @Column
    private String articleUpdateUid;

    @Column
    private long articleAgree;
}
