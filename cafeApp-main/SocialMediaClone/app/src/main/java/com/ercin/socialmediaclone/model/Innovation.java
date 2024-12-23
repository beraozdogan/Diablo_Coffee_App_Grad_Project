package com.ercin.socialmediaclone.model;

import com.google.firebase.Timestamp;

public class Innovation {

    private String innovationName;
    private String innovationPhotoUrl;
    private String innovationContent;
    private Timestamp signUpTime;

    public Innovation(){

    }


    public Innovation (String innovationName, String innovationPhotoUrl, String innovationContent, Timestamp signUpTime){
        this.setInnovationName(innovationName);
        this.setInnovationPhotoUrl(innovationPhotoUrl);
        this.setInnovationContent(innovationContent);
        this.setSignUpTime(signUpTime);
    }
    public Innovation (String innovationName, String innovationPhotoUrl, String innovationContent){
        this.setInnovationName(innovationName);
        this.setInnovationPhotoUrl(innovationPhotoUrl);
        this.setInnovationContent(innovationContent);
    }


    public String getInnovationName() {
        return innovationName;
    }

    public void setInnovationName(String innovationName) {
        this.innovationName = innovationName;
    }

    public String getInnovationPhotoUrl() {
        return innovationPhotoUrl;
    }

    public void setInnovationPhotoUrl(String innovationPhotoUrl) {
        this.innovationPhotoUrl = innovationPhotoUrl;
    }

    public String getInnovationContent() {
        return innovationContent;
    }

    public void setInnovationContent(String innovationContent) {
        this.innovationContent = innovationContent;
    }

    public Timestamp getSignUpTime() {
        return signUpTime;
    }

    public void setSignUpTime(Timestamp signUpTime) {
        this.signUpTime = signUpTime;
    }
}
