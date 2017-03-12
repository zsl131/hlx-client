package com.zslin.tools;

import com.alibaba.fastjson.JSON;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.model.Company;
import com.zslin.service.ICompanyService;
import com.zslin.timer.MyScheduleService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/12 11:40.
 * 接收到服务端配置信息的处理工具
 */
@Component
public class CompanyDataTools {

    @Autowired
    private ICompanyService companyService;

    @Autowired
    private MyScheduleService myScheduleService;

    @Autowired
    private ClientConfigTools clientConfigTools;

    public void handler(JSONObject jsonObj) {
        Company c = companyService.loadOne();
        if(c==null) {c=new Company();}
        Integer oldUploadTime = c.getUploadTime();
        Integer oldDownloadTime = c.getDownloadTime();
        Company com = JSON.toJavaObject(JSON.parseObject(jsonObj.toString()), Company.class);
        MyBeanUtils.copyProperties(com, c, new String[]{"id"});
        companyService.save(c);

        clientConfigTools.setCompanyJson(c, false);

        if(oldUploadTime!=c.getUploadTime()) {
            myScheduleService.addUploadTask(c);
        }
        if(oldDownloadTime!=c.getDownloadTime()) {
            myScheduleService.addDownloadTask(c);
        }
    }
}
