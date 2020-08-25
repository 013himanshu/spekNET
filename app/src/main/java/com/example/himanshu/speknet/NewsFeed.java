package com.example.himanshu.speknet;

public class NewsFeed {
    String userName;
    String PostText;

    public NewsFeed() {
    }

    public NewsFeed(String userName, String postText) {
        this.userName = userName;
        PostText = postText;
    }

    public String getUserName() {
        return userName;
    }

    public String getPostText() {
        return PostText;
    }
}
