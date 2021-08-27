package com.gcl.navigation.controller;

import com.gcl.navigation.domain.Demo;
import com.gcl.navigation.resp.CommonResp;
import com.gcl.navigation.service.DemoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DemoController {
    @Resource
    private DemoService demoService;

    @GetMapping("/demo/list")
    public CommonResp list(){
        CommonResp<List<Demo>> resp = new CommonResp<>();
        List<Demo> list = demoService.list();
        resp.setMessage("请求成功");
        resp.setSuccess(true);
        resp.setContent(list);
        return resp;
    }
}
