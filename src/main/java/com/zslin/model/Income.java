package com.zslin.model;

import javax.persistence.*;

/**
 * Created by zsl on 2018/7/1.
 * 营收
 */
@Entity
@Table(name = "t_income")
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 收入日期，格式yyyyMMdd */
    @Column(name = "come_day")
    private String comeDay;

    private Float money = 0f;

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getComeDay() {
        return comeDay;
    }

    public void setComeDay(String comeDay) {
        this.comeDay = comeDay;
    }
}
