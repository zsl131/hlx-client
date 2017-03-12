package com.zslin.model;

import javax.persistence.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/12 15:40.
 */
@Entity
@Table(name = "t_price")
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 早餐价格，单位：元 */
    @Column(name = "breakfast_price")
    private Float breakfastPrice;

    /** 晚餐价格，单位：元 */
    @Column(name = "dinner_price")
    private Float dinnerPrice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getBreakfastPrice() {
        return breakfastPrice;
    }

    public void setBreakfastPrice(Float breakfastPrice) {
        this.breakfastPrice = breakfastPrice;
    }

    public Float getDinnerPrice() {
        return dinnerPrice;
    }

    public void setDinnerPrice(Float dinnerPrice) {
        this.dinnerPrice = dinnerPrice;
    }
}
