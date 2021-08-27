package com.gcl.navigation.resp;

import javax.validation.constraints.NotNull;

public class WebsiteQueryResp {
    private Long id;

    private Long userId;

    private String websiteName;

    private String websiteNick;

    private String siteUrl;


    private Long parentId;

    private Integer sort;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName;
    }

    public String getWebsiteNick() {
        return websiteNick;
    }

    public void setWebsiteNick(String websiteNick) {
        this.websiteNick = websiteNick;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }



    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", websiteName=").append(websiteName);
        sb.append(", websiteNick=").append(websiteNick);
        sb.append(", siteUrl=").append(siteUrl);
        sb.append(", parentId=").append(parentId);
        sb.append(", sort=").append(sort);
        sb.append("]");
        return sb.toString();
    }
}