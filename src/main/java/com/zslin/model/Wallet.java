package com.zslin.model;

import javax.persistence.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/23 14:35.
 * 钱包
 */
@Entity
@Table(name = "t_wallet")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String openid;

    /** 剩余积分 */
    private Integer score;

    /** 支付密码 */
    private String password;

    /** 手机号码 */
    private String phone;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
