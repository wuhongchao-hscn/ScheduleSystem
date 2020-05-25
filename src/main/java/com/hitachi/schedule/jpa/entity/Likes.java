package com.hitachi.schedule.jpa.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private long articleId;

    @Column
    private Date updateDate;

    @Column
    private String updateId;
}
