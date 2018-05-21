package com.zslin.web.tools;

import com.zslin.service.IWalletService;
import com.zslin.upload.tools.UploadFileTools;
import com.zslin.upload.tools.UploadJsonTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zsl on 2018/5/19.
 */
@Component
public class ScoreTools {

    @Autowired
    private IWalletService walletService;

    @Autowired
    private UploadFileTools uploadFileTools;

    /**
     * 积分消费
     * @param openid 消费者手机号码
     * @param score 消费积分数量
     */
    public void scoreConsume(String openid, Integer score) {
        walletService.plusScore(0-score, openid);
        sendWalletScore2Server(openid, 0-score);
    }

    private void sendWalletScore2Server(String openid, Integer score) {
        String content = UploadJsonTools.buildDataJson(UploadJsonTools.buildWalletScore(openid, score));
        uploadFileTools.setChangeContext(content, true);
    }
}
