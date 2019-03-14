package com.zslin.tools;

import com.zslin.model.DiscountDay;
import com.zslin.service.IDiscountDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by zsl on 2019/3/13.
 */
@Component
public class DiscountDayTools {

    @Autowired
    private IDiscountDayService discountDayService;

    /** 今天是否为折扣日 */
    public boolean isDiscountDay() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dayStr = sdf.format(cal.getTime());
        DiscountDay day = discountDayService.findByYearMonth(dayStr.substring(0, 6));
        if(day!=null) {
            String days = day.getDays();
            String [] array = days.split(",");
            for(String single : array) {
                if(Integer.parseInt(dayStr.substring(6 ,8)) == Integer.parseInt(single.split("_")[0])) {
                    return single.split("_")[1].equals("1");
                }
            }
        }
        return false;
    }
}
