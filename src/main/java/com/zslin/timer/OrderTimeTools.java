package com.zslin.timer;

import com.zslin.basic.tools.NormalTools;
import com.zslin.model.BuffetOrder;
import com.zslin.service.IBuffetOrderService;
import com.zslin.upload.tools.UploadFileTools;
import com.zslin.upload.tools.UploadJsonTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/6/11 23:50.
 */
@Component
public class OrderTimeTools {

    @Autowired
    private IBuffetOrderService buffetOrderService;

    @Autowired
    private UploadFileTools uploadFileTools;

    //第一次延迟50秒执行，当执行完后30分钟再执行，半小时未付款则自动取消
    @Scheduled(initialDelay = 1000*50, fixedDelay = 1000*60*30)
    public void processCancelOrders() {
        List<BuffetOrder> list = buffetOrderService.findByNoPay();
        for(BuffetOrder orders : list) {
            if(isTimeout(orders.getCreateTime(), 30)) {
                orders.setStatus("-2");
                orders.setRetreatName("系统自动");
                orders.setRetreatReason("长时间未付款");
                orders.setEndLong(System.currentTimeMillis());
                orders.setEndTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
                orders.setRetreatTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
                buffetOrderService.save(orders);

                //TODO 需要将结果发送至服务器
                sendBuffetOrder2Server(orders);
            }
        }
    }

    //第一次延迟30秒执行，当执行完后15分钟再执行，15分钟内自动处理押金为0的订单
    @Scheduled(initialDelay = 1000*30, fixedDelay = 1000*60*15)
    public void processOrders() {
        List<BuffetOrder> list = buffetOrderService.findByNoBond();
        for(BuffetOrder o : list) {
            if(isTimeout(o.getCreateTime(), 20)) {
                o.setStatus("4");
                o.setSurplusBond(0f); //扣下来的压金
                o.setEndLong(System.currentTimeMillis());
                o.setEndTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));

                buffetOrderService.save(o);

                sendBuffetOrder2Server(o);
            }
        }
    }

    public void sendBuffetOrder2Server(BuffetOrder order) {
        String content = UploadJsonTools.buildDataJson(UploadJsonTools.buildBuffetOrder(order));
        uploadFileTools.setChangeContext(content, true);
    }

    private boolean isTimeout(String createTime, Integer min) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date createDate = sdf.parse(createTime);

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, 0-min);
            return cal.getTime().after(createDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
