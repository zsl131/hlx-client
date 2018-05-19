package com.zslin.web.controller;

import com.zslin.model.*;
import com.zslin.service.*;
import com.zslin.tools.SingleCaseTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/12 15:54.
 */
@RestController
@RequestMapping(value = "public/json")
public class PublicJsonController {

    @Autowired
    private IPriceService priceService;

    @Autowired
    private IRulesService rulesService;

    @Autowired
    private IAdminPhoneService adminPhoneService;

    @Autowired
    private IMemberService memberService;

    @Autowired
    private IPrizeService prizeService;

    @Autowired
    private IWalletService walletService;

    @GetMapping(value = "getPrice")
    public Price getPirce() {
        Price p = SingleCaseTools.getInstance().getPrice();
        if(p==null) {
            p = priceService.findOne();
            SingleCaseTools.getInstance().setPrice(p);
        }
        return p;
    }

    @GetMapping(value = "getRules")
    public Rules getRules() {
        Rules r = SingleCaseTools.getInstance().getRules();
        if(r==null) {
            r = rulesService.loadOne();
            SingleCaseTools.getInstance().setRules(r);
        }
        return r;
    }

    @PostMapping(value = "getAdminPhone")
    public AdminPhone getAdminPhone(String phone) {
        AdminPhone ap = adminPhoneService.findByPhone(phone);
        if(ap==null) {ap = new AdminPhone();}
        return ap;
    }

    @PostMapping(value = "getMember")
    public Member getMember(String phone) {
        Member m = memberService.findByPhone(phone);
        if(m==null) {m = new Member();}
        return m;
    }

    @PostMapping(value = "getWallet")
    public Wallet getWallet(String phone) {
        Wallet w = walletService.findByPhone(phone);
        if(w==null) {w = new Wallet();}
        return w;
    }

    @PostMapping(value = "getPrize")
    public List<Prize> getPrize() {
        List<Prize> list = prizeService.findAll();
        return list;
    }
}
