package com.example.flapflap_front.model;

public class Community {
    private int id;
    private String gameName;
    private String icon;

    public Community(int id, String gameName, String icon) {
        this.id = id;
        this.gameName = gameName;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public String getGameName() {
        return gameName;
    }

    public String getIcon() {
        return icon;
    }
}
