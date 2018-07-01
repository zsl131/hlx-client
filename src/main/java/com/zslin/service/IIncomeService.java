package com.zslin.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.model.Income;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by zsl on 2018/7/1.
 */
public interface IIncomeService extends BaseRepository<Income, Integer>, JpaSpecificationExecutor<Income> {

    Income findByComeDay(String comeDay);
}
