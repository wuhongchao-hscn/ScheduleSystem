package com.hitachi.schedule.jpa.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Agree {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String userId;

    @Column
    private long articleId;

    @Column
    private boolean agree;

    @Column
    private String uId;

    @Column
    private Date uDate;
}
