package com.zslin.model;

import javax.persistence.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/14 14:23.
 * 管理人员手机号码（股东的）
 */
@Entity
@Table(name = "t_admin_phone")
public class AdminPhone {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    //联系电话
    private String phone;
    //姓名，可能为空
    private String name;
    //openid
    private String openid;
    //用户Id
    @Column(name = "account_id")
    private Integer accountId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }
}
