package com.zslin.web.controller;

import com.zslin.model.AdminPhone;
import com.zslin.model.Price;
import com.zslin.model.Rules;
import com.zslin.service.IAdminPhoneService;
import com.zslin.service.IPriceService;
import com.zslin.service.IRulesService;
import com.zslin.tools.SingleCaseTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @Autowired
    private IRulesService rulesService;

    @Autowired
    private IAdminPhoneService adminPhoneService;

    @GetMapping(value = "getPrice")
    public Price getPirce() {
        Price p = SingleCaseTools.getInstance().getPrice();
        if(p==null) {
            p = priceService.findOne();
            SingleCaseTools.getInstance().setPrice(p);
        }
        return p;
    }

    @GetMapping(value = "getRules")
    public Rules getRules() {
        Rules r = SingleCaseTools.getInstance().getRules();
        if(r==null) {
            r = rulesService.loadOne();
            SingleCaseTools.getInstance().setRules(r);
        }
        return r;
    }

    @PostMapping(value = "getAdminPhone")
    public AdminPhone getAdminPhone(String phone) {
        AdminPhone ap = adminPhoneService.findByPhone(phone);
        if(ap==null) {ap = new AdminPhone();}
        return ap;
    }
}
