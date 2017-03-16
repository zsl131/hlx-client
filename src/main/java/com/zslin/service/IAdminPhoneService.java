package com.zslin.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.model.AdminPhone;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/14 14:25.
 */
public interface IAdminPhoneService extends BaseRepository<AdminPhone, Integer> {

    AdminPhone findByAccountId(Integer accountId);

    @Query("DELETE AdminPhone a WHERE a.accountId=?1")
    @Modifying
    @Transactional
    void deleteByAccountId(Integer accountId);

    AdminPhone findByPhone(String phone);
}
