package com.zslin.tools;

import com.zslin.model.Worker;
import com.zslin.service.IWorkerService;
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
        Worker w = workerService.findByObjId(dataId);
        if("delete".equals(action)) {
            workerService.delete(w);
        } else if("update".equals(action)) {
            if(w==null) {
                w = new Worker();
            }
            w.setHeadimgurl(jsonObj.getString("headimgurl"));
            w.setName(jsonObj.getString("name"));
            w.setObjId(dataId);
            w.setOpenid(jsonObj.getString("openid"));
            w.setPassword(jsonObj.getString("password"));
            w.setPhone(jsonObj.getString("phone"));
            workerService.save(w);
        }
    }
}
