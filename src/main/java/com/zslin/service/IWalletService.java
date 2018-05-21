package com.zslin.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.model.Wallet;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zsl on 2018/5/19.
 */
public interface IWalletService extends BaseRepository<Wallet, Integer> {
    Wallet findByOpenid(String openid);

    Wallet findByPhone(String phone);

    @Query("UPDATE Wallet w SET w.score=w.score+?1 WHERE w.openid=?2")
    @Modifying
    @Transactional
    void plusScore(Integer score, String openid);
}
