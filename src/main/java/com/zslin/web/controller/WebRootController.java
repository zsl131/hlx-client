package com.zslin.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/12 10:08.
 * 入口请求
 */
@Controller
public class WebRootController {
    @GetMapping(value = {"", "index"})
    public String root() {
        return "redirect:/web/index";
    }
}
