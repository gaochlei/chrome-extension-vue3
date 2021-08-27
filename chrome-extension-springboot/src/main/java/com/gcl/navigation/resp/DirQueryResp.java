package com.gcl.navigation.resp;

import com.gcl.navigation.req.WebsiteSaveReq;

import java.util.List;

public class DirQueryResp {
    private Long id;

    private Long user;

    private Long parent;

    private String name;

    private Integer sort;

    private List<WebsiteQueryResp> websiteList;

    public List<WebsiteQueryResp> getWebsiteList() {
        return websiteList;
    }

    public void setWebsiteList(List<WebsiteQueryResp> websiteList) {
        this.websiteList = websiteList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "DirQueryResp{" +
                "id=" + id +
                ", user=" + user +
                ", parent=" + parent +
                ", name='" + name + '\'' +
                ", sort=" + sort +
                ", websiteList=" + websiteList +
                '}';
    }
}