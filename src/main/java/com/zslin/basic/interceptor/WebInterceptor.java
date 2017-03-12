package com.zslin.basic.interceptor;

import com.zslin.basic.dto.AuthToken;
import com.zslin.basic.exception.SystemException;
import com.zslin.basic.service.IAppConfigService;
import com.zslin.model.Worker;
import com.zslin.tools.WorkerCookieTools;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class WebInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
        WorkerCookieTools workerCookieTools = (WorkerCookieTools) factory.getBean("workerCookieTools");
        Worker w = workerCookieTools.getWorker(request);
        if(w==null) {
            response.sendRedirect(request.getContextPath()+"/public/login");
            return false;
        } else {
            workerCookieTools.setWorker(response, request, w);
        }
        return super.preHandle(request, response, handler);
    }
}