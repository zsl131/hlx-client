package com.zslin.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.model.Prize;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/10 17:21.
 */
public interface IPrizeService extends BaseRepository<Prize, Integer>, JpaSpecificationExecutor<Prize> {

    Prize findByDataId(Integer dataId);

    @Query("DELETE FROM Prize p WHERE p.dataId=?1")
    @Modifying
    @Transactional
    void deleteByDataId(Integer dataId);
}
