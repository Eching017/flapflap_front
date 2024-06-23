package com.example.flapflap_front.model;

public class User {
    private int id;
    private String name;
    private String password; // 通常在前端不会使用到密码，此处仅作为示例
    private String nickname;
    private String gender;
    private String birth;
    private String avatar;
    private String sign;

    // 无参构造方法
    public User() {}

    // 带参数的构造方法
    public User(int id, String name, String password, String nickname, String gender, String birth, String avatar, String sign) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.nickname = nickname;
        this.gender = gender;
        this.birth = birth;
        this.avatar = avatar;
        this.sign = sign;
    }

    // Getter 和 Setter 方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}


