package com.example.achuan.newsimooc.model.bean;

/**
 * Created by achuan on 16-10-3.
 * 功能：列表中显示内容的实体类
 */
public class NewsBean {
    private String newsIconUrl;//图片的访问链接
    private String newsTitle;//标题
    private String newsContent;//内容
    public NewsBean(String newsIconUrl, String newsTitle, String newsContent) {
        this.newsIconUrl = newsIconUrl;
        this.newsTitle = newsTitle;
        this.newsContent = newsContent;
    }

    public String getNewsIconUrl() {
        return newsIconUrl;
    }

    public void setNewsIconUrl(String newsIconUrl) {
        this.newsIconUrl = newsIconUrl;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsContent() {
        return newsContent;
    }

    public void setNewsContent(String newsContent) {
        this.newsContent = newsContent;
    }
}
