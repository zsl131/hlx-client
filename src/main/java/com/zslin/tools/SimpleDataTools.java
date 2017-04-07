package com.zslin.tools;

import com.alibaba.fastjson.JSON;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.model.*;
import com.zslin.service.*;
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

    @Autowired
    private IMemberLevelService memberLevelService;

    @Autowired
    private IOrdersService ordersService;

    @Autowired
    private ICommodityService commodityService;

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

    public void handlerMemberLevel(String action, Integer dataId, JSONObject jsonObj){
        if("delete".equalsIgnoreCase(action)) {
            memberLevelService.delete(dataId);
        } else if("update".equalsIgnoreCase(action)) {
            MemberLevel memberLevel = memberLevelService.findOne(dataId);
            if(memberLevel==null) {memberLevel = new MemberLevel();}
            MemberLevel ml = JSON.toJavaObject(JSON.parseObject(jsonObj.toString()), MemberLevel.class);
            MyBeanUtils.copyProperties(ml, memberLevel);
            memberLevelService.save(memberLevel);
        }
    }

    public void handlerCommodity(String action, Integer dataId, JSONObject jsonObj) {
        Commodity c = JSON.toJavaObject(JSON.parseObject(jsonObj.toString()), Commodity.class);
        if("delete".equalsIgnoreCase(action)) {
            commodityService.deleteByNo(c.getNo());
        } else if("update".equalsIgnoreCase(action)) {
            Commodity commodity = commodityService.findOne(dataId);
            if(commodity==null) { commodity = new Commodity(); }
            MyBeanUtils.copyProperties(c, commodity);
            commodityService.save(commodity);
        }
    }

    public void handlerOrder(JSONObject jsonObj) {
        Orders o = JSON.toJavaObject(JSON.parseObject(jsonObj.toString()), Orders.class);
        String no = o.getNo();
        Orders orders = ordersService.findByNo(no);
        if(orders==null) {
            ordersService.save(o);
        } else {
            MyBeanUtils.copyProperties(o, orders, new String[]{"no"});
            ordersService.save(orders);
        }
    }
}
