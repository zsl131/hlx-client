package com.zslin.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.model.Rules;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/9 10:31.
 */
public interface IRulesService extends BaseRepository<Rules, Integer> {

    @Query("FROM Rules ")
    Rules loadOne();
}
