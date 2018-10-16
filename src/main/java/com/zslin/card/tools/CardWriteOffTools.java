package com.zslin.card.tools;

import com.zslin.basic.tools.NormalTools;
import com.zslin.card.model.Card;
import com.zslin.card.model.CardCheck;
import com.zslin.card.model.CardUnder;
import com.zslin.card.service.ICardCheckService;
import com.zslin.card.service.ICardService;
import com.zslin.card.service.ICardUnderService;
import com.zslin.card.service.IGrantCardService;
import com.zslin.upload.tools.UploadFileTools;
import com.zslin.upload.tools.UploadJsonTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 卡券核销工具类
 * Created by zsl on 2018/10/16.
 */
@Component
public class CardWriteOffTools {

    @Autowired
    private ICardCheckService cardCheckService;

    @Autowired
    private ICardUnderService cardUnderService;

    @Autowired
    private ICardService cardService;

    @Autowired
    private IGrantCardService grantCardService;

    @Autowired
    private UploadFileTools uploadFileTools;

    public void writeOff(Card card, String orderNo) {
        card.setStatus("1");
        cardService.save(card);
        sendCard2Server(card); //发送到服务端

        CardCheck cc = new CardCheck();
        cc.setCardNo(card.getNo());
        cc.setCardType(card.getType());
        cc.setCheckDay(NormalTools.curDate("yyyyMMdd"));
        cc.setCheckMonth(NormalTools.curDate("yyyyMM"));
        cc.setCheckTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
        cc.setOrderNo(orderNo);
        if("1".equals(card.getType())) { //如果是10元券
            CardUnder cu = cardUnderService.findByCardNo(card.getNo());
            if(cu!=null) {
                cc.setUnderKey(cu.getUnderKey());
                cc.setUnderName(cu.getUnderName());
            }
        }
        cardCheckService.save(cc);
        sendCardCheck2Server(cc);
    }

    public void sendCard2Server(Card obj) {
        String content = UploadJsonTools.buildDataJson(UploadJsonTools.buildCard(obj));
        uploadFileTools.setChangeContext(content, true);
    }

    public void sendCardCheck2Server(CardCheck obj) {
        String content = UploadJsonTools.buildDataJson(UploadJsonTools.buildCardCheck(obj));
        uploadFileTools.setChangeContext(content, true);
    }
}
