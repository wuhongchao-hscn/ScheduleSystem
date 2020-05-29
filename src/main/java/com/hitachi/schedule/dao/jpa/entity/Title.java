package com.hitachi.schedule.dao.jpa.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Title {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long titleId;

    @Column
    private String titleName;

    @Column
    private String titleContent;

    @Column
    private String updateId;

    @Column
    private Date updateDate;
}
