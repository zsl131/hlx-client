package com.zslin.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.sankuai.sjst.platform.developer.domain.RequestSysParams;
import com.sankuai.sjst.platform.developer.request.CipCaterCouponConsumptionCancelRequest;
import com.sankuai.sjst.platform.developer.request.CipCaterCouponConsumptionPrepareRequest;
import com.sankuai.sjst.platform.developer.request.CipCaterCouponQueryRequest;
import com.zslin.meituan.dto.ReadyDto;
import com.zslin.meituan.model.MeituanConfig;
import com.zslin.meituan.model.MeituanShop;
import com.zslin.meituan.service.IMeituanConfigService;
import com.zslin.meituan.service.IMeituanShopService;
import com.zslin.meituan.tools.MeituanHandlerTools;
import com.zslin.meituan.tools.MeituanParamsTools;
import com.zslin.tools.JsonTools;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/10 15:51.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class MeituanTest {

    @Autowired
    private IMeituanConfigService meituanConfigService;

    @Value("${poiId}")
    private String poiId;

    @Autowired
    private IMeituanShopService meituanShopService;

    @Autowired
    private MeituanHandlerTools meituanHandlerTools;

    @Test
    public void test09() {
        meituanHandlerTools.handlerCheck("018679450756", 1, "2017070610002");
    }

    @Test
    public void test10() {
        meituanHandlerTools.handlerUndo("124022850248");
    }

    @Test
    public void test11() {
        String str = "{\"data\":{\"couponCodes\":[\"018679450756\"],\"result\":0,\"dealId\":41777850,\"dealValue\":0.11,\"dealTitle\":\"kfpttest_zl5_02人餐\",\"poiid\":156596339,\"message\":\"\"}}";
        String data = JsonTools.getJsonParam(str, "data").toString();
        JSONArray array = JSON.parseArray(JsonTools.getJsonParam(data, "couponCodes").toString());
        for(int i=0; i<array.size(); i++) {
            System.out.println("==="+array.get(i).toString());
        }
    }

    @Test
    public void test01() {
        try {
            MeituanConfig config = meituanConfigService.loadOne();
            MeituanShop shop = meituanShopService.findByPoiId(poiId);
            RequestSysParams requestSysParams = new RequestSysParams(config.getSignKey(), shop.getToken());
//            RequestSysParams requestSysParams = new RequestSysParams("t_8EcVmYko", signKey);
            CipCaterCouponQueryRequest request = new CipCaterCouponQueryRequest();
            request.setRequestSysParams(requestSysParams);
            request.setCouponCode("185309850041");
            String str = request.doRequest();
            System.out.println("========="+str);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    MeituanParamsTools meituanParamsTools;

    @Test
    public void test04() throws Exception {
        RequestSysParams requestSysParams = meituanParamsTools.getSysParams();
        CipCaterCouponConsumptionPrepareRequest request = new CipCaterCouponConsumptionPrepareRequest();
        request.setRequestSysParams(requestSysParams);
        request.setCouponCode("185309850042");
        String str = request.doRequest();
        System.out.println(str);
    }

    @Test
    public void test07() {
        String str = "{\"error\":{\"code\":1008,\"error_type\":\"remote_error\",\"message\":\"无效码\"}}";
        String str2 = "{\"data\":{\"dealBeginTime\":\"2016-10-26\",\"dealId\":41777850,\"dealValue\":0.11,\"dealTitle\":\"kfpttest_zl5_02人餐\",\"count\":3,\"dealPrice\":0.05,\"dealMenu\":[[{\"type\":\"0\",\"content\":\"开放平台测试小吃\"},{\"total\":\"0.1\",\"price\":\"0.1\",\"specification\":\"1份\",\"type\":\"128\",\"content\":\"小吃1\"},{\"total\":\"0.01\",\"price\":\"0.01\",\"specification\":\"1份\",\"type\":\"128\",\"content\":\"小吃2\"},{\"type\":\"0\",\"content\":\"免费提供餐巾纸\"}]],\"message\":\"\",\"minConsume\":1,\"couponEndTime\":\"2017-10-25\",\"result\":0,\"couponBuyPrice\":0.05,\"couponCode\":\"185309850041\"}}";
        String res = (String) JsonTools.getJsonParam(str, "data");
        System.out.println(res);
        System.out.println(JsonTools.getJsonParam(JsonTools.getJsonParam(str, "error").toString(), "message"));
        System.out.println(JsonTools.getJsonParam(str2, "data"));
    }

    @Test
    public void test08() {
        String str2 = "{\"data\":{\"dealBeginTime\":\"2016-10-26\",\"dealId\":41777850,\"dealValue\":0.11,\"dealTitle\":\"kfpttest_zl5_02人餐\",\"count\":3,\"dealPrice\":0.05,\"dealMenu\":[[{\"type\":\"0\",\"content\":\"开放平台测试小吃\"},{\"total\":\"0.1\",\"price\":\"0.1\",\"specification\":\"1份\",\"type\":\"128\",\"content\":\"小吃1\"},{\"total\":\"0.01\",\"price\":\"0.01\",\"specification\":\"1份\",\"type\":\"128\",\"content\":\"小吃2\"},{\"type\":\"0\",\"content\":\"免费提供餐巾纸\"}]],\"message\":\"\",\"minConsume\":1,\"couponEndTime\":\"2017-10-25\",\"result\":0,\"couponBuyPrice\":0.05,\"couponCode\":\"185309850041\"}}";
        ReadyDto dto = JSON.toJavaObject(JSON.parseObject(JsonTools.getJsonParam(str2, "data").toString()), ReadyDto.class);
//                     JSON.toJavaObject(JSON.parseObject(jsonObj.toString()), MeituanShop.class);
        System.out.println(dto);
    }

    //撤消
    @Test
    public void test06() {
        RequestSysParams params = meituanParamsTools.getSysParams();
        CipCaterCouponConsumptionCancelRequest request = new CipCaterCouponConsumptionCancelRequest();
        request.setRequestSysParams(params);
//        request.set
    }

    @Test
    public void test02() throws Exception {
        String str = "http://zsl8.5166.info/web/index";
        System.out.println(URLEncoder.encode(str, "utf-8"));
    }

    @Test
    public void test03() {
        System.out.println("======="+poiId);
    }
}
