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

    @Autowired
    private VersionTools versionTools;

    public void handler(JSONObject jsonObj) {
        Company c = companyService.loadOne();
        if(c==null) {c=new Company();}
        Integer oldUploadTime = c.getUploadTime();
        Integer oldDownloadTime = c.getDownloadTime();
        Company com = JSON.toJavaObject(JSON.parseObject(jsonObj.toString()), Company.class);

        //如果服务端有版本信息，并且版本信息与当前版本信息不同
        if(com.getClientVersion()!=null && !com.getClientVersion().equals(c.getCurVersion())) {
            versionTools.upgradeVersion(com.getClientVersion(), com.getVersionUrl()); //更新版本
        }

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
