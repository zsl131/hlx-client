package com.zslin.tools;

import com.alibaba.fastjson.JSON;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.meituan.model.MeituanConfig;
import com.zslin.meituan.model.MeituanShop;
import com.zslin.meituan.service.IMeituanConfigService;
import com.zslin.meituan.service.IMeituanShopService;
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

    @Autowired
    private IBuffetOrderService buffetOrderService;

    @Autowired
    private IPrizeService prizeService;

    @Autowired
    private IMemberService memberService;

    @Autowired
    private IMemberChargeService memberChargeService;

    @Autowired
    private IDiscountTimeService discountTimeService;

    @Autowired
    private IRestdayService restdayService;

    @Autowired
    private IDiscountDayService discountDayService;

    @Autowired
    private IDiscountConfigService discountConfigService;

    @Autowired
    private IWalletService walletService;

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

    public void handlerBuffetOrder(JSONObject jsonObj) {
        BuffetOrder o = JSON.toJavaObject(JSON.parseObject(jsonObj.toString()), BuffetOrder.class);
        String no = o.getNo();
        BuffetOrder orders = buffetOrderService.findByNo(no);
        if(orders==null) {
            buffetOrderService.save(o);
        } else {
            MyBeanUtils.copyProperties(o, orders, new String[]{"no", "id"}, true);
            buffetOrderService.save(orders);
        }
    }

    public void handlerPrize(String action, Integer dataId, JSONObject jsonObj) {
        if("delete".equalsIgnoreCase(action)) {
            prizeService.deleteByDataId(dataId);
        } else if("update".equalsIgnoreCase(action)) {
            Prize prize = prizeService.findByDataId(dataId);
            if(prize==null){
                prize = new Prize();
            }
            Prize p = JSON.toJavaObject(JSON.parseObject(jsonObj.toString()), Prize.class);
            MyBeanUtils.copyProperties(p, prize, new String[]{"id"});
            prize.setDataId(dataId);
            prizeService.save(prize);
        }
    }

    public void handlerWalletDetail(JSONObject jsonObj) {
        MemberCharge mc = new MemberCharge();
        Integer amount = jsonObj.getInt("amount");
        String phone = jsonObj.getString("phone");
        mc.setChargeMoney(amount*1f/100);
        try {
            mc.setName(jsonObj.getString("accountName"));
        } catch (Exception e) {
            mc.setName("");
        }
        mc.setCreateTime(jsonObj.getString("createTime"));
        mc.setCreateLong(jsonObj.getLong("createLong"));
        mc.setCreateDay(jsonObj.getString("createDay"));
        mc.setPhone(phone);
        try {
            mc.setNickname(jsonObj.getString("accountName"));
        } catch (Exception e) {
            mc.setNickname("");
        }
        mc.setStatus("1");

        Member m = memberService.findByPhone(phone);
        if(m!=null) {
            memberService.plusMoneyByPhone(amount, phone);
            mc.setBalance((m.getSurplus()+amount)/100);
        } else {
            m = new Member();
            m.setCreateTime(mc.getCreateTime());
            m.setSurplus(amount);
            m.setCreateLong(mc.getCreateLong());
            m.setPhone(phone);
            m.setCreateDay(mc.getCreateDay());
            m.setName(mc.getName());
            m.setCachierName("微信办理");
            memberService.save(m);
            mc.setBalance(amount);
        }
        memberChargeService.save(mc);
    }

    @Autowired
    private IMeituanConfigService meituanConfigService;

    public void handlerMeituanConfig(JSONObject jsonObj) {
        MeituanConfig config = JSON.toJavaObject(JSON.parseObject(jsonObj.toString()), MeituanConfig.class);
        MeituanConfig mc = meituanConfigService.loadOne();
        if(mc==null) {
            meituanConfigService.save(config);
        } else {
            MyBeanUtils.copyProperties(config, mc, new String[]{"id"});
            meituanConfigService.save(mc);
        }
    }

    @Autowired
    private IMeituanShopService meituanShopService;

    public void handlerMeituanShop(String action, JSONObject jsonObj) {
        MeituanShop shop = JSON.toJavaObject(JSON.parseObject(jsonObj.toString()), MeituanShop.class);
        if("update".equalsIgnoreCase(action)) {
            MeituanShop ms = meituanShopService.findByPoiId(shop.getPoiId());
            if(ms==null) {
                meituanShopService.save(shop);
            } else {
                MyBeanUtils.copyProperties(shop, ms, new String[]{"id"});
                meituanShopService.save(ms);
            }
        } else if("delete".equalsIgnoreCase(action)) {
            meituanShopService.deleteByPoiId(shop.getPoiId());
        }
    }

    public void handlerDiscountTime(JSONObject jsonObj, Integer dataId) {
        DiscountTime d = JSON.toJavaObject(JSON.parseObject(jsonObj.toString()), DiscountTime.class);
        DiscountTime dt = discountTimeService.findByObjId(dataId);
        if(dt==null) {
            d.setObjId(dataId);
            discountTimeService.save(d);
        } else {
            MyBeanUtils.copyProperties(d, dt, new String[]{"id", "objId"});
            discountTimeService.save(dt);
        }
    }

    public void handlerRestday(JSONObject jsonObj) {
        Restday restday = JSON.toJavaObject(JSON.parseObject(jsonObj.toString()), Restday.class);
        Restday day = restdayService.findByYearMonth(restday.getYearMonth());
        if(day==null) {
            restdayService.save(restday);
        } else {
            day.setDays(restday.getDays());
            restdayService.save(day);
        }
    }

    public void handlerDiscountDay(JSONObject jsonObj) {
        DiscountDay restday = JSON.toJavaObject(JSON.parseObject(jsonObj.toString()), DiscountDay.class);
        DiscountDay day = discountDayService.findByYearMonth(restday.getYearMonth());
        if(day==null) {
            discountDayService.save(restday);
        } else {
            day.setDays(restday.getDays());
            discountDayService.save(day);
        }
    }

    public void handlerDiscountConfig(JSONObject jsonObj) {
        DiscountConfig config = discountConfigService.loadOne();
        if(config==null) {config = new DiscountConfig();}
        DiscountConfig c = JSON.toJavaObject(JSON.parseObject(jsonObj.toString()), DiscountConfig.class);
        MyBeanUtils.copyProperties(c, config);
        discountConfigService.save(config);

        SingleCaseTools.getInstance().setDiscountConfig(config); //存折扣配置
    }

    public void handlerUpdatePassword(JSONObject jsonObj) {
        String phone = jsonObj.getString("key");
        String password = jsonObj.getString("value");

        memberService.updatePassword(password, phone); //修改密码
    }

    public void handlerUpdateOrderResult(JSONObject jsonObj) {
        String no = jsonObj.getString("key");
        String flag = jsonObj.getString("value");

        buffetOrderService.updateFinishFlag(flag, no); //修改订单完成标识
    }

    public void handlerWallet(JSONObject jsonObj) {
        Wallet wallet = JSON.toJavaObject(JSON.parseObject(jsonObj.toString()), Wallet.class);
        Wallet w = walletService.findByPhone(wallet.getPhone());
        if(w==null) {
            walletService.save(wallet);
        } else {
            MyBeanUtils.copyProperties(wallet, w, new String[]{"id"});
            walletService.save(w);
        }
    }
}
