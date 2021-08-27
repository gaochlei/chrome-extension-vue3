package com.gcl.navigation.controller;

import com.gcl.navigation.domain.Dir;
import com.gcl.navigation.exception.BusinessException;
import com.gcl.navigation.exception.BusinessExceptionCode;
import com.gcl.navigation.req.DirSaveReq;
import com.gcl.navigation.resp.CommonResp;
import com.gcl.navigation.resp.DirQueryResp;
import com.gcl.navigation.service.DirService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/dir")
public class DirController {
    private static final Logger LOG = LoggerFactory.getLogger(DirService.class);
    @Resource
    private DirService dirService;

    @PostMapping("/add")
    public CommonResp add(@Valid @RequestBody List<DirSaveReq> dirSaveReqList){
        CommonResp resp = new CommonResp();
        dirService.save(dirSaveReqList);
        Long userId = dirSaveReqList.get(dirSaveReqList.size() -1).getUser();
        List<DirQueryResp> dirs = dirService.searchAllDir(userId);
        resp.setContent(dirs);
        resp.setMessage("新增成功");
        return resp;
    }

    @GetMapping("reqDirs/{userId}")
    public CommonResp reqDirs(@PathVariable Long userId){
        CommonResp resp = new CommonResp();
        try {
            List<DirQueryResp> dirs = dirService.searchAllDir(userId);
            resp.setContent(dirs);
        }catch (BusinessException e){
            resp.setSuccess(false);
        }
        return resp;
    }

    @PostMapping("/deleteDirs")
    public CommonResp deleteDirs(@Valid @RequestBody List<DirSaveReq> dirSaveReqList){
        CommonResp resp = new CommonResp();
        dirService.deleteDirAndWebsite(dirSaveReqList);
        resp.setMessage("删除成功");
        return  resp;
    }
}
