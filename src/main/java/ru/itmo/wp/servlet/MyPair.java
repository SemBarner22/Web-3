package ru.itmo.wp.servlet;

public class MyPair {
    String user;
    String text;
    MyPair(String user, String text) {
        this.user = user;
        this.text = text;
    }
    public String getUser() {
        return user;
    }
    public String getText() {
        return text;
    }
}
