package com.zslin.tools;

import com.alibaba.fastjson.JSON;
import com.zslin.basic.tools.ConfigTools;
import com.zslin.model.Company;
import com.zslin.service.ICompanyService;
import com.zslin.timer.MyScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/12 9:02.
 */
@Component
public class ClientConfigTools {

    @Autowired
    private ICompanyService companyService;

    @Autowired
    private ConfigTools configTools;

    @Autowired
    private MyScheduleService myScheduleService;

    private static final String CONFIG_FILE = "config.json";

    public Company getCompany() {
        String context = getFileContext(getConfigFile());
        Company res = null;
        if(context==null || "".equals(context.trim())) {
            res = buildNewObj();
        } else {
            try {
                res = JSON.toJavaObject(JSON.parseObject(context), Company.class);
            } catch (Exception e) {
                res = buildNewObj();
            }
        }
        return res;
    }

    private Company buildNewObj() {
        Company res = companyService.loadOne();
        if(res==null) {
            res = new Company();
            /*res.setBasePort(80);
            res.setDownloadTime(20);
            res.setUploadTime(20);*/
        }
        String context = JSON.toJSONString(res);
        setFileContext(getConfigFile(), context);
        return res;
    }

    public void setCompanyJson(Company c) {
        setCompanyJson(c, true);
    }

    public void setCompanyJson(Company c, boolean modifyTask) {
        String json = JSON.toJSONString(c);
        setFileContext(getConfigFile(), json);

        if(modifyTask) {
            //修改定时任务
            myScheduleService.addUploadTask(c);
            myScheduleService.addDownloadTask(c);
        }
    }

    private File getConfigFile() {
        return getFile(CONFIG_FILE);
    }

    private File getFile(String fileName) {
        File file = new File(configTools.getUploadPath("/config/")+fileName);
        if(!file.exists()) {
            createFile(fileName);
            file = new File(configTools.getUploadPath("/config/")+fileName);
        }
        return file;
    }

    private void createFile(String fileName) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"));
            bw.write("");
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(bw!=null) {
                    bw.close();
                }
            } catch (IOException e) {
            }
        }
    }

    private String getFileContext(File file) {
        BufferedReader br = null;
//        String res = null;
        StringBuffer sb = new StringBuffer();
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String str = null;
            while((str=br.readLine())!=null) {
                sb.append(str).append("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(br!=null) {
                    br.close();
                }
            } catch (IOException e) {
            }
        }
        return sb.toString();
    }

    private void setFileContext(File file, String content) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            bw.write(content);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(bw!=null) {
                    bw.close();
                }
            } catch (IOException e) {
            }
        }
    }
}
