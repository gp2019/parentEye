package com.example.parenteye;

public class GroupRequests {
    private String id;
    private String GroupId; // dh el id eli byshawer 3la randomkey fe gadwel community
    private String Userid;
   // private Integer state;   //o if rejected , 1 if pending

    public GroupRequests(){

    }

    public GroupRequests(String id, String groupId, String userid) {
        this.id = id;
        GroupId = groupId;
        Userid = userid;

    }

    public void setId(String id) {
        this.id = id;
    }

    public void setGroupId(String groupId) {
        GroupId = groupId;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }



    public String getId() {
        return id;
    }

    public String getGroupId() {
        return GroupId;
    }

    public String getUserid() {
        return Userid;
    }


}
