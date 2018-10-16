package com.zslin.upload.tools;

import com.alibaba.fastjson.JSON;
import com.zslin.card.model.Card;
import com.zslin.card.model.CardApply;
import com.zslin.card.model.CardCheck;
import com.zslin.card.model.GrantCard;
import com.zslin.model.*;
import com.zslin.upload.dto.JsonDto;
import com.zslin.upload.dto.NormalDto;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/10 11:29.
 */
public class UploadJsonTools {

    /**
     * 构建能发送的Json数据
     * @param dataJson
     * @return
     */
    public static String buildDataJson(String dataJson) {
        StringBuffer sb = new StringBuffer();
        sb.append("{status:1,info:\"ok\",data:[");
        sb.append(dataJson);
        sb.append("]}");
        return sb.toString();
    }

    public static String buildWorkerJson(Worker w) {
        JsonDto jd = new JsonDto("worker", "update", w.getObjId(), w);
        return JSON.toJSONString(jd);
    }

    public static String buildOrderJson(Orders orders) {
        JsonDto jd = new JsonDto("orders", "update", orders.getId(), orders);
        return JSON.toJSONString(jd);
    }

    public static String buildMemberJson(Member m) {
        JsonDto jd = new JsonDto("member", "update", m.getId(), m);
        return JSON.toJSONString(jd);
    }

    public static String buildMemberChargeJson(MemberCharge mc) {
        JsonDto jd = new JsonDto("memberCharge", "update", mc.getId(), mc);
        return JSON.toJSONString(jd);
    }

    public static String buildBuffetOrder(BuffetOrder bo) {
        JsonDto jd = new JsonDto("buffetOrder", "update", bo.getId(), bo);
        return JSON.toJSONString(jd);
    }

    public static String buildBuffetOrderDetail(BuffetOrderDetail detail) {
        JsonDto jd = new JsonDto("buffetOrderDetail", "update", detail.getId(), detail);
        return JSON.toJSONString(jd);
    }

    public static String buildPassword(String phone, String password) {
        JsonDto jd = new JsonDto("password", "update", 0, new NormalDto(phone, password));
        return JSON.toJSONString(jd);
    }

    public static String buildWalletScore(String openid, Integer score) {
        JsonDto jd = new JsonDto("wallet", "update", 0, new NormalDto(openid, score+""));
        return JSON.toJSONString(jd);
    }

    public static String buildIncome(Income income) {
        JsonDto jd = new JsonDto("income", "update", income.getId(), income);
        return JSON.toJSONString(jd);
    }

    /** 以下是卡券相关 */
    public static String buildGrantCard(GrantCard gc) {
        JsonDto jd = new JsonDto("cardGrantCard", "update", gc.getId(), gc);
        return JSON.toJSONString(jd);
    }

    public static String buildCard(Card card) {
        JsonDto jd = new JsonDto("card", "update", card.getId(), card);
        return JSON.toJSONString(jd);
    }

    public static String buildCardApply(CardApply ca) {
        JsonDto jd = new JsonDto("cardApply", "update", ca.getId(), ca);
        return JSON.toJSONString(jd);
    }

    public static String buildCardCheck(CardCheck obj) {
        JsonDto jd = new JsonDto("cardCheck", "update", obj.getId(), obj);
        return JSON.toJSONString(jd);
    }
    /** 以上是卡券相关 */
}
