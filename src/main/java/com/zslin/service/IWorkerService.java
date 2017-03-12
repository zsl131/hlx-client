package com.zslin.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.model.Worker;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/10 10:16.
 */
public interface IWorkerService extends BaseRepository<Worker, Integer>, JpaSpecificationExecutor<Worker> {

    Worker findByPhone(String phone);

    Worker findByOpenid(String openid);

    Worker findByObjId(Integer objId);

    @Query("SELECT COUNT(id) FROM Worker w")
    Integer findCount();
}
