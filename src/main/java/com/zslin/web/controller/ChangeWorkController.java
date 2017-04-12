package com.zslin.web.controller;

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

        return "web/changeWork/index";
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
            } else if("3".equals(status)) {
                ingList.add(b);
            }
        }
        return new ProcessOrder(finishedList, ingList);
    }
}
class ProcessOrder {
    private List<BuffetOrder> finishedList;
    private List<BuffetOrder> ingList;

    public ProcessOrder(List<BuffetOrder> finishedList, List<BuffetOrder> ingList) {
        this.finishedList = finishedList;
        this.ingList = ingList;
    }

    public List<BuffetOrder> getFinishedList() {
        return finishedList;
    }

    public void setFinishedList(List<BuffetOrder> finishedList) {
        this.finishedList = finishedList;
    }

    public List<BuffetOrder> getIngList() {
        return ingList;
    }

    public void setIngList(List<BuffetOrder> ingList) {
        this.ingList = ingList;
    }
}
