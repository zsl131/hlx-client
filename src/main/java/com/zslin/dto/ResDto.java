package com.zslin.dto;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/15 16:03.
 * 提交数据的返回DTO对象
 */
public class ResDto {
    private String code;

    private String msg;

    public ResDto(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResDto(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
