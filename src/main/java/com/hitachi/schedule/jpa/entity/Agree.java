package com.hitachi.schedule.jpa.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class Agree {

    @Id
    @GeneratedValue
    private long agreeId;

    @Column
    private long userId;

    @Column
    private long articleId;

    @Column
    private boolean agree;

    @Column
    private String uId;

    @Column
    private Date uDate;
}
