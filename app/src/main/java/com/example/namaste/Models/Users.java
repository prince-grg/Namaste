package com.example.namaste.Models;

public class Users {
    String  profilepic,username,lastmsg,userId,mail,password;

    public Users(String profilepic, String username, String lastmsg, String userId, String mail, String password) {
        this.profilepic = profilepic;
        this.username = username;
        this.lastmsg = lastmsg;
        this.userId = userId;
        this.mail = mail;
        this.password = password;
    }
    public Users(){}

    public Users(String username, String mail, String password) {
        this.username = username;
        this.mail = mail;
        this.password = password;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastmsg() {
        return lastmsg;
    }

    public void setLastmsg(String lastmsg) {
        this.lastmsg = lastmsg;
    }
    public String getUserId() {
        return userId;
    }
    public String getUserId(String key) {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
