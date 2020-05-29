package com.hitachi.schedule.dao.jpa.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private long articleId;

    @Column
    private Long parentId;

    @Column
    private String comment;

    @Column
    private String updateId;

    @Column
    private Date updateDate;

    @Column
    private long agree;

    @Column
    private long level;
}
