package com.zslin.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.model.BuffetOrder;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/2 23:27.
 */
public interface IBuffetOrderService extends BaseRepository<BuffetOrder, Integer>, JpaSpecificationExecutor<BuffetOrder> {

    BuffetOrder findByNo(String no);

    @Query("FROM BuffetOrder b WHERE b.createDay>=?1 AND b.createDay<=?1 AND b.isSelf=?3")
    List<BuffetOrder> findByDate(String startDate, String endDate, String isSelf);
}
