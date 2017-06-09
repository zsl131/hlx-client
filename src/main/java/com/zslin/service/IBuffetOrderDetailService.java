package com.zslin.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.model.BuffetOrderDetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/2 23:28.
 */
public interface IBuffetOrderDetailService extends BaseRepository<BuffetOrderDetail, Integer>, JpaSpecificationExecutor<BuffetOrderDetail> {

    @Query("FROM BuffetOrderDetail bod WHERE bod.orderNo=?1 ORDER BY bod.price DESC, bod.commodityNo ASC")
    List<BuffetOrderDetail> listByOrderNo(String orderNo);

    @Query("SELECT price FROM BuffetOrderDetail WHERE orderNo=?1 AND commodityNo=?2 GROUP BY commodityNo")
    Float findPrice(String orderNo, String comNo);
}
