package com.zslin.web.controller;

import com.zslin.basic.tools.NormalTools;
import com.zslin.dto.InventoryDto;
import com.zslin.dto.ProcessOrder;
import com.zslin.model.BuffetOrder;
import com.zslin.service.IBuffetOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/11 16:39.
 * 交班
 */
@Controller
@RequestMapping(value = "web/changeWork")
public class ChangeWorkController {

    @Autowired
    private IBuffetOrderService buffetOrderService;

    @GetMapping(value = "index")
    public String index(Model model, HttpServletRequest request) {
        String curDate = NormalTools.curDate("yyyy-MM-dd");
        List<BuffetOrder> list = buffetOrderService.findByDate(curDate, curDate, "1");
        InventoryDto selfDto = buildDto(list);
        model.addAttribute("selfDto", selfDto);
        model.addAttribute("outDto", buildDto(buffetOrderService.findByDate(curDate, curDate, "0")));
        model.addAttribute("curDate", curDate);
        return "web/changeWork/index";
    }

    //计算自助餐数据
    private InventoryDto buildDto(List<BuffetOrder> list) {
        InventoryDto res = new InventoryDto();
//        ProcessOrder orderList = rebuildProcessOrder(list);
        Integer cashTotal = 0, otherTotal=0; //现金支付订单数
        Float cashMoney = 0f, otherMoney = 0f;
        Float curBond = 0f, decuctBond=0f;
        for(BuffetOrder order : list) { //已经完成的订单
            String status = order.getStatus();
            if("2".equals(status) || "4".equals(status) || "5".equals(status)) {
                if ("1".equals(order.getPayType())) { //现金支付
                    cashTotal++;
                    cashMoney += order.getTotalMoney();
                } else {
                    otherTotal++;
                    otherMoney += order.getTotalMoney();
                }
                if ("4".equals(status) || "5".equals(status)) { //订单已完成
                    decuctBond += order.getSurplusBond();
                } else if ("2".equals(status)) {
                    curBond += order.getSurplusBond();
                }
            }
        }
        res.setCashMoney(cashMoney);
        res.setCashTotal(cashTotal);
        res.setCurBond(curBond);
        res.setDecuctBond(decuctBond);
        res.setOtherMoney(otherMoney);
        res.setOtherTotal(otherTotal);
        return res;
    }

    /**
     * 生成与资金有关系的订单
     * @param list
     * @return
     */
    private ProcessOrder rebuildProcessOrder(List<BuffetOrder> list) {
        List<BuffetOrder> finishedList = new ArrayList<>();
        List<BuffetOrder> ingList = new ArrayList<>();
        for(BuffetOrder b : list) {
            String status = b.getStatus();
            if("4".equals(status) || "5".equals(status)) { //订单已完成
                finishedList.add(b);
            } else if("2".equals(status)) {
                ingList.add(b);
            }
        }
        return new ProcessOrder(finishedList, ingList);
    }
}
