package com.zslin.test;

import com.zslin.tools.WordTools;

import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/10 17:18.
 */
public class Prient implements Printable {

    private String title;

    private Integer peopleCount=0;

    private Integer childCount=0;

    private String cashierName;

    private String phone;

    private String tel;

    private String address;

    private String info;

    private String day;

    private String time;

    //方式，收银员下单、微信下单
    private String type;

    //等待人数
    private Integer waitCount=0;

    //大约用餐时间，只有waitCount大于0时才使用
    private String aboutTime;

    private String line = "-------------------------------------";

    @Override
    public int print(Graphics g, PageFormat pf, int page) throws PrinterException {

        if (page > 0) {
            return NO_SUCH_PAGE;
        }

        //一个汉字占位：12.7
        //一个字母或数字占位：6.35
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(new Font("Default", Font.PLAIN, 12));
        String title = "昭通汉丽轩（午餐）";
        g2d.drawString(title, WordTools.buildLeftMargin(title), 10);
        g2d.drawString(line, 0, 20);

        g2d.setFont(new Font("Default", Font.PLAIN, 14));
        g2d.drawString(buildPeopleStr(), WordTools.buildLeftMargin(buildPeopleStr()), 40);
        g2d.drawString(buildChildStr(), WordTools.buildLeftMargin(buildChildStr()), 60);

        g2d.setFont(new Font("Default", Font.PLAIN, 10));

        g2d.drawString(line, 0, 80);

        g2d.drawString(type, WordTools.buildLeftMargin(type), 100);
        g2d.drawString(phone, WordTools.buildLeftMargin(phone), 120);
        g2d.drawString(line, 0, 130);

        g2d.drawString(buildCachier(), WordTools.buildLeftMargin(buildCachier()), 150);
        g2d.drawString(buildDay(), WordTools.buildLeftMargin(buildDay()), 175);
        g2d.drawString(buildTime(), WordTools.buildLeftMargin(buildTime()), 190);

        g2d.drawString(line, 0, 210);
        String [] address = WordTools.rebuildStr(getAddress(), 11);
        for(int i=0;i<address.length;i++) {
            String a = address[i];
            g2d.drawString(a, 0, 230+(i*20));
        }

        g2d.drawString(tel, WordTools.buildLeftMargin(tel), 230+address.length*20);
        g2d.drawString(tel, WordTools.buildLeftMargin(tel), 230+address.length*20);

        String [] infoArray = WordTools.rebuildStr(info, 11);
        for(int i=0;i<infoArray.length;i++) {
            String inf = infoArray[i];
            g2d.drawString(inf, 0, 250+address.length*20+(i*20));
        }

        return PAGE_EXISTS;
    }

    private String buildCachier() {
        return "收银员："+cashierName;
    }

    private String buildDay() {
        if(waitCount>0) {
            return "等待 "+waitCount+" 桌";
        } else {
            return "用餐时间："+day;
        }
    }

    private String buildTime() {
        if(waitCount>0) {
            return "预计用餐时间："+aboutTime;
        } else {
            return time;
        }
    }

    public Integer getWaitCount() {
        return waitCount;
    }

    public void setWaitCount(Integer waitCount) {
        this.waitCount = waitCount;
    }

    public String getAboutTime() {
        return aboutTime;
    }

    public void setAboutTime(String aboutTime) {
        this.aboutTime = aboutTime;
    }

    private String buildPeopleStr() {
        return "成人 "+this.peopleCount+" 位";
    }

    private String buildChildStr() {
        return "儿童 "+this.childCount+" 位";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(Integer peopleCount) {
        this.peopleCount = peopleCount;
    }

    public Integer getChildCount() {
        return childCount;
    }

    public void setChildCount(Integer childCount) {
        this.childCount = childCount;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
