package com.zslin.upload.tools;

import com.alibaba.fastjson.JSON;
import com.zslin.model.Worker;
import com.zslin.upload.dto.JsonDto;

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
}
