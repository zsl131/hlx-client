package com.zslin.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.model.MemberCharge;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/17 22:48.
 */
public interface IMemberChargeService extends BaseRepository<MemberCharge, Integer>, JpaSpecificationExecutor<MemberCharge> {
}
