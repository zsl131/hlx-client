package com.zslin.model;

import javax.persistence.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/10 10:00.
 */
@Entity
@Table(name = "t_company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 公司简称 */
    private String name;

    /** 公司全称 */
    @Column(name = "long_name")
    private String longName;

    /** 上传数据时间间隔，单位秒 */
    @Column(name = "upload_time")
    private Integer uploadTime;

    /** 下载数据时间间隔，单位秒 */
    @Column(name = "download_time")
    private Integer downloadTime;

    /** 请求的地址 */
    @Column(name = "base_url")
    private String baseUrl;

    /** 请求的端口号 */
    @Column(name = "base_port")
    private Integer basePort;

    /** 上传数据的Url */
    @Column(name = "upload_url")
    private String uploadUrl;

    /** 下载数据的Url */
    @Column(name = "download_url")
    private String downloadUrl;

    /** token识别客户端的唯一标识 */
    private String token;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Integer getBasePort() {
        return basePort;
    }

    public void setBasePort(Integer basePort) {
        this.basePort = basePort;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public Integer getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Integer uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Integer getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(Integer downloadTime) {
        this.downloadTime = downloadTime;
    }
}