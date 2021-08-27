package com.gcl.navigation.req;

import javax.validation.constraints.NotNull;

public class WebsiteSaveReq {
    private Long id;

    @NotNull(message = "【用户id】不能为空")
    private Long userId;

    private Long parentId;

    @NotNull(message = "【网站名称】不能为空")
    private String websiteName;

    private String websiteNick;

    @NotNull(message = "【网站地址】不能为空")
    private String siteUrl;

    private Boolean nameNick = false;

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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
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

    public Boolean getNameNick() {
        return nameNick;
    }

    public void setNameNick(Boolean nameNick) {
        this.nameNick = nameNick;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", parentId=").append(parentId);
        sb.append(", websiteName=").append(websiteName);
        sb.append(", websiteNick=").append(websiteNick);
        sb.append(", siteUrl=").append(siteUrl);
        sb.append(", nameNick=").append(nameNick);
        sb.append("]");
        return sb.toString();
    }
}