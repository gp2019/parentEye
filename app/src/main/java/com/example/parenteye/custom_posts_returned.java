package com.example.parenteye;

import java.util.Date;

public class custom_posts_returned {
    private String post_Id;
    private int countComment;
    private int countLike;
    private String post_owner_name;
    private String post_text;
    private String post_owner_ID;
    private String post_image;
    private String post_date;
    private String placeTypeId;
    private String communityId;

    // private  static  final String noimage="-1";



    public custom_posts_returned(){

    }

    public String getPost_Id() {
        return post_Id;
    }

    public void setPost_Id(String post_Id) {
        this.post_Id = post_Id;
    }

    public custom_posts_returned(String post_text, String post_owner_ID, String post_image, String post_date, String post_owner_name,String placeTypeId,String communityId) {
        this.post_text = post_text;
        this.post_owner_ID = post_owner_ID;
        this.post_image = post_image;
        this.post_date = post_date;
        this.post_owner_name=post_owner_name;
        this.placeTypeId=placeTypeId;
        this.communityId=communityId;
    }

    public void setPost_owner_name(String post_owner_name) {
        this.post_owner_name = post_owner_name;
    }

    public String getPlaceTypeId() {
        return placeTypeId;
    }

    public void setPlaceTypeId(String placeTypeId) {
        this.placeTypeId = placeTypeId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getPost_owner_name() {
        return post_owner_name;
    }

    public void setPost_text(String post_text) {
        this.post_text = post_text;
    }

    public void setpost_owner_ID(String profile_image) {
        this.post_owner_ID = profile_image;
    }

    public void setPost_image(String post_image) {
        this.post_image = post_image;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    public String getPost_text() {
        return post_text;
    }

    public String getpost_owner_ID() {
        return post_owner_ID;
    }

    public String getPost_image() {
        return post_image;
    }

    public int getCountComment() {
        return countComment;
    }

    public void setCountComment(int countComment) {
        this.countComment = countComment;
    }

    public int getCountLike() {
        return countLike;
    }

    public void setCountLike(int countLike) {
        this.countLike = countLike;
    }

    public String getPost_date() {
        return post_date;
    }
    public boolean haspost_owner_ID(){
        if(post_owner_ID!=null){
            return true;
        }
        else{
            return false;
        }

    }
    public boolean haspostimage(){
        if(post_image!=null){
            return true;
        }
        else{
            return false;
        }
    }
}
