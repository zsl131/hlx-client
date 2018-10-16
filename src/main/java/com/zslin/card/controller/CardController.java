package com.zslin.card.controller;

import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SpecificationOperator;
import com.zslin.basic.tools.NormalTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.card.dto.CardCheckDto;
import com.zslin.card.model.*;
import com.zslin.card.service.*;
import com.zslin.card.tools.CardNoTools;
import com.zslin.model.Worker;
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
import java.util.List;

/**
 * Created by zsl on 2018/10/15.
 */
@Controller
@RequestMapping(value = "web/card")
public class CardController {

    @Autowired
    private IApplyReasonService applyReasonService;

    @Autowired
    private CardNoTools cardNoTools;

    @Autowired
    private ICardService cardService;

    @Autowired
    private IGrantCardService grantCardService;

    @Autowired
    private UploadFileTools uploadFileTools;

    @Autowired
    private WorkerCookieTools workerCookieTools;

    @Autowired
    private ICardApplyService cardApplyService;

    @Autowired
    private ICardCheckService cardCheckService;

    @PostMapping(value = "listReason")
    public @ResponseBody
    List<ApplyReason> listReason() {
        return applyReasonService.findAll();
    }

    @PostMapping(value = "findCardNo")
    public @ResponseBody Integer findCardNo(String type) {
        return getCardNo(type);
    }

    private Integer getCardNo(String type) {
        List<Integer> nos = cardNoTools.buildAllotNos(type, 1);
        if(nos==null || nos.size()<=0) {return 0;}
        else return nos.get(0);
    }

    @PostMapping(value = "applyCard")
    public @ResponseBody String applyCard(String type, String reason, HttpServletRequest request) {
        Worker w = workerCookieTools.getWorker(request);
        if(w==null) {return "-1";}

        Integer cardNo = getCardNo(type);
        if(cardNo==null || cardNo==0) {return "0";}

        GrantCard gc = grantCardService.findByCardNo(cardNo);
        gc.setStatus("2"); //申请中
        grantCardService.save(gc);
        sendGrantCard2Server(gc);

        CardApply ca = new CardApply();
        ca.setStatus("0");
        ca.setCardType(type);
        ca.setCardNo(cardNo);
        ca.setApplyCreateTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
        ca.setApplyKey(w.getPhone());
        ca.setApplyName(w.getName());
        ca.setApplyReason(reason);
        cardApplyService.save(ca);
        sendCardApply2Server(ca); //

        return "1";
    }

    @GetMapping(value = "listCardApply")
    public String listCardApply(Model model, Integer page, HttpServletRequest request) {
        Page<CardApply> datas = cardApplyService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request,
                new SpecificationOperator("status", "eq", "0")),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        return "web/card/listCardApply";
    }

    @PostMapping(value = "resubmitCardApply")
    public @ResponseBody String resubmitCardApply(Integer id) {
        CardApply cardApply = cardApplyService.findOne(id);
        sendCardApply2Server(cardApply);
        return "1";
    }

    public void sendGrantCard2Server(GrantCard obj) {
        String content = UploadJsonTools.buildDataJson(UploadJsonTools.buildGrantCard(obj));
        uploadFileTools.setChangeContext(content, true);
    }

    public void sendCardApply2Server(CardApply obj) {
        String content = UploadJsonTools.buildDataJson(UploadJsonTools.buildCardApply(obj));
        uploadFileTools.setChangeContext(content, true);
    }

    @PostMapping(value = "queryCard")
    public @ResponseBody String queryCard(Integer cardNo) {
        Card card = cardService.findByNo(cardNo);
        String res = "1";
        if(card==null) {res = "查无此卡";}
        else if("2".equals(card.getStatus())) {res = "此卡已作废";}
        else if("1".equals(card.getStatus())) {res = "此卡已使用";}
        return res;
    }

    @GetMapping(value = "listCardCheck")
    public String listCardCheck(Model model, Integer page, HttpServletRequest request) {
        Page<CardCheck> datas = cardCheckService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        return "web/card/listCardCheck";
    }

    @GetMapping(value = "listCardCheckDto")
    public String listCardCheckDto(Model model, String month, String day, HttpServletRequest request) {
        List<CardCheckDto> list;
//        System.out.println("===day:"+day+"====month:"+month+"==="+(month==null && day==null)+"====="+(day!=null));
        if(month==null && day==null) {
            day = NormalTools.curDate("yyyyMMdd"); month = NormalTools.curDate("yyyyMM");
            list = cardCheckService.findCheckDtoByDay(day);
        } else if(day!=null) {
            day = day.replace("eq-", "");
            list = cardCheckService.findCheckDtoByDay(day);
        } else {
            month = month.replace("eq-", "");
            list = cardCheckService.findCheckDtoByMonth(month);
        }
        day = day==null?"":day.replace("eq-", "");
        month = month==null?"":month.replace("eq-", "");
        model.addAttribute("day", day);
        model.addAttribute("month", month);
        model.addAttribute("list", list);
        return "web/card/listCardCheckDto";
    }
}
