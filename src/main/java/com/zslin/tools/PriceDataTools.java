package com.zslin.tools;

import com.alibaba.fastjson.JSON;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.model.Price;
import com.zslin.service.IPriceService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/12 15:43.
 */
@Component
public class PriceDataTools {

    @Autowired
    private IPriceService priceService;

    public void handler(JSONObject jsonObj) {
        Price price = priceService.findOne();
        if(price==null) {price = new Price();}
        Price p = JSON.toJavaObject(JSON.parseObject(jsonObj.toString()), Price.class);
        MyBeanUtils.copyProperties(p, price);
        priceService.save(price);

        SingleCaseTools.getInstance().setPrice(price); //存价格
    }
}
