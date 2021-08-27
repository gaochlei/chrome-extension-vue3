package com.gcl.navigation.controller;

import com.gcl.navigation.domain.Website;
import com.gcl.navigation.exception.BusinessException;
import com.gcl.navigation.exception.BusinessExceptionCode;
import com.gcl.navigation.req.WebsiteSaveReq;
import com.gcl.navigation.resp.CommonResp;
import com.gcl.navigation.service.WebsiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/website")
public class WebsiteController {
    private static final Logger LOG = LoggerFactory.getLogger(WebsiteService.class);
    @Resource
    private WebsiteService websiteService;

    @PostMapping("/add")
    public CommonResp add(@Valid @RequestBody WebsiteSaveReq req) {
        CommonResp resp = new CommonResp();
        try {
            req.setNameNick(false);
            websiteService.save(req);
            resp.setMessage("添加成功");
        }catch (Exception e){
            if(e instanceof BusinessException) {
                resp.setMessage(BusinessExceptionCode.WEBSITE_SAVE_EXIST.getDesc());
            }else{
                LOG.info("Exception ==== ",e.toString());
                resp.setMessage("数据异常");
            }
            resp.setSuccess(false);
        }
        return resp;
    }

    @PostMapping("/update")
    public CommonResp update(@Valid @RequestBody List<WebsiteSaveReq> websiteSaveReqs){
        CommonResp commonResp = new CommonResp();
        websiteService.updateWebs(websiteSaveReqs);
        commonResp.setMessage("修改成功");
        return commonResp;
    }

    @PostMapping("/delete")
    public CommonResp delete(@Valid @RequestBody List<WebsiteSaveReq> websiteSaveReqs){
        CommonResp commonResp = new CommonResp();
        websiteService.deleteWebsites(websiteSaveReqs);
        commonResp.setMessage("删除成功");
        return commonResp;
    }

    @GetMapping("list/{id}")
    public CommonResp list(@PathVariable Long id){
        CommonResp resp = new CommonResp();
        try {
            List<Website> websites = websiteService.searchAllSite(id);
            resp.setContent(websites);
        }catch (BusinessException e){
            resp.setSuccess(false);
            resp.setMessage(BusinessExceptionCode.WEBSITE_EMPTY.getDesc());
        }
        return resp;
    }
}
