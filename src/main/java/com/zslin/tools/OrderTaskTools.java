package com.zslin.tools;

import com.zslin.model.BuffetOrder;
import com.zslin.service.IBuffetOrderService;
import com.zslin.upload.tools.UploadFileTools;
import com.zslin.upload.tools.UploadJsonTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 订单定时任务工具
 *  - 启动时执行
 * Created by zsl on 2018/12/26.
 */
@Component
public class OrderTaskTools implements ApplicationRunner {

    @Autowired
    private IBuffetOrderService buffetOrderService;

    @Autowired
    private UploadFileTools uploadFileTools;

    /** 重新提交未提交成功的订单数据 */
    private void resubmitOrder() {
        List<BuffetOrder> list = buffetOrderService.findNoFinishedOrder();
        System.out.println("-------------执行：重新提交订单 ("+list.size()+")-------------");
//        list.forEach(this::sendBuffetOrder2Server);
        for (BuffetOrder order : list) {
            sendBuffetOrder2Server(order);
        }
    }

    public void sendBuffetOrder2Server(BuffetOrder order) {
        String content = UploadJsonTools.buildDataJson(UploadJsonTools.buildBuffetOrder(order));
        uploadFileTools.setChangeContext(content, true);
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        resubmitOrder(); //
    }
}
