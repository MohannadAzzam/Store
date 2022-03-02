package com.example.helloworld.Model;

public class User {
    private String password;
    private String phone;
    private String userName;
    private String address;
    private String image;

    public User() {
    }

    public User(String password, String phone, String userName, String address, String image) {
        this.password = password;
        this.phone = phone;
        this.userName = userName;
        this.address = address;
        this.image = image;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
