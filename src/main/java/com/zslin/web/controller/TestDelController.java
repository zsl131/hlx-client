package com.zslin.web.controller;

import com.zslin.model.BuffetOrder;
import com.zslin.service.IBuffetOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by zsl on 2018/7/4.
 */
@RestController
@RequestMapping(value = "test/del")
public class TestDelController {

    @Autowired
    private IBuffetOrderService buffetOrderService;

    @GetMapping(value = "run")
    public String run(HttpServletRequest request, String from, String to, Integer money) {
        Float total = buffetOrderService.queryTotalMoneyByDay(from);
        handle(from, to, money);
        return "result:"+total;
    }

    private void handle(String from ,String to, Integer money) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar tempCal = Calendar.getInstance();
            tempCal.setTime(sdf.parse(to));

            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(from));

            if(cal.before(tempCal)) {
                do {
                    delete(from, money);
                    cal.add(Calendar.DAY_OF_MONTH, 1);
                    from = sdf.format(cal.getTime());
                } while (!from.equals(to));
            } else {
                delete(from, money);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void delete(String date, Integer money) {
        Float total = buffetOrderService.queryTotalMoneyByDay(date);
        total=total==null?0f:total;
        if(money==null || money<=0) {
            money = 4000;
        }
        while(total>money) {
            List<BuffetOrder> list = buffetOrderService.listByDate(date);
            for(int i=0;i<10 && i<list.size(); i++) {
                buffetOrderService.delete(list.get(i));
            }
            total = buffetOrderService.queryTotalMoneyByDay(date);
            total=total==null?0f:total;
        }
    }
}
