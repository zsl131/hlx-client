package com.zslin.dto;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/17 11:27.
 * 交班盘点DTO对象
 */
public class InventoryDto {
    private Integer cashTotal = 0; //现金支付订单数
    private Integer otherTotal=0; //其他支付方式订单数
    private Float cashMoney = 0f; //现金金额
    private Float otherMoney = 0f; //其他支付金额
    private Float curBond = 0f; //当前压金总数
    private Float decuctBond=0f; //已经扣除的压金金额

    public Integer getCashTotal() {
        return cashTotal;
    }

    public void setCashTotal(Integer cashTotal) {
        this.cashTotal = cashTotal;
    }

    public Integer getOtherTotal() {
        return otherTotal;
    }

    public void setOtherTotal(Integer otherTotal) {
        this.otherTotal = otherTotal;
    }

    public Float getCashMoney() {
        return cashMoney;
    }

    public void setCashMoney(Float cashMoney) {
        this.cashMoney = cashMoney;
    }

    public Float getOtherMoney() {
        return otherMoney;
    }

    public void setOtherMoney(Float otherMoney) {
        this.otherMoney = otherMoney;
    }

    public Float getCurBond() {
        return curBond;
    }

    public void setCurBond(Float curBond) {
        this.curBond = curBond;
    }

    public Float getDecuctBond() {
        return decuctBond;
    }

    public void setDecuctBond(Float decuctBond) {
        this.decuctBond = decuctBond;
    }
}
