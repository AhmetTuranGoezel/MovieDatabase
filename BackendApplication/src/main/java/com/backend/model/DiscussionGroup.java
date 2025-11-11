package com.backend.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name ="discussionGroups")
public class DiscussionGroup {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // identity
    private int groupId;

    private String groupName;

    @Lob
    private byte[] groupImg;

    @ManyToMany
    @JoinColumn(name="account_id",referencedColumnName = "account_id")
    private Set<User> groupMember;

    private int visible;


    public DiscussionGroup() {
    }

    public DiscussionGroup(String groupName, byte[] groupImg, Set<User> groupMember, int visible) {
        this.groupName = groupName;
        this.groupImg = groupImg;
        this.groupMember = groupMember;
        this.visible = visible;
    }

    public DiscussionGroup(int groupId, String groupName, byte[] groupImg, Set<User> groupMember, int visible) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupImg = groupImg;
        this.groupMember = groupMember;
        this.visible = visible;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public byte[] getGroupImg() {
        return groupImg;
    }

    public void setGroupImg(byte[] groupImg) {
        this.groupImg = groupImg;
    }

    public Set<User> getGroupMember() {
        return groupMember;
    }

    public void setGroupMember(Set<User> groupMember) {
        this.groupMember = groupMember;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }
}
