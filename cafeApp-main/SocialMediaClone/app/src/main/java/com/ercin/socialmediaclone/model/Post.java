package com.ercin.socialmediaclone.model;

public class Post {

    public String email;
    public String comment;
    public String downloadUrl;

    public Post (String email, String comment, String downloadUrl){
        this.comment = comment;
        this.downloadUrl = downloadUrl;
        this.email = email;
    }

}
