package com.zslin.web.controller;

import com.zslin.basic.tools.NormalTools;
import com.zslin.dto.ResDto;
import com.zslin.model.*;
import com.zslin.service.*;
import com.zslin.tools.OrderNoTools;
import com.zslin.tools.OrdersOperateTools;
import com.zslin.tools.WorkerCookieTools;
import com.zslin.upload.tools.UploadFileTools;
import com.zslin.upload.tools.UploadJsonTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/13 17:25.
 */
@Controller
@RequestMapping(value = "web/orders")
public class OrdersController {

    @Autowired
    private IOrderNoService orderNoService;

    @Autowired
    private IOrdersService ordersService;

    @Autowired
    private IRulesService rulesService;

    @Autowired
    private OrderNoTools orderNoTools;

    @Autowired
    private WorkerCookieTools workerCookieTools;

    @Autowired
    private UploadFileTools uploadFileTools;

    @Autowired
    private IMemberService memberService;

    @Autowired
    private IMemberChargeService memberChargeService;

    @Autowired
    private IPriceService priceService;

    @GetMapping(value = "add")
    public String add(Model model, HttpServletRequest request) {
        model.addAttribute("rules", rulesService.loadOne());
        model.addAttribute("price", priceService.findOne());
        return "web/orders/add";
    }

    @GetMapping(value = "show")
    public String show(Model model, String no, HttpServletRequest request) {
        Orders orders = ordersService.findByNo(no);
        model.addAttribute("orders", orders);
        model.addAttribute("ordersType", orders.getType());
        model.addAttribute("rules", rulesService.loadOne());
        model.addAttribute("price", priceService.findOne());
        return "web/orders/show";
    }

    @PostMapping(value = "addInStoreOrder")
    public @ResponseBody String addInStoreOrder(Integer peopleCount, Integer halfCount, Integer childCount, String payType,
            String level, Float price, Float bondMoney, HttpServletRequest request) {
        Worker w = workerCookieTools.getWorker(request);
        if(w==null) {return "-1";} //未检测到收银员，刷新重新登陆

        try {
            Orders orders = new Orders();
            orders.setCashierName(w.getName());
            orders.setCashierPhone(w.getPhone());
            orders.setChildCount(childCount);
            orders.setHalfCount(halfCount);
            orders.setNo(orderNoTools.getOrderNo("1")); //店内订单以1开头
            orders.setCreateLong(System.currentTimeMillis());
            orders.setCreateTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
            orders.setCreateDay(NormalTools.curDate("yyyy-MM-dd"));
            orders.setEntryTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
            orders.setEntryLong(System.currentTimeMillis());
            orders.setBondMoney(bondMoney);
            orders.setLevel(level);
            orders.setStatus("2"); //店内订单一下单就表示已收款可以进场就餐
            orders.setPayType(payType);
            orders.setType("1"); //1为店内下单
            orders.setTotalMoney(peopleCount*price+(halfCount*price*0.5f)); //总金额为全票+半票
            orders.setPeopleCount(peopleCount);
            orders.setSurplusBond(0f);
            orders.setPrice(price);
            orders.setDiscountType("0"); //0表示无优惠
            ordersService.save(orders);

            //TODO 此时应该打印小票
            //TODO 提交到服务端
            sendOrders2Server(orders);
            return "1"; //表示下单成功
        } catch (Exception e) {
            e.printStackTrace();
            return "-2"; //有异常
        }
    }

    //添加会员订单
    @PostMapping(value = "addMemberOrder")
    public @ResponseBody ResDto addMemberOrder(Integer peopleCount, Integer halfCount, Integer childCount, String payType,
                                                String level, Float price, String phone, Float bondMoney, HttpServletRequest request) {
        Worker w = workerCookieTools.getWorker(request);
        if(w==null) {return new ResDto("-1", "未检测到收银员");} //未检测到收银员，刷新重新登陆

        try {
            Member m = memberService.findByPhone(phone);
            if(m==null) {return new ResDto("-2", "会员不存在");}
            Orders orders = new Orders();
            Float totalMoney = peopleCount*price+(halfCount*price*0.5f);
            orders.setCashierName(w.getName());
            orders.setCashierPhone(w.getPhone());
            orders.setChildCount(childCount);
            orders.setHalfCount(halfCount);
            orders.setNo(orderNoTools.getOrderNo("5")); //会员订单以1开头
            orders.setCreateLong(System.currentTimeMillis());
            orders.setCreateTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
            orders.setCreateDay(NormalTools.curDate("yyyy-MM-dd"));
            orders.setEntryTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
            orders.setEntryLong(System.currentTimeMillis());
            orders.setBondMoney(bondMoney);
            orders.setLevel(level);
            orders.setStatus("2"); //店内订单一下单就表示已收款可以进场就餐
            orders.setPayType(payType);
            orders.setType("5"); //5为会员下单
            orders.setTotalMoney(totalMoney); //总金额为全票+半票
            orders.setPeopleCount(peopleCount);
            orders.setSurplusBond(0f);
            orders.setPhone(phone);
            orders.setPrice(price);
            if(m.getSurplus()>=totalMoney*100) { //余额足
                orders.setDiscountMoney(totalMoney);
                memberService.plusMoneyByPhone(0-(int)(totalMoney*100), phone);
            } else { //余额不足
                orders.setDiscountMoney((m.getSurplus()*1.0f)/100);
                memberService.plusMoneyByPhone(0-m.getSurplus(), phone);
            }
            orders.setDiscountType("0"); //0表示无优惠
            ordersService.save(orders);

            //TODO 此时应该打印小票
            //TODO 提交到服务端
            sendOrders2Server(orders);
            buildMemberCharge(w, orders);
            return new ResDto(orders.getNo(), "下单成功"); //表示下单成功
        } catch (Exception e) {
            e.printStackTrace();
//            return "-2"; //有异常
            return new ResDto("-2", "出现异常，下单失败");
        }
    }

    private void buildMemberCharge(Worker w, Orders orders) {
        MemberCharge mc = new MemberCharge();
        mc.setPhone(orders.getPhone());
        mc.setStatus("1");
        mc.setName(orders.getName());
        mc.setLevel(0);
        mc.setChargeMoney(0f-orders.getDiscountMoney());
        mc.setCreateDay(NormalTools.curDate("yyyy-MM-dd"));
        mc.setCreateLong(System.currentTimeMillis());
        mc.setCreateTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
        mc.setVerifyAccountName(w.getName());
        mc.setVerifyAccountPhone(w.getPhone());
        mc.setVerifyAccountOpenid(w.getOpenid());
        memberChargeService.save(mc);
        sendMemberCharge2Server(mc); //推送到服务器
    }

    @PostMapping(value = "addFriendDiscountOrder")
    public @ResponseBody ResDto addFriendDiscountOrder(String phone, Integer peopleCount, Integer halfCount,
           Integer childCount, Float price, String level, Float bondMoney, HttpServletRequest request) {
        try {
            Worker w = workerCookieTools.getWorker(request);
            if(w==null) {return new ResDto("-1", "未检测到收银员");} //未检测到收银员，刷新重新登陆
//            Rules rules = rulesService.loadOne();
            Price p = priceService.findOne();
            Float friendPrice = "1".equals(level)?p.getFriendDinnerPrice():p.getFriendBreakfastPrice();
            Orders orders = new Orders();
            String no = orderNoTools.getOrderNo("4");
            orders.setCashierName(w.getName());
            orders.setCashierPhone(w.getPhone());
            orders.setChildCount(childCount);
            orders.setHalfCount(halfCount);
            orders.setNo(no); //友情价订单以4开头
            orders.setCreateLong(System.currentTimeMillis());
            orders.setBondMoney(bondMoney);
            orders.setCreateTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
            orders.setCreateDay(NormalTools.curDate("yyyy-MM-dd"));
//        orders.setEntryTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
//        orders.setEntryLong(System.currentTimeMillis());
//        orders.setBondMoney(bondMoney);
            orders.setLevel(level);
            orders.setStatus("0"); //友情价下单，先下单待确认后再进场就餐
//        orders.setPayType(payType);
            orders.setType("4"); //4为友情价下单
            //目前只对全票打折
//            orders.setTotalMoney(peopleCount*price*(rules.getFriendPercent()*1.0f/100)+(halfCount*price*0.5f)); //总金额为全票+半票
//            orders.setDiscountMoney(peopleCount*price*(1-rules.getFriendPercent()*1.0f/100)); //优惠金额
            orders.setTotalMoney(peopleCount*friendPrice+(halfCount*price*0.5f)); //总金额为全票+半票
            orders.setDiscountMoney(peopleCount*price-peopleCount*friendPrice); //优惠金额
            orders.setPeopleCount(peopleCount);
            orders.setSurplusBond(0f);
            orders.setDiscountReason(phone);
            orders.setPrice(price);
//        orders.setDiscountType("4"); //0表示无优惠
            ordersService.save(orders);

            //TODO 推送到服务器
            sendOrders2Server(orders);
            return new ResDto(no, "下单成功，等待老板确认");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResDto("-2", "操作失败");
        }
    }

    //获取单个订单信息，主要用于检测老板有无确认友情价订单
    @PostMapping(value = "queryOrder")
    public @ResponseBody Orders queryOrder(String no) {
        return ordersService.findByNo(no);
    }

    @PostMapping(value = "confirmOrder")
    public @ResponseBody ResDto confirmOrder(String no, String payType, HttpServletRequest request) {
        Worker w = workerCookieTools.getWorker(request);
        if(w==null) {return new ResDto("-1", "未检测到收银员");} //未检测到收银员，刷新重新登陆
        Orders orders = ordersService.findByNo(no);
        if(orders==null) {
            return new ResDto("-2", "订单不存在");
        }
        try {
            orders.setCashierName(w.getName());
            orders.setCashierPhone(w.getPhone());
            orders.setEntryTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
            orders.setEntryLong(System.currentTimeMillis());
            orders.setStatus("2"); //确认后即可收费入场就餐
            orders.setPayType(payType);
            orders.setSurplusBond(0f);
            ordersService.save(orders);

            //TODO 推送到服务器
            sendOrders2Server(orders);
            //TODO 打印小票
            return new ResDto("0", "确认完成，请顾客带票入场");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResDto("-3", "操作失败");
        }
    }

    //退票
    @PostMapping(value = "retreatOrders")
    public @ResponseBody
    ResDto retreatOrders(String no, String reason, HttpServletRequest request) {
        Worker w = workerCookieTools.getWorker(request);
        if(w==null) {return new ResDto("-10", "无权操作");}
        Rules rules = rulesService.loadOne(); //先获取规则
        Orders orders = ordersService.findByNo(no);
        if(orders==null) {
            return new ResDto("-1", "订单不存在"); //订单不存在
        } else {
            if(orders.getEndTime()!=null) {return new ResDto("-2", "订单已完结，不可退票");}
            else if(!"1".equalsIgnoreCase(orders.getType()) && !"4".equalsIgnoreCase(orders.getType())) {
                return new ResDto("-3", "此订单类型不可退票");
            } else if(!OrdersOperateTools.canRetreat(rules.getRefundMin(), orders.getEntryLong())) {
                return new ResDto("-4", "已超过退票时间");
            } else if(!"2".equalsIgnoreCase(orders.getStatus())) {
                return new ResDto("-5", "只能在就餐状态才可退票");
            } else {
                orders.setStatus("-1");
                orders.setRetreatName(w.getName());
                orders.setRetreatReason(reason);
                orders.setEndLong(System.currentTimeMillis());
                orders.setEndTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
                orders.setRetreatTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
                ordersService.save(orders);

                //TODO 需要将结果发送至服务器
                sendOrders2Server(orders);
                return new ResDto("0", "退票完成");
            }
        }
    }

    @PostMapping(value = "cancelOrders")
    public @ResponseBody ResDto cancelOrders(String no, String reason, HttpServletRequest request) {
        Worker w = workerCookieTools.getWorker(request);
        if(w==null) {return new ResDto("-10", "无权操作");}
        Orders orders = ordersService.findByNo(no);
        if(orders==null) {
            return new ResDto("-1", "订单不存在"); //订单不存在
        } else { //在就餐前都可取消
            if(!"0".equalsIgnoreCase(orders.getStatus()) && !"6".equalsIgnoreCase(orders.getStatus())) {
                return new ResDto("-2", "当前状态不允许取消");
            } else if(!"4".equalsIgnoreCase(orders.getType())) {
                return new ResDto("-3", "此类型的订单不允许取消");
            } else {
                orders.setStatus("-2");
                orders.setRetreatName(w.getName());
                orders.setRetreatReason(reason);
                orders.setEndLong(System.currentTimeMillis());
                orders.setEndTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
                orders.setRetreatTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
                ordersService.save(orders);

                //TODO 需要将结果发送至服务器
                sendOrders2Server(orders);
                return new ResDto("0", "取消完成");
            }
        }
    }

    @GetMapping(value = "returnBond")
    public String returnBond(Model model, String no, HttpServletRequest request) {
        if(no!=null && !"".equalsIgnoreCase(no)) {
            model.addAttribute("orders", ordersService.findByNo(no));
        }
        return "web/orders/returnBond";
    }

    /**
     * 提交退还压金
     * @param no 订单编号
     * @param money 扣除压金金额，一般为0
     * @param request
     * @return
     */
    @PostMapping(value = "returnBond")
    public @ResponseBody ResDto returnBond(String no, Float money, HttpServletRequest request) {
        Worker w = workerCookieTools.getWorker(request);
        if(w==null) {
            return new ResDto("-1", "未检测到收银员");
        }
        Orders orders = ordersService.findByNo(no);
        if(orders==null) {return new ResDto("-2", "订单不存在");}
        if(!"2".equalsIgnoreCase(orders.getStatus()) && !"3".equalsIgnoreCase(orders.getStatus())) { //只有在就餐状态才可退压金
            return new ResDto("-3", "只有在就餐状态才可退压金");
        }
        if(money>0) { //有扣压金
            orders.setStatus("5");
        } else {
            orders.setStatus("4");
        }
        orders.setSurplusBond(money); //扣下来的压金
        orders.setEndLong(System.currentTimeMillis());
        orders.setEndTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));

        ordersService.save(orders);

        sendOrders2Server(orders);

        return new ResDto("0", "操作完成！");
    }

    private void sendOrders2Server(Orders orders) {
        String content = UploadJsonTools.buildDataJson(UploadJsonTools.buildOrderJson(orders));
        uploadFileTools.setChangeContext(content, true);
    }

    private void sendMemberCharge2Server(MemberCharge mc) {
        String content = UploadJsonTools.buildDataJson(UploadJsonTools.buildMemberChargeJson(mc));
        uploadFileTools.setChangeContext(content, true);
    }
}
