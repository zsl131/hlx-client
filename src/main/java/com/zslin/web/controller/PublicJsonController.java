package com.zslin.web.controller;

import com.zslin.model.Price;
import com.zslin.service.IPriceService;
import com.zslin.tools.SingleCaseTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/12 15:54.
 */
@RestController
@RequestMapping(value = "public/json")
public class PublicJsonController {

    @Autowired
    private IPriceService priceService;

    @GetMapping(value = "getPrice")
    public Price getPirce() {
        Price p = SingleCaseTools.getInstance().getPrice();
        if(p==null) {
            p = priceService.findOne();
            SingleCaseTools.getInstance().setPrice(p);
        }
        return p;
    }
}
