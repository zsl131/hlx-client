package com.zslin.tools;

import com.zslin.model.Company;
import com.zslin.timer.ScheduledDtoTools;
import com.zslin.upload.tools.UploadFileTools;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/12 9:21.
 */
@Component
public class TimerTaskTools {

    public static final String UPLOAD_TASK_NAME = "uploadThread";
    public static final String DOWNLOAD_TASK_NAME = "downloadThread";

    @Autowired
    private ClientConfigTools clientConfigTools;

    @Autowired
    private CompanyDataTools companyDataTools;

    @Autowired
    private WorkerDataTools workerDataTools;

    @Autowired
    private PriceDataTools priceDataTools;

    @Autowired
    private UploadFileTools uploadFileTools;

    public void upload() {
        Company c = clientConfigTools.getCompany();
        if(c!=null && c.getId()!=null && c.getId()>0) {

            String json = uploadFileTools.getChangeContext();
            if(json!=null && !"".equals(json.trim())) {
                Integer resCode = InternetTools.post(buildUrl(c, true)+"?token="+c.getToken(), json);
                if(resCode==200) { //如果提交成功则清空数据
                    uploadFileTools.setChangeContext("", false);
                }
            }
        }
//        System.out.println("===========upload=========="+ ScheduledDtoTools.getInstance().getDtoList().size());
    }

    public void download() {
        Company c = clientConfigTools.getCompany();
        if(c!=null && c.getId()!=null && c.getId()>0) {
            String json = InternetTools.doGet(buildUrl(c, false), buildBasePar(c));
            Integer status = (Integer) JsonTools.getJsonParam(json, "status");
            if(status!=null && status==1) { //表示获取成功
                String datas = JsonTools.getJsonParam(json, "data").toString();
                processDatas(datas);
            }
            System.out.println(json);
        }
    }

    private void processDatas(String dataJson) {
        JSONArray array = new JSONArray(dataJson);
        for(int i=0; i<array.length();i++) {
            JSONObject jsonObj = array.getJSONObject(i);
            processSingleData(jsonObj);
        }
    }

    private void processSingleData(JSONObject jsonObj) {
        String action = jsonObj.getString("action"); //操作
        String type = jsonObj.getString("type"); //类型
        Integer dataId = jsonObj.getInt("dataId"); //对象Id
        JSONObject dataObj = jsonObj.getJSONObject("data"); //具体对象的Json数据
        if("config".equals(type)) { //修改配置信息
            companyDataTools.handler(dataObj);
        } else if("worker".equals(type)) { //处理员工信息
            workerDataTools.handler(dataId, dataObj, action);
        } else if("price".equals(type)) { //价格
            priceDataTools.handler(dataObj);
        }
    }

    private String buildUrl(Company c, boolean isUpload) {
        String res = (c.getBaseUrl()+":"+c.getBasePort())+(isUpload?c.getUploadUrl():c.getDownloadUrl());
        return res;
    }

    private Map<String, Object> buildBasePar(Company c) {
        Map<String, Object> res = new HashMap<>();
        res.put("token", c.getToken());
        return res;
    }
}
