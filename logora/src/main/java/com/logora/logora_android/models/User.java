package com.logora.logora_android.models;

public class User {
    private String fullName;
    private String slug;
    private String uid;
    private String imageUrl;
    private Integer debatesCount;
    private Integer votesCount;
    private Integer disciplesCount;

    public User() {}

    public String getFullName() { return fullName; }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSlug() { return slug; }

    public void setSlug(String slug) { this.slug = slug; }

    public String getUid() { return uid; }

    public void setUid(String uid) { this.uid = uid; }

    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Integer getDebatesCount() {
        return debatesCount;
    }

    public void setDebatesCount(Integer debatesCount) {
        this.debatesCount = debatesCount;
    }

    public Integer getVotesCount() {
        return votesCount;
    }

    public void setVotesCount(Integer votesCount) {
        this.votesCount = votesCount;
    }

    public Integer getDisciplesCount() {
        return disciplesCount;
    }

    public void setDisciplesCount(Integer disciplesCount) {
        this.disciplesCount = disciplesCount;
    }
}
