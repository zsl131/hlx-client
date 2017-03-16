package com.zslin.tools;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/15 16:09.
 * 订单操作工具类
 */
public class OrdersOperateTools {

    /**
     * 判断是否可退票
     * @param refundMin 分钟
     * @param entryLong 入场时间
     * @return
     */
    public static boolean canRetreat(Integer refundMin, Long entryLong) {
        if(entryLong==null || entryLong<=0) {return true;}
        long diffSeconds = (System.currentTimeMillis()-entryLong)/1000;
        return (diffSeconds-(refundMin*60))<=0;
    }
}
