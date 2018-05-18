package com.zslin.web.controller;

import com.zslin.basic.annotations.Token;
import com.zslin.basic.exception.SystemException;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.NormalTools;
import com.zslin.basic.tools.TokenTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.model.*;
import com.zslin.service.IMemberChargeService;
import com.zslin.service.IMemberLevelService;
import com.zslin.service.IMemberService;
import com.zslin.tools.WorkerCookieTools;
import com.zslin.upload.tools.UploadFileTools;
import com.zslin.upload.tools.UploadJsonTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/17 23:39.
 * 会员管理，只显示在店内办理的会员信息
 */
@Controller
@RequestMapping(value = "web/member")
public class MemberController {

    @Autowired
    private IMemberService memberService;

    @Autowired
    private IMemberLevelService memberLevelService;

    @Autowired
    private IMemberChargeService memberChargeService;

    @Autowired
    private WorkerCookieTools workerCookieTools;

    @Autowired
    private UploadFileTools uploadFileTools;

    /**
     * 初始化会员支付密码
     * @param phone
     * @return
     */
    @PostMapping(value = "initPwd")
    public @ResponseBody String initPwd(String phone) {
        String password = "0000";
        memberService.updatePassword(password, phone);
        sendPassword2Server(phone, password);
        return "1";
    }

    public void sendPassword2Server(String phone, String password) {
        String content = UploadJsonTools.buildDataJson(UploadJsonTools.buildPassword(phone, password));
        uploadFileTools.setChangeContext(content, true);
    }

    @GetMapping(value = "list")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<Member> datas = memberService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("surplus_d")));
        model.addAttribute("datas", datas);
        return "web/member/list";
    }

    @GetMapping(value = "listCharges")
    public String listCharges(Model model, Integer page, HttpServletRequest request) {
        Page<MemberCharge> datas = memberChargeService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        return "web/member/listCharges";
    }

    @Token(flag= Token.READY)
    @GetMapping(value = "add")
    public String add(Model model, Integer id, HttpServletRequest request) {
        model.addAttribute("levelList", memberLevelService.findAll());
        Member m = null;
        if(id!=null && id>0) {
            m = memberService.findOne(id);
            if(m==null) {m = new Member();}
        } else {
            m = new Member();
        }
        model.addAttribute("member", m);
        return "web/member/add";
    }

    @Token(flag= Token.CHECK)
    @PostMapping(value="add")
    public String add(Integer levelId, String payType, Member member, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) {
            Worker w = workerCookieTools.getWorker(request);
            if(w==null) {throw new SystemException("未检测到收银员");}
            Member m = memberService.findByPhone(member.getPhone());
            if(m==null) {
                member.setCreateDay(NormalTools.curDate("yyyy-MM-dd"));
                member.setCachierName(w.getName());
                member.setCachierPhone(w.getPhone());
                member.setCreateLong(System.currentTimeMillis());
                member.setCreateTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
                memberService.save(member);
                sendMember2Server(member);

                addMemberCharge(levelId, member, payType);
            } else {
                addMemberCharge(levelId, m, payType);
            }
        }
        return "redirect:/web/member/listCharges";
    }

    private void addMemberCharge(Integer levelId, Member m, String payType) {
        MemberLevel ml = memberLevelService.findOne(levelId);
        MemberCharge mc = new MemberCharge();
        mc.setCreateTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
        mc.setCreateDay(NormalTools.curDate("yyyy-MM-dd"));
        mc.setBalance(ml.getChargeMoney()+ml.getGiveMoney());
        mc.setChargeMoney(ml.getChargeMoney()*1.0f);
        mc.setCreateLong(System.currentTimeMillis());
        mc.setGiveMoney(ml.getGiveMoney()*1.0f);
        mc.setLevel(levelId);
        mc.setName(m.getName());
        mc.setPhone(m.getPhone());
        mc.setStatus("1");
        mc.setPayType(payType);

        Integer old = m.getSurplus();
        old = old==null?0:old;
        m.setSurplus(old+ml.getGiveMoney()*100+ml.getChargeMoney()*100);
        mc.setTotalBalance(m.getSurplus()/100);
        memberService.save(m);

        memberChargeService.save(mc);
        sendMemberCharge2Server(mc);
    }

    private void sendMember2Server(Member m) {
        String content = UploadJsonTools.buildDataJson(UploadJsonTools.buildMemberJson(m));
        uploadFileTools.setChangeContext(content, true);
    }

    public void sendMemberCharge2Server(MemberCharge mc) {
        String content = UploadJsonTools.buildDataJson(UploadJsonTools.buildMemberChargeJson(mc));
        uploadFileTools.setChangeContext(content, true);
    }
}
