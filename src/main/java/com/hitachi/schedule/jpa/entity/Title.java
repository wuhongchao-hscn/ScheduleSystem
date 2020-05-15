package com.hitachi.schedule.jpa.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class Title {

    @Id
    private long titleId;

    @Column
    private String titleName;

    @Column
    private String titleContent;

    @Column
    private String uId;

    @Column
    private Date uDate;
}
