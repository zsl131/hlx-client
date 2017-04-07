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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/1 22:58.
 * 收银台，新版本
 */
@Controller
@RequestMapping(value = "web/newOrders")
public class NewOrdersController {

    @Autowired
    private ICommodityService commodityService;

    @Autowired
    private IBuffetOrderDetailService buffetOrderDetailService;

    @Autowired
    private IBuffetOrderService buffetOrderService;

    @Autowired
    private WorkerCookieTools workerCookieTools;

    @Autowired
    private OrderNoTools orderNoTools;

    @Autowired
    private IRulesService rulesService;

    @Autowired
    private IPriceService priceService;

    @Autowired
    private IMemberService memberService;

    @Autowired
    private UploadFileTools uploadFileTools;

    @GetMapping(value = "index")
    public String index(Model model, String type, HttpServletRequest request) {
        type = type==null||"".equalsIgnoreCase(type)?"1":type;
        List<Commodity> commodityList ;
        if("1".equalsIgnoreCase(type)) {
            commodityList = commodityService.listByTicket();
            model.addAttribute("rules", rulesService.loadOne());
        } else {
            commodityList = commodityService.listByType("3");
        }
        model.addAttribute("commodityList", commodityList);
        model.addAttribute("type", type);
        return "web/newOrders/index";
    }

    @PostMapping(value = "addBuffetOrder")
    public @ResponseBody ResDto addBuffetOrder(String commodityNo, HttpServletRequest request) {
        try {
            Worker w = workerCookieTools.getWorker(request);
            if(w==null) {return new ResDto("-1", "未检测到收银员");} //未检测到收银员，刷新重新登陆
            List<BuffetOrderDetail> detailList = buildDetail(commodityNo);
            if(detailList==null || detailList.size()<=0) {return new ResDto("-2", "订单中无商品");}
            BuffetOrder order = new BuffetOrder();
            order.setCashierName(w.getName());
            order.setCashierPhone(w.getPhone());
            order.setNo(orderNoTools.getOrderNo("1")); //前台订单以1开头
            order.setCreateLong(System.currentTimeMillis());
            order.setCreateTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
            order.setCreateDay(NormalTools.curDate("yyyy-MM-dd"));
            order.setStatus("0"); //表示下单状态，未付款
            order.setType("1");
            order.setCommodityCount(detailList.size()); //订单中商品数量
            order.setTotalMoney(buildTotalMoney(detailList)); //订单总价
            buffetOrderService.save(order);

            addOrderDetail(detailList, order); //保存订单商品信息
            return new ResDto(order.getNo(), "下单成功"); //表示下单成功
        } catch (Exception e) {
            return new ResDto("-3", "订单错误");
        }
    }

    private void addOrderDetail(List<BuffetOrderDetail> detailList, BuffetOrder order) {
        for(BuffetOrderDetail bod : detailList) {
            bod.setOrderId(order.getId());
            bod.setOrderNo(order.getNo());
            buffetOrderDetailService.save(bod);
        }
    }

    //订单总价
    private Float buildTotalMoney(List<BuffetOrderDetail> detailList) {
        Float res = 0f;
        for(BuffetOrderDetail bod : detailList) {
            res += bod.getPrice();
        }
        return res;
    }

    private List<BuffetOrderDetail> buildDetail(String commodityNo) {
        List<BuffetOrderDetail> res = new ArrayList<>();
        if(commodityNo==null || "".equalsIgnoreCase(commodityNo) || commodityNo.indexOf(",")<0) {return res;}
        String [] nos = commodityNo.split(",");
        for(String no : nos) {
            if(no!=null && !"".equalsIgnoreCase(no) && !"0".equalsIgnoreCase(no)) {
                Commodity c = commodityService.findByNo(no);
                if(c!=null) {
                    BuffetOrderDetail bod = new BuffetOrderDetail();
                    bod.setPrice(c.getPrice());
                    bod.setCommodityId(c.getId());
                    bod.setCommodityName(c.getName());
                    bod.setCommodityNo(c.getNo());
                    bod.setCommodityType(c.getType());
                    bod.setCreateLong(System.currentTimeMillis());
                    bod.setCreateTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
                    bod.setCreateDay(NormalTools.curDate("yyyy-MM-dd"));
                    res.add(bod);
                }
            }
        }
        return res;
    }

    //订单支付页面
    @GetMapping(value = "payBuffetOrder")
    public String payBuffetOrder(Model model, String orderNo, HttpServletRequest request) {
        BuffetOrder order = buffetOrderService.findByNo(orderNo);
        String status = order.getStatus();
        if(!"0".equalsIgnoreCase(status) && !"1".equalsIgnoreCase(status) && !"6".equals(status)) {
            return "redirect:/web/newOrders/showOrder?orderNo="+orderNo;
        }
        model.addAttribute("order", order);
        model.addAttribute("commodityList", buffetOrderDetailService.listByOrderNo(orderNo));
        model.addAttribute("price", priceService.findOne());
        return "web/newOrders/payBuffetOrder";
    }

    //订单显示页面
    @GetMapping(value = "showOrder")
    public String showOrder(Model model, String orderNo, HttpServletRequest request) {
        BuffetOrder order = buffetOrderService.findByNo(orderNo);
        model.addAttribute("order", order);
        model.addAttribute("commodityList", buffetOrderDetailService.listByOrderNo(orderNo));
        model.addAttribute("price", priceService.findOne());
        model.addAttribute("rules", rulesService.loadOne());
        return "web/newOrders/showOrder";
    }

    /** 提交订单 */
    @PostMapping(value = "postOrder")
    public @ResponseBody ResDto postOrder(String no, Float bondMoney, Integer bondCount, String payType,
                                          String specialType, String reserve, HttpServletRequest request) {
        Worker w = workerCookieTools.getWorker(request);
        if(w==null) {return new ResDto("-1", "未检测到收银员");} //未检测到收银员，刷新重新登陆
        BuffetOrder order = buffetOrderService.findByNo(no);
        if(order==null) {return new ResDto("-2", "未检查到订单信息");}
        if("5".equalsIgnoreCase(specialType)) { //如果是会员订单
            Member m = memberService.findByPhone(reserve);
            if(m==null) {return new ResDto("-3", "未检测到会员信息");}
            order.setSurplusBond(bondMoney*bondCount); //剩余压金金额
            order.setType(specialType); //订单类型
            order.setPayType(payType);
            Float memberSurplus = m.getSurplus()*1.0f/100;
            Float curSurplus = memberSurplus - order.getTotalMoney(); //消费后剩余
            order.setDiscountMoney(curSurplus>=0?order.getTotalMoney():memberSurplus);
            order.setDiscountReason(reserve);
            order.setDiscountType("5");
            order.setEntryLong(System.currentTimeMillis());
            order.setEntryTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
            order.setStatus("2"); //就餐中……
            memberService.plusMoneyByPhone(0-(int)(order.getDiscountMoney()*100), reserve);
            //TODO 服务器端需要自行对会员账户进行消费处理
            //TODO 需要打印小票
        } else if("1".equals(specialType)) { //普通订单
            order.setSurplusBond(bondMoney*bondCount); //剩余压金金额
            order.setType(specialType); //订单类型
            order.setPayType(payType);
            order.setDiscountType("0");
            order.setEntryLong(System.currentTimeMillis());
            order.setEntryTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
            order.setStatus("2"); //就餐中……
            //TODO 需要打印小票
        }
        buffetOrderService.save(order);

        sendBuffetOrder2Server(order); //发送数据到服务器
        sendBuffetOrderDetail2Server(order.getNo());
        return new ResDto("1", "操作成功");
    }

    //退票
    @PostMapping(value = "retreatOrders")
    public @ResponseBody
    ResDto retreatOrders(String no, String reason, HttpServletRequest request) {
        Worker w = workerCookieTools.getWorker(request);
        if(w==null) {return new ResDto("-10", "无权操作");}
        Rules rules = rulesService.loadOne(); //先获取规则
        BuffetOrder orders = buffetOrderService.findByNo(no);
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
                buffetOrderService.save(orders);

                //TODO 需要将结果发送至服务器
                sendBuffetOrder2Server(orders);
                return new ResDto("0", "退票完成");
            }
        }
    }

    //发送友情价订单到服务器
    @PostMapping(value = "sendFriendOrder")
    public @ResponseBody ResDto sendFriendOrder(String no, String bossPhone, HttpServletRequest request) {
        Worker w = workerCookieTools.getWorker(request);
        if(w==null) {return new ResDto("-10", "无权操作");}
        BuffetOrder orders = buffetOrderService.findByNo(no);
        if(orders==null) {
            return new ResDto("-1", "订单不存在"); //订单不存在
        } else {
            if(!"0".equals(orders.getStatus())) {
                return new ResDto("-2", "当前状态不可再进行亲情折扣");
            }
            orders.setType("4");
            Float totalMoney = buildFriendMoney(no);
            Float discountMoney = orders.getTotalMoney()-totalMoney;
            orders.setTotalMoney(totalMoney);
            orders.setDiscountMoney(discountMoney);
            orders.setDiscountReason(bossPhone);
            orders.setDiscountType("2");
            buffetOrderService.save(orders);
            sendBuffetOrder2Server(orders);
            return new ResDto("0", "提交成功等待确认");
        }
    }

    private Float buildFriendMoney(String no) {
        List<BuffetOrderDetail> list = buffetOrderDetailService.listByOrderNo(no);
        Price price = priceService.findOne();
        Float result = 0f;
        for(BuffetOrderDetail d : list) {
            if("88888".equals(d.getCommodityNo())) { //如果是午餐券
                result += price.getFriendBreakfastPrice();
            } else if("99999".equals(d.getCommodityNo())) { //如果是晚餐券
                result += price.getFriendDinnerPrice();
            } else {
                result += d.getPrice();
            }
        }
        return result;
    }

    @PostMapping(value = "cancelOrders")
    public @ResponseBody ResDto cancelOrders(String no, String reason, HttpServletRequest request) {
        Worker w = workerCookieTools.getWorker(request);
        if(w==null) {return new ResDto("-10", "无权操作");}
        BuffetOrder orders = buffetOrderService.findByNo(no);
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
                buffetOrderService.save(orders);

                //TODO 需要将结果发送至服务器
                sendBuffetOrder2Server(orders);
                return new ResDto("0", "取消完成");
            }
        }
    }

    @GetMapping(value = "returnBond")
    public String returnBond(Model model, String no, HttpServletRequest request) {
        if(no!=null && !"".equalsIgnoreCase(no)) {
            model.addAttribute("order", buffetOrderService.findByNo(no));
        }
        return "web/newOrders/returnBond";
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
        BuffetOrder orders = buffetOrderService.findByNo(no);
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

        buffetOrderService.save(orders);

        sendBuffetOrder2Server(orders);
        return new ResDto("0", "操作完成！");
    }

    public void sendBuffetOrder2Server(BuffetOrder order) {
        String content = UploadJsonTools.buildDataJson(UploadJsonTools.buildBuffetOrder(order));
        uploadFileTools.setChangeContext(content, true);
    }

    public void sendBuffetOrderDetail2Server(String no) {
        List<BuffetOrderDetail> list = buffetOrderDetailService.listByOrderNo(no);
        for(BuffetOrderDetail detail : list) {
            String content = UploadJsonTools.buildDataJson(UploadJsonTools.buildBuffetOrderDetail(detail));
            uploadFileTools.setChangeContext(content, true);
        }
    }
}