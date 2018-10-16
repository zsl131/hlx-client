package com.zslin.card.model;

import javax.persistence.*;

/**
 * 卡券申请原因
 * Created by zsl on 2018/10/12.
 */
@Entity
@Table(name = "c_apply_reason")
public class ApplyReason {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 原因 */
    private String name;

    /** 服务端的ID */
    @Column(name = "obj_id")
    private Integer objId;

    public Integer getObjId() {
        return objId;
    }

    public void setObjId(Integer objId) {
        this.objId = objId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
