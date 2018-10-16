package com.zslin.tools;

import com.zslin.model.Worker;
import com.zslin.service.IWorkerService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/12 11:57.
 * 接收到服务端数据的处理工作
 */
@Component
public class WorkerDataTools {

    @Autowired
    private IWorkerService workerService;

    public void handler(Integer dataId, JSONObject jsonObj, String action) {
        String phone = jsonObj.getString("phone");
        Worker w = workerService.findByPhone(phone);
//        Worker w = workerService.findByObjId(dataId);
        if("delete".equals(action)) {
            workerService.delete(w);
        } else if("update".equals(action)) {
            if(w==null) {
                w = new Worker();
            }
            try {
                w.setHeadimgurl(jsonObj.getString("headimgurl"));
            } catch (JSONException e) {
            }
            w.setName(jsonObj.getString("name"));
            w.setObjId(dataId);
            try {
                w.setOpenid(jsonObj.getString("openid"));
            } catch (JSONException e) {
            }
            w.setPassword(jsonObj.getString("password"));
            w.setPhone(phone);
            workerService.save(w);
        }
    }
}
