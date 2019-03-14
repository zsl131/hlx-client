package com.zslin.tools;

import com.zslin.model.DiscountConfig;
import com.zslin.model.Price;
import com.zslin.model.Rules;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/12 15:44.
 * 单例工具类
 */
public class SingleCaseTools {

    private static SingleCaseTools instance;
    public static SingleCaseTools getInstance() {
        if(instance == null) {
            instance = new SingleCaseTools();
        }
        return instance;
    }

    private static Price price;

    private static Rules rules;

    private static DiscountConfig discountConfig;

    public void setDiscountConfig(DiscountConfig _config) {
        discountConfig = _config;
    }

    public DiscountConfig getDiscountConfig() {
        return discountConfig;
    }

    public void setPrice(Price _price) {
        price = _price;
    }

    public Price getPrice() {
        return price;
    }

    public void setRules(Rules _rules) {
        rules = _rules;
    }
    public Rules getRules() {
        return rules;
    }
}
