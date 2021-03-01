package com.logora.logora_android.models;

public class User {
    private String full_name;
    private String slug;
    private String uid;
    private String image_url;

    public User() {}

    public User(String full_name, String slug, String uid) {
        this.full_name = full_name;
        this.slug = slug;
        this.uid = uid;
    }

    public String getFullName() { return full_name; }

    public void setFullName(String full_name) {
        this.full_name = full_name;
    }

    public String getSlug() { return slug; }

    public void setSlug(String slug) { this.slug = slug; }

    public String getUid() { return uid; }

    public void setUid(String uid) { this.uid = uid; }

    public String getImageUrl() { return image_url; }

    public void setImageUrl(String image_url) { this.image_url = image_url; }
}
