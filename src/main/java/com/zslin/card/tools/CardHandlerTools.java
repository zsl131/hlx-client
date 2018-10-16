package com.zslin.card.tools;

import com.alibaba.fastjson.JSON;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.card.model.*;
import com.zslin.card.service.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 卡券处理
 * Created by zsl on 2018/10/15.
 */
@Component
public class CardHandlerTools {

    @Autowired
    private IApplyReasonService applyReasonService;

    @Autowired
    private ICardService cardService;

    @Autowired
    private IGrantCardService grantCardService;

    @Autowired
    private ICardApplyService cardApplyService;

    @Autowired
    private ICardUnderService cardUnderService;

    public void handlerApplyReason(String action, Integer dataId, JSONObject jsonObj) {
        if("delete".equalsIgnoreCase(action)) {
            applyReasonService.deleteByObjId(dataId);
        } else {
            ApplyReason ar = applyReasonService.findByObjId(dataId); //获取原有数据
            ApplyReason reason = JSON.toJavaObject(JSON.parseObject(jsonObj.toString()), ApplyReason.class);
            if(ar==null) {
                reason.setObjId(dataId);
                applyReasonService.save(reason);
            } else {
                MyBeanUtils.copyProperties(reason, ar, "objId");
                applyReasonService.save(ar);
            }
        }
    }

    public void handlerCard(JSONObject jsonObj) {
        Card card = JSON.toJavaObject(JSON.parseObject(jsonObj.toString()), Card.class);
        Integer cardNo = card.getNo();
        Card c = cardService.findByNo(cardNo);
        if(c==null) {cardService.save(card);} else {
            MyBeanUtils.copyProperties(card, c);
            cardService.save(c);
        }
    }

    public void handlerGrantCard(JSONObject jsonObj) {
        GrantCard card = JSON.toJavaObject(JSON.parseObject(jsonObj.toString()), GrantCard.class);
        Integer cardNo = card.getCardNo();
        GrantCard gc = grantCardService.findByCardNo(cardNo);
        if(gc==null) {grantCardService.save(card);} else {
            MyBeanUtils.copyProperties(card, gc);
            grantCardService.save(gc);
        }
    }

    public void handlerGrantCardStatus(String action, JSONObject jsonObject) {
        //{"nos":[1000041,1000042,1000043,1000044,1000045],"status":"1"}
        String status = jsonObject.getString("status");
        JSONArray array = jsonObject.getJSONArray("nos");
        Integer [] nos = new Integer[array.length()];
        for(int i=0;i<array.length();i++) {
            nos[i] = (Integer) array.get(i);
        }
        grantCardService.updateStatus(status, nos);
    }

    public void handlerCardApply(JSONObject jsonObj) {
        CardApply ca = JSON.toJavaObject(JSON.parseObject(jsonObj.toString()), CardApply.class);
        Integer cardNo = ca.getCardNo();
        CardApply apply = cardApplyService.findByCardNo(cardNo);
        MyBeanUtils.copyProperties(ca, apply);
        cardApplyService.save(apply);
    }

    public void handlerCardUnder(JSONObject jsonObj) {
        CardUnder cu = JSON.toJavaObject(JSON.parseObject(jsonObj.toString()), CardUnder.class);
        Integer cardNo = cu.getCardNo();
        CardUnder under = cardUnderService.findByCardNo(cardNo);
        if(under==null) {cardUnderService.save(cu);}
    }
}
