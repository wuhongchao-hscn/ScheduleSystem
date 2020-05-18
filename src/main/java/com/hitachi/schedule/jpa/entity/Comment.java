package com.hitachi.schedule.jpa.entity;

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
