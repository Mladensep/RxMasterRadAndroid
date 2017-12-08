package com.example.mladen.masterradandroid.model;


public class CommentModel {
    public String titleComment;
    public String contentComment;
    public String fbName;
    public String dateTime;
    public int school_id;

    public String getTitleComment() {
        return titleComment;
    }

    public void setTitleComment(String titleComment) {
        this.titleComment = titleComment;
    }

    public String getContentComment() {
        return contentComment;
    }

    public void setContentComment(String contentComment) {
        this.contentComment = contentComment;
    }

    public String getFbName() {
        return fbName;
    }

    public void setFbName(String fbName) {
        this.fbName = fbName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getSchool_id() {
        return school_id;
    }

    public void setSchool_id(int school_id) {
        this.school_id = school_id;
    }
}
