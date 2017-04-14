package com.zslin.tools;

import com.zslin.model.BuffetOrder;
import com.zslin.model.BuffetOrderDetail;
import com.zslin.model.Company;
import com.zslin.service.IBuffetOrderDetailService;
import com.zslin.service.ICompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/12 16:01.
 * 下单后打印票据工具类
 */
@Component
public class PrintTicketTools {

    @Autowired
    private IBuffetOrderDetailService buffetOrderDetailService;

    @Autowired
    private WordTemplateTools wordTemplateTools;

    @Autowired
    private ICompanyService companyService;

    /**
     * 打印自助券票据
     * @param order 订单对象
     */
    public void printBuffetOrder(BuffetOrder order) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Long start = System.currentTimeMillis();
                Company com = companyService.loadOne();
                List<BuffetOrderDetail> detailList = buffetOrderDetailService.listByOrderNo(order.getNo());
                printTicket(order, detailList, com.getName(), com.getPhone(), com.getAddress());
                printBond(order, com.getName(), com.getPhone(), com.getAddress());
                Long end = System.currentTimeMillis();
                System.out.println("耗时============="+((end-start)/1000));
            }
        }).start();
    }

    private void printBond(BuffetOrder order, String shopName, String phone, String address) {
        File f = wordTemplateTools.buildBondFile(shopName, order.getCommodityCount(), order.getSurplusBond(),
                order.getEntryTime()==null?order.getCreateTime():order.getEntryTime(), order.getNo(), phone, address);

        PrintTools.print(f.getAbsolutePath());

//        f.delete();
    }

    /**
     * 打印入场券
     * @param order 订单信息
     * @param detailList 订单详情列表
     * @param shopName 公司名称
     */
    private void printTicket(BuffetOrder order, List<BuffetOrderDetail> detailList, String shopName, String phone, String address) {
        String level = getLevel(detailList);
        for(BuffetOrderDetail detail : detailList) {
            if(detail.getPrice()>0) {
                File f = wordTemplateTools.buildTicketFile(shopName,
                        level + ("77777".equals(detail.getCommodityNo()) || "66666".equals(detail.getCommodityNo()) ? "儿童" : ""),
                        order.getEntryTime() == null ? order.getCreateTime() : order.getEntryTime(), order.getNo(), phone, address);

                PrintTools.print(f.getAbsolutePath());
//                f.delete();
            }
        }
    }

    private String getLevel(List<BuffetOrderDetail> detailList) {
        String level = "午餐";
        for(BuffetOrderDetail detail : detailList) {
            if(detail.getPrice()>0 && "99999".equals(detail.getCommodityNo())) {
                level = "晚餐";
                break;
            }
        }
        return level;
    }
}
