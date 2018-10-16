package com.zslin.card.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.card.model.ApplyReason;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zsl on 2018/10/13.
 */
public interface IApplyReasonService extends BaseRepository<ApplyReason, Integer>, JpaSpecificationExecutor<ApplyReason> {

    @Query("DELETE FROM ApplyReason a WHERE a.objId=?1")
    @Modifying
    @Transactional
    void deleteByObjId(Integer objId);

    ApplyReason findByObjId(Integer objId);
}
