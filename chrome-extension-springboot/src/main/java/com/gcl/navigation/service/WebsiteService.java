package com.gcl.navigation.service;

import com.gcl.navigation.domain.Website;
import com.gcl.navigation.domain.WebsiteExample;
import com.gcl.navigation.exception.BusinessException;
import com.gcl.navigation.exception.BusinessExceptionCode;
import com.gcl.navigation.mapper.WebsiteMapper;
import com.gcl.navigation.req.WebsiteSaveReq;
import com.gcl.navigation.util.CopyUtil;
import com.gcl.navigation.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class WebsiteService {

    private static final Logger LOG = LoggerFactory.getLogger(WebsiteService.class);

    @Resource
    private WebsiteMapper websiteMapper;

    @Resource
    private SnowFlake snowFlake;

    /**
     * @Description: 网站保存
     * @Param:
     * @return:
     * @Author: gaochlei
     * @Date: 2021/7/25
     */
    public void save(WebsiteSaveReq req) {
        Website websiteDb = selectByUrlAndUserId(req.getSiteUrl(), req.getUserId());
//        if (ObjectUtils.isEmpty(websiteDb)) {
//        req.setId(snowFlake.nextId());
        websiteDb = CopyUtil.copy(req, Website.class);
        websiteMapper.insert(websiteDb);
//        } else {
//            LOG.info("该网站已被保存 {}", req.getSiteUrl());
//            throw new BusinessException(BusinessExceptionCode.WEBSITE_SAVE_EXIST);
//        }
    }

    public void updateWebs(List<WebsiteSaveReq> websiteSaveReqs) {
        if (websiteSaveReqs.size() == 0) {
            throw new BusinessException(BusinessExceptionCode.WEBSITE_EMPTY);
        }
        for (WebsiteSaveReq websiteSaveReq : websiteSaveReqs) {
            Website website = CopyUtil.copy(websiteSaveReq, Website.class);
            websiteMapper.updateByPrimaryKey(website);
        }
    }

    public Website selectByUrlAndUserId(String url, Long userId) {
        WebsiteExample websiteExample = new WebsiteExample();
        WebsiteExample.Criteria criteria = websiteExample.createCriteria();
        criteria.andSiteUrlEqualTo(url);
        criteria.andUserIdEqualTo(userId);
        List<Website> websites = websiteMapper.selectByExample(websiteExample);
        if (CollectionUtils.isEmpty(websites)) {
            return null;
        } else {
            return websites.get(0);
        }
    }

    public List<Website> searchWebsitesByName(String name){
        WebsiteExample websiteExample = new WebsiteExample();
        WebsiteExample.Criteria criteria = websiteExample.createCriteria();
        criteria.andWebsiteNameLike(name);
        List<Website> websites = websiteMapper.selectByExample(websiteExample);
        if (CollectionUtils.isEmpty(websites)) {
            return null;
        } else {
            return websites;
        }
    }

    public Website selectByUrl(String url) {
        WebsiteExample websiteExample = new WebsiteExample();
        WebsiteExample.Criteria criteria = websiteExample.createCriteria();
        criteria.andSiteUrlEqualTo(url);
        List<Website> websites = websiteMapper.selectByExample(websiteExample);
        if (CollectionUtils.isEmpty(websites)) {
            return null;
        } else {
            return websites.get(0);
        }
    }

    public List<Website> searchAllSite(Long userId) {
        WebsiteExample websiteExample = new WebsiteExample();
        WebsiteExample.Criteria criteria = websiteExample.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<Website> websiteList = websiteMapper.selectByExample(websiteExample);
        if (CollectionUtils.isEmpty(websiteList)) {
            throw new BusinessException(BusinessExceptionCode.WEBSITE_EMPTY);
        } else {
            return websiteList;
        }
    }

    public List<Website> selectByParentId(Long parentId) {
        WebsiteExample websiteExample = new WebsiteExample();
        WebsiteExample.Criteria criteria = websiteExample.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<Website> websiteList = websiteMapper.selectByExample(websiteExample);
        return websiteList;
    }

    public List<Website> selectByParentIdAndUserId(Long parentId, Long userId) {
        WebsiteExample websiteExample = new WebsiteExample();
        WebsiteExample.Criteria criteria = websiteExample.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        criteria.andUserIdEqualTo(userId);
        List<Website> websiteList = websiteMapper.selectByExample(websiteExample);
        return websiteList;
    }

    public void deleteWebsites(List<WebsiteSaveReq> websiteSaveReqs) {
        if (websiteSaveReqs.size() == 0) {
            return;
        } else {
            for (WebsiteSaveReq websiteSaveReq : websiteSaveReqs) {
                deleteWebsitById(websiteSaveReq.getId());
            }
        }
    }

    public int deleteWebsitById(Long id) {
        return websiteMapper.deleteByPrimaryKey(id);
    }
}
