package com.ercin.socialmediaclone.model;

public class MenuItem {

    public String name;
    public String downloadUrl;
    public String comment;


    public MenuItem(String name, String downloadUrl, String comment){
        this.comment = comment;
        this.downloadUrl = downloadUrl;
        this.name = name;
    }
}
