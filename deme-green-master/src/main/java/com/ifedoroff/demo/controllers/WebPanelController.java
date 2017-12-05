package com.ifedoroff.demo.controllers;

import com.ifedoroff.demo.tools.ResponseContextUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * Created by Rostik on 27.07.2017.
 */
@Controller
public class WebPanelController {

    @GetMapping("/login")
    public String login() {
        return "/login";
    }

    @RequestMapping("/")
    public String index(Map<String, Object> model) {
        model.put("contextJson", ResponseContextUtil.buildInitContext());
        return "/web-panel-user";
    }

    @RequestMapping("/web-panel-user")
    public String userWebPanelAccess(Map<String, Object> model) {
        model.put("contextJson", ResponseContextUtil.buildInitContext());
        return "/web-panel-user";
    }

    @GetMapping("/web-panel-admin")
    public String adminWebPanelAccess() {
        return "/web-panel-admin";
    }

    @GetMapping("/403")
    public String error403() {
        return "/error/403";
    }
}
