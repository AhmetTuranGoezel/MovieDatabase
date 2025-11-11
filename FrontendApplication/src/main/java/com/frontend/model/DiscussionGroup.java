package com.frontend.model;

import org.apache.commons.io.IOUtils;

import java.util.Base64;
import java.util.List;

public class DiscussionGroup {

    private int groupId;

    private String groupName;

    private String groupImg;

    private List<User> groupMember;

    private int visible;

    public DiscussionGroup() {
    }

    public DiscussionGroup(String groupName, String groupImg, List<User> groupMember) {
        this.groupName = groupName;
        this.groupImg = groupImg;
        this.groupMember = groupMember;
    }

    public DiscussionGroup(int groupId, String groupName, String groupImg, List<User> groupMember) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupImg = groupImg;
        this.groupMember = groupMember;
    }


    public int getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupImg() {
        return groupImg;
    }

    public void setGroupImg(String groupImg) {
        this.groupImg = groupImg;
    }

    public List<User> getGroupMember() {
        return groupMember;
    }

    public void setGroupMember(List<User> groupMember) {
        this.groupMember = groupMember;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }
}
