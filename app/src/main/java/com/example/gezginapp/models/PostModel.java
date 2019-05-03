package com.example.gezginapp.models;

public class PostModel {

    String postId;
    String postUrl;
    String postName;
    String postDescription;

    public PostModel(){

    }

    public PostModel(String postId, String postUrl, String postName, String postDescription) {
        this.postId = postId;
        this.postUrl = postUrl;
        this.postName = postName;
        this.postDescription = postDescription;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

}
