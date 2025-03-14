package com.wixsite.mupbam1.models;

public class PictureUploadRequest {
    private String urls;
    private String description;

    // Геттеры и сеттеры
    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
