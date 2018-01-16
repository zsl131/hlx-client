package com.zslin.tools;

import com.zslin.model.Restday;
import com.zslin.service.IRestdayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by zsl on 2018/1/13.
 */
@Component
public class RestdayTools {

    @Autowired
    private IRestdayService restdayService;

    public boolean isWorkday() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dayStr = sdf.format(cal.getTime());
        Restday restday = restdayService.findByYearMonth(dayStr.substring(0, 6));
        if(restday!=null) {
            String days = restday.getDays();
            String [] array = days.split(",");
            for(String single : array) {
                if(Integer.parseInt(dayStr.substring(6 ,8)) == Integer.parseInt(single.split("_")[0])) {
                    return single.split("_")[1].equals("1");
                }
            }
        }
        return true;
    }
}
