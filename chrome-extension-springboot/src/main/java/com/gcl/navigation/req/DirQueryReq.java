package com.gcl.navigation.req;

import com.gcl.navigation.domain.Website;

public class DirQueryReq {
    private Long id;

    private Long user;

    private Long parent;

    private String name;

    private Integer sort;


    @Override
    public String toString() {
        return "DirSaveReq{" +
                "id=" + id +
                ", user=" + user +
                ", parent=" + parent +
                ", name='" + name + '\'' +
                ", sort=" + sort +
                ", websiteSaveReq=" + websiteSaveReq +
                '}';
    }

    public WebsiteSaveReq getWebsiteSaveReq() {
        return websiteSaveReq;
    }

    public void setWebsiteSaveReq(WebsiteSaveReq websiteSaveReq) {
        this.websiteSaveReq = websiteSaveReq;
    }

    private WebsiteSaveReq websiteSaveReq;

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


}