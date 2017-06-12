package com.zslin.web.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.tools.NormalTools;
import com.zslin.model.BuffetOrder;
import com.zslin.model.Prize;
import com.zslin.service.*;
import com.zslin.web.dto.MyTicketDto;
import com.zslin.web.dto.MyTimeDto;
import com.zslin.web.tools.MyTicketTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by 钟述林 393156105@qq.com on 2017/6/7 0:48.
 */
@Controller
@RequestMapping(value = "web/newOrders")
@AdminAuth(name = "订单管理", psn = "订单管理", orderNum = 10, porderNum = 1, pentity = 0, icon = "fa fa-list")
public class WebNewOrdersController {

    @Autowired
    private IBuffetOrderService buffetOrderService;

    @Autowired
    private IBuffetOrderDetailService buffetOrderDetailService;

    @Autowired
    private ICommodityService commodityService;

    @Autowired
    private IRulesService rulesService;

    @Autowired
    private IPrizeService prizeService;

    @GetMapping(value = "cal")
    public String cal(Model model, String day, HttpServletRequest request) {
        if(day==null) {
            day = NormalTools.curDate("yyyy-MM-dd"); //默认为当天
        } else {
            day = day.replace("eq-", "");
        }

        MyTimeDto mtd = new MyTimeDto(day);

        queryCount(day, model);
        queryTotalMoney(mtd, model);
        model.addAttribute("day", day);
        Float bondMoneyAM = buffetOrderService.queryBondMoney(mtd.getStartTimeAM(), mtd.getEndTimeAM());
        model.addAttribute("bondMoneyAM", bondMoneyAM==null?0:bondMoneyAM); //押金
        Float bondMoneyPM = buffetOrderService.queryBondMoney(mtd.getStartTimePM(), mtd.getEndTimePM());
        model.addAttribute("bondMoneyPM", bondMoneyPM==null?0:bondMoneyPM); //押金

        Float marketMoneyAM = buffetOrderService.queryTotalMoneyByPayType(mtd.getStartTimeAM(), mtd.getEndTimeAM(), "5");
        model.addAttribute("marketMoneyAM", marketMoneyAM==null?0:marketMoneyAM); //商场签单
        Float marketMoneyPM = buffetOrderService.queryTotalMoneyByPayType(mtd.getStartTimePM(), mtd.getEndTimePM(), "5");
        model.addAttribute("marketMoneyPM", marketMoneyPM==null?0:marketMoneyPM); //商场签单

        Float meituanMoneyAM = buffetOrderService.queryMoneyByMeiTuan(mtd.getStartTimeAM(), mtd.getEndTimeAM());
        model.addAttribute("meituanMoneyAM", meituanMoneyAM==null?0:meituanMoneyAM); //美团
        Float meituanMoneyPM = buffetOrderService.queryMoneyByMeiTuan(mtd.getStartTimePM(), mtd.getEndTimePM());
        model.addAttribute("meituanMoneyPM", meituanMoneyPM==null?0:meituanMoneyPM); //美团

        Float ticketMoneyAM = buffetOrderService.queryMoneyByTicket(mtd.getStartTimeAM(), mtd.getEndTimeAM());
        model.addAttribute("ticketMoneyAM", ticketMoneyAM==null?0:ticketMoneyAM); //卡券
        Float ticketMoneyPM = buffetOrderService.queryMoneyByTicket(mtd.getStartTimePM(), mtd.getEndTimePM());
        model.addAttribute("ticketMoneyPM", ticketMoneyPM==null?0:ticketMoneyPM); //卡券

        Float weixinMoneyAM = buffetOrderService.queryTotalMoneyByPayType(mtd.getStartTimeAM(), mtd.getEndTimeAM(), "3");
        model.addAttribute("weixinMoneyAM", weixinMoneyAM==null?0:weixinMoneyAM); //微信支付
        Float weixinMoneyPM = buffetOrderService.queryTotalMoneyByPayType(mtd.getStartTimePM(), mtd.getEndTimePM(), "3");
        model.addAttribute("weixinMoneyPM", weixinMoneyPM==null?0:weixinMoneyPM); //微信支付

        Float alipayMoneyAM = buffetOrderService.queryTotalMoneyByPayType(mtd.getStartTimeAM(), mtd.getEndTimeAM(), "4");
        model.addAttribute("alipayMoneyAM", alipayMoneyAM==null?0:alipayMoneyAM); //支付宝
        Float alipayMoneyPM = buffetOrderService.queryTotalMoneyByPayType(mtd.getStartTimePM(), mtd.getEndTimePM(), "4");
        model.addAttribute("alipayMoneyPM", alipayMoneyPM==null?0:alipayMoneyPM); //支付宝

        Float cashMoneyAM = buffetOrderService.queryTotalMoneyByPayType(mtd.getStartTimeAM(), mtd.getEndTimeAM(), "1");
        model.addAttribute("cashMoneyAM", cashMoneyAM==null?0:cashMoneyAM); //现金
        Float cashMoneyPM = buffetOrderService.queryTotalMoneyByPayType(mtd.getStartTimePM(), mtd.getEndTimePM(), "1");
        model.addAttribute("cashMoneyPM", cashMoneyPM==null?0:cashMoneyPM); //现金

        Float cardMoneyAM = buffetOrderService.queryTotalMoneyByPayType(mtd.getStartTimeAM(), mtd.getEndTimeAM(), "2");
        model.addAttribute("cardMoneyAM", cardMoneyAM==null?0:cardMoneyAM); //刷卡
        Float cardMoneyPM = buffetOrderService.queryTotalMoneyByPayType(mtd.getStartTimePM(), mtd.getEndTimePM(), "2");
        model.addAttribute("cardMoneyPM", cardMoneyPM==null?0:cardMoneyPM); //刷卡

        calTicket(mtd, model);
        calMeituan(mtd, model);
        return "web/newOrders/cal";
    }

    private void calMeituan(MyTimeDto mtd, Model model) {
        List<BuffetOrder> list = buffetOrderService.findByMeiTuan(mtd.getStartTimeAM(), mtd.getEndTimeAM()); //上午
        model.addAttribute("meituanAmountAM", buildMeituanAmount(list));

        List<BuffetOrder> listPM = buffetOrderService.findByMeiTuan(mtd.getStartTimePM(), mtd.getEndTimePM()); //下午
        model.addAttribute("meituanAmountPM", buildMeituanAmount(listPM));
    }

    private Integer buildMeituanAmount(List<BuffetOrder> list) {
        Integer res = 0;
        for(BuffetOrder order : list) {
            String datas = order.getDiscountReason();
            String [] array = datas.split(",");
            for(String d : array) {
                if(d!=null && d.length()==12) {
                    res ++;
                }
            }
        }
        return res;
    }

    private void calTicket(MyTimeDto mtd, Model model) {
        List<BuffetOrder> list = buffetOrderService.findByTicket(mtd.getStartTimeAM(), mtd.getEndTimeAM()); //上午
        model.addAttribute("ticketAmountAM", buildTicketAmount(list));

        List<BuffetOrder> listPM = buffetOrderService.findByTicket(mtd.getStartTimePM(), mtd.getEndTimePM()); //下午
        model.addAttribute("ticketAmountPM", buildTicketAmount(listPM));
    }

    private Map<MyTicketDto, Integer> buildTicketAmount(List<BuffetOrder> list) {
        MyTicketTools mtt = new MyTicketTools();
        for(BuffetOrder order : list) {
            String [] array = order.getDiscountReason().split("_");
            for(String single : array) {
                if(single!=null && single.indexOf(":")>0) {
                    String [] s_a = single.split(":");
                    Integer dataId = Integer.parseInt(s_a[0]); //Prize的id
                    Integer amount = Integer.parseInt(s_a[1]); //对应数量
                    Prize prize = prizeService.findOne(dataId);
                    mtt.add(prize.getId(), prize.getName(), amount);
                }
            }
        }
        return mtt.getDatas();
    }

    private void queryTotalMoney(MyTimeDto mtd, Model model) {
        Float totalAM = buffetOrderService.queryTotalMoneyByDay(mtd.getStartTimeAM(), mtd.getEndTimeAM());
        Float totalPM = buffetOrderService.queryTotalMoneyByDay(mtd.getStartTimePM(), mtd.getEndTimePM());
        model.addAttribute("totalAM", totalAM==null?0:totalAM);
        model.addAttribute("totalPM", totalPM==null?0:totalPM);
    }

    private void queryCount(String day, Model model) {
        Integer halfAm = buffetOrderDetailService.queryCount(day, "66666"); //午餐半票人数
        Integer fullAm = buffetOrderDetailService.queryCount(day, "88888"); //午餐全票人数
        Integer halfPm = buffetOrderDetailService.queryCount(day, "77777"); //晚餐半票人数
        Integer fullPm = buffetOrderDetailService.queryCount(day, "99999"); //晚餐全票人数

        model.addAttribute("halfAm", halfAm);
        model.addAttribute("fullAm", fullAm);
        model.addAttribute("halfPm", halfPm);
        model.addAttribute("fullPm", fullPm);
    }
}
