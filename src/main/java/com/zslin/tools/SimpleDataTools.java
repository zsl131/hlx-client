package com.zslin.tools;

import com.alibaba.fastjson.JSON;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.model.AdminPhone;
import com.zslin.model.Price;
import com.zslin.model.Rules;
import com.zslin.service.IAdminPhoneService;
import com.zslin.service.IPriceService;
import com.zslin.service.IRulesService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/12 15:43.
 */
@Component
public class SimpleDataTools {

    @Autowired
    private IPriceService priceService;

    @Autowired
    private IRulesService rulesService;

    @Autowired
    private IAdminPhoneService adminPhoneService;

    public void handlerPrice(JSONObject jsonObj) {
        Price price = priceService.findOne();
        if(price==null) {price = new Price();}
        Price p = JSON.toJavaObject(JSON.parseObject(jsonObj.toString()), Price.class);
        MyBeanUtils.copyProperties(p, price);
        priceService.save(price);

        SingleCaseTools.getInstance().setPrice(price); //存价格
    }

    public void handlerRules(JSONObject jsonObj) {
        Rules rules = rulesService.loadOne();
        if(rules==null) {rules = new Rules();}
        Rules r = JSON.toJavaObject(JSON.parseObject(jsonObj.toString()), Rules.class);
        MyBeanUtils.copyProperties(r, rules);
        rulesService.save(rules);

        SingleCaseTools.getInstance().setRules(rules); //存规则
    }

    public void handlerAdminPhone(String action, Integer dataId, JSONObject jsonObj) {
        if("delete".equalsIgnoreCase(action)) {
            adminPhoneService.deleteByAccountId(dataId);
        } else if("update".equalsIgnoreCase(action)) {
            AdminPhone adminPhone = adminPhoneService.findByAccountId(dataId);
            if(adminPhone==null) {adminPhone = new AdminPhone();}
            AdminPhone ap = JSON.toJavaObject(JSON.parseObject(jsonObj.toString()), AdminPhone.class);
            MyBeanUtils.copyProperties(ap, adminPhone);
            adminPhoneService.save(adminPhone);
        }
    }
}
