package com.example.flapflap_front.model;

public class Reply {
    private String id;
    private String content;
    private long timestamp;
    private int likes;
    private User user;

    public Reply(String id, String content, long timestamp, int likes, User user) {
        this.id = id;
        this.content = content;
        this.timestamp = timestamp;
        this.likes = likes;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

