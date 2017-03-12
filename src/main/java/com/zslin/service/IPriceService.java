package com.zslin.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.model.Price;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/12 15:41.
 */
public interface IPriceService extends BaseRepository<Price, Integer> {

    @Query("FROM Price")
    Price findOne();
}
