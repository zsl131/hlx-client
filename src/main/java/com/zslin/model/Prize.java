package com.zslin.model;

import javax.persistence.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/23 11:58.
 * 礼物-还在库中未送出去的叫礼物，送出去的叫礼品
 */
@Entity
@Table(name = "t_prize")
public class Prize {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 服务器端数据ID */
    @Column(name = "data_id")
    private Integer dataId;

    /** 名称 */
    private String name;

    /** 图片 */
    @Column(name = "pic_path")
    private String picPath;

    /** 说明 */
    @Lob
    private String remark;

    /**
     * 类型
     * 1-积分
     * 2-抵价券
     * 3-早餐券
     * 4-晚餐券
     */
    private String type;

    /**
     * 状态，1-可以使用；0-不可用
     */
    private String status;

    /** 价值，如果是积分则是可抵积分数量；如果是抵价券，则是可抵现金金额，单位：分 */
    private Integer worth = 0;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDataId() {
        return dataId;
    }

    public void setDataId(Integer dataId) {
        this.dataId = dataId;
    }

    public Integer getWorth() {
        return worth;
    }

    public void setWorth(Integer worth) {
        this.worth = worth;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
