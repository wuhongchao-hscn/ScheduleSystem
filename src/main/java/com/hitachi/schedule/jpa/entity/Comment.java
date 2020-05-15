package com.hitachi.schedule.jpa.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class Comment {

    @Id
    private long articleId;

    @Column
    private long parentId;

    @Column
    private String comment;

    @Column
    private String uId;

    @Column
    private Date uDate;

    @Column
    private long agree;

    @Column
    private long level;
}
