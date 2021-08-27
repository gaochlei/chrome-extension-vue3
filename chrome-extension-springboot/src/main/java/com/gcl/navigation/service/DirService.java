package com.gcl.navigation.service;

import com.gcl.navigation.domain.Dir;
import com.gcl.navigation.domain.DirExample;
import com.gcl.navigation.domain.Website;
import com.gcl.navigation.domain.WebsiteExample;
import com.gcl.navigation.exception.BusinessException;
import com.gcl.navigation.exception.BusinessExceptionCode;
import com.gcl.navigation.mapper.DirMapper;
import com.gcl.navigation.req.DirQueryReq;
import com.gcl.navigation.req.DirSaveReq;
import com.gcl.navigation.req.WebsiteSaveReq;
import com.gcl.navigation.resp.DirQueryResp;
import com.gcl.navigation.resp.WebsiteQueryResp;
import com.gcl.navigation.util.CopyUtil;
import com.gcl.navigation.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class DirService {

    private static final Logger LOG = LoggerFactory.getLogger(DirService.class);

    @Resource
    private DirMapper dirMapper;

    @Resource
    WebsiteService websiteService;

    @Resource
    private SnowFlake snowFlake;

    /**
     * @Description: 网站保存
     * @Param:
     * @return:
     * @Author: gaochlei
     * @Date: 2021/7/25
     */
    public void save(List<DirSaveReq> list) {
        for (DirSaveReq dirSaveReq : list) {
            Dir dirDb = selectDirById(dirSaveReq.getId());
            Dir dir = CopyUtil.copy(dirSaveReq, Dir.class);
            if (ObjectUtils.isEmpty(dirDb)) {
                dirMapper.insert(dir);
            } else {
                dirMapper.updateByPrimaryKey(dir);
            }
        }
    }


    public Dir selectDirById(Long id) {
        DirExample dirExample = new DirExample();
        DirExample.Criteria criteria = dirExample.createCriteria();
        criteria.andIdEqualTo(id);
        List<Dir> dirs = dirMapper.selectByExample(dirExample);
        if (CollectionUtils.isEmpty(dirs)) {
            return null;
        } else {
            return dirs.get(0);
        }
    }

    public List<DirQueryResp> searchAllDir(Long userId) {
        DirExample dirExample = new DirExample();
        DirExample.Criteria criteria = dirExample.createCriteria();
        List<Long> userIds = new ArrayList<>();
        userIds.add(userId);
        userIds.add(0L);
        criteria.andUserIn(userIds);
        List<Dir> dirList = dirMapper.selectByExample(dirExample);

        if (CollectionUtils.isEmpty(dirList)) {
            throw new BusinessException(BusinessExceptionCode.WEBSITE_EMPTY);
        } else {
            List<DirQueryResp> dirQueryResps = CopyUtil.copyList(dirList, DirQueryResp.class);
            List<Website> websites = null;
            for (DirQueryResp dir : dirQueryResps) {
                if (dir.getUser() != 0) {
                    websites = websiteService.selectByParentId(dir.getId());
                } else {
                    websites = websiteService.selectByParentIdAndUserId(dir.getId(), userId);
                }
                if (CollectionUtils.isEmpty(websites)) {
                    List<WebsiteQueryResp> websiteQueryResps = new ArrayList<>();
                    dir.setWebsiteList(websiteQueryResps);
                    continue;
                }
                List<WebsiteQueryResp> websiteQueryResps = CopyUtil.copyList(websites, WebsiteQueryResp.class);
                dir.setWebsiteList(websiteQueryResps);
            }
            return dirQueryResps;
        }
    }

    public List<Dir> searchDirsByName(String name){
        DirExample dirExample = new DirExample();
        DirExample.Criteria criteria = dirExample.createCriteria();
        criteria.andNameLike(name);
        List<Dir> dirs = dirMapper.selectByExample(dirExample);
        if (CollectionUtils.isEmpty(dirs)) {
            return null;
        } else {
            return dirs;
        }
    }

    public void deleteDirAndWebsite(List<DirSaveReq> dirs) {
        for (DirSaveReq dirSaveReq : dirs) {
            deleteDir(dirSaveReq.getId());
            if (dirSaveReq.getWebsiteList().size() > 0) {
                for (WebsiteSaveReq websiteSaveReq : dirSaveReq.getWebsiteList()) {
                    websiteService.deleteWebsitById(websiteSaveReq.getId());
                }
            }
        }
    }

    public int deleteDir(Long dirId) {
        return dirMapper.deleteByPrimaryKey(dirId);
    }
}
