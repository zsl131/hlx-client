package com.zslin.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.model.Company;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/10 10:07.
 */
public interface ICompanyService extends BaseRepository<Company, Integer> {

    @Query("FROM Company ")
    Company loadOne();

    @Query("UPDATE Company c SET c.curVersion=?1")
    @Modifying
    @Transactional
    void updateVersion(String version);
}
