package com.zslin.web.controller;

import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.SecurityUtil;
import com.zslin.model.Company;
import com.zslin.model.Worker;
import com.zslin.service.ICompanyService;
import com.zslin.service.IWorkerService;
import com.zslin.tools.ClientConfigTools;
import com.zslin.tools.PrintTicketTools;
import com.zslin.tools.WorkerCookieTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/12 9:49.
 */
@Controller
@RequestMapping(value = "public")
public class PublicController {

    @Autowired
    private ICompanyService companyService;

    @Autowired
    private ClientConfigTools clientConfigTools;

    @Autowired
    private IWorkerService workerService;

    @Autowired
    private WorkerCookieTools workerCookieTools;

    @Autowired
    private PrintTicketTools printTicketTools;

    @GetMapping(value = "config")
    public String config(Model model, HttpServletRequest request) {
        Company c = companyService.loadOne();
        Integer workerCount = workerService.findCount();
        if(c!=null && workerCount>0) {

            return "redirect:/web/index";
        } else {
            c = c==null? new Company():c;
            model.addAttribute("company", c);
            return "public/config";
        }
    }

    @PostMapping(value = "config")
    public String config(Company company, HttpServletRequest request) {
        company.setUploadUrl("/client/api/upload");
        company.setDownloadUrl("/client/api/download");
        company.setUploadTime(20);
        company.setDownloadTime(20);
        if(company.getBasePort()==null || company.getBasePort()<=0) {company.setBasePort(80);}
        Company c = companyService.loadOne();
        if(c!=null) {
            MyBeanUtils.copyProperties(company, c, new String[]{"id", "token"});
            companyService.save(c);
            clientConfigTools.setCompanyJson(c);
            request.getSession().setAttribute("company", c);
        } else {
            companyService.save(company);
            clientConfigTools.setCompanyJson(company);
            request.getSession().setAttribute("company", company);
        }
        return "redirect:/web/index";
    }

    @GetMapping(value = "login")
    public String login(Model model, HttpServletRequest request) {

        return "public/login";
    }

    @PostMapping(value = "login")
    public String login(Model model, HttpServletRequest request, HttpServletResponse response) {
        try {
            String phone = request.getParameter("phone");
            String password = request.getParameter("password");
            String errMsg = null;
            Worker w = workerService.findByPhone(phone);
            if(w==null) {errMsg = phone+"对应的员工不存在！";}
            else if(!w.getPassword().equals(SecurityUtil.md5(password))) {errMsg = "密码输入不正确";}
            else {
                workerCookieTools.setWorker(response, request, w);
            }
            if(errMsg!=null && !"".equals(errMsg)) {
                model.addAttribute("phone", phone);
                model.addAttribute("errMsg", errMsg);
                return "public/login";
            } else {
                return "redirect:/web/index";
            }
        } catch (NoSuchAlgorithmException e) {
            return "redirect:/public/login";
        }
    }

    @PostMapping(value = "logout")
    public @ResponseBody String logout(HttpServletRequest request, HttpServletResponse response) {
        workerCookieTools.cleanCookie(request, response);
        return "1";
    }

    @PostMapping(value = "printVoucher")
    public @ResponseBody String printVoucher(Integer count) {
        printTicketTools.printVoucher(count);
        return "1";
    }
}
