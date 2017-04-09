package com.zslin.web.controller;

import com.zslin.basic.tools.SecurityUtil;
import com.zslin.model.Company;
import com.zslin.model.Worker;
import com.zslin.service.ICompanyService;
import com.zslin.service.IWorkerService;
import com.zslin.tools.WorkerCookieTools;
import com.zslin.upload.tools.UploadFileTools;
import com.zslin.upload.tools.UploadJsonTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/12 9:55.
 * 准备开始，即登陆
 */
@Controller
@RequestMapping(value = "web")
public class WebController {

    @Autowired
    private ICompanyService companyService;

    @Autowired
    private IWorkerService workerService;

    @Autowired
    private WorkerCookieTools workerCookieTools;

    @Autowired
    private UploadFileTools uploadFileTools;

    @GetMapping(value = {"index", "", "/"})
    public String ready(Model model, HttpServletRequest request) {
        Company c = companyService.loadOne();
        Integer workerCcount = workerService.findCount();
        if(c==null || workerCcount<=0) {
            return "redirect:/public/config";
        } else {
            model.addAttribute("company", c);
            request.getSession().setAttribute("company", c);
//            return "web/index";
            return "redirect:/web/newOrders/index";
        }
    }

    /** 修改密码 */
    @RequestMapping(value="updatePwd")
    public String updatePwd(Model model, Integer flag, String oldPwd, String password, String nickname, HttpServletRequest request) {
        String method = request.getMethod(); //获取请求方式，GET、POST
        if("get".equalsIgnoreCase(method)) {
            model.addAttribute("flag", flag);
            return "web/updatePwd";
        } else if("post".equalsIgnoreCase(method)) {
            Worker w = workerCookieTools.getWorker(request);
            try {
                if(password!=null && !"".equals(password)) { //如果没有输入密码，则不修改
                    if(!SecurityUtil.md5(oldPwd).equals(w.getPassword())) {
                        model.addAttribute("errorMsg", "原始密码输入错误");
                        return "web/updatePwd";
                    }
                    w.setPassword(SecurityUtil.md5(password));
                }
                workerService.save(w);
                send2Server(w); //发送到服务端
                return "redirect:/web/updatePwd?flag=1";
            } catch (Exception e) {
                //e.printStackTrace();
                return "redirect:/web/updatePwd?flag=2";
            }
        }
        return "redirect:/web/updatePwd";
    }

    private void send2Server(Worker w) {
        String content = UploadJsonTools.buildDataJson(UploadJsonTools.buildWorkerJson(w));
        uploadFileTools.setChangeContext(content, true);
    }
}
