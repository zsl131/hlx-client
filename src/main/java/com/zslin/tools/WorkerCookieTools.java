package com.zslin.tools;

import com.zslin.model.Worker;
import com.zslin.service.IWorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/12 14:37.
 * 员工登陆的Cookie工具类
 */
@Component("workerCookieTools")
public class WorkerCookieTools {

    private static final String WORKER_NAME = "login_worker";

    @Autowired
    private IWorkerService workerService;

    public Worker getWorker(HttpServletRequest request) {
        Worker w = (Worker) request.getSession().getAttribute(WORKER_NAME);
        if(w==null) {w = getWorkerFromCookie(request);}
        return w;
    }

    public void cleanCookie(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().removeAttribute(WORKER_NAME);
        Cookie [] cs = request.getCookies();
        for(Cookie c : cs) {
            c.setMaxAge(0);
            c.setPath("/");
            response.addCookie(c);
        }
    }

    private Worker getWorkerFromCookie(HttpServletRequest request) {
        Cookie [] cs = request.getCookies();
        if(cs==null || cs.length<=0) {return null;}
        for(Cookie c : cs) {
            String name = c.getName();
            if(WORKER_NAME.equals(name)) {
                String phone = c.getValue();
                return workerService.findByPhone(phone);
            }
        }
        return null;
    }

    public void setWorker(HttpServletResponse response, HttpServletRequest request, Worker worker) {
        Cookie c = new Cookie(WORKER_NAME, worker.getPhone());
        c.setMaxAge(10*60*60); //10个小时
        c.setPath("/");
        response.addCookie(c);
        request.getSession().setAttribute(WORKER_NAME, worker);
    }
}
