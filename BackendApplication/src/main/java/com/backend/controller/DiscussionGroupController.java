package com.backend.controller;

import com.backend.model.DiscussionGroup;
import com.backend.model.User;
import com.backend.service.DiscussionGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
public class DiscussionGroupController {

    @Autowired
    DiscussionGroupService discussionGroupService;

    @PostMapping(value = "/discussiongroup/create/")
    public ResponseEntity<String> createDiscussionGroup(@RequestBody DiscussionGroup discussionGroup) {

        return discussionGroupService.createDiscussionGroup(discussionGroup);

    }

    @PostMapping(value= "/discussiongroup/{groupId}/{userId}/")
    public ResponseEntity<String> addUserToDiscussionGroup(@PathVariable("groupId") int groupId,@PathVariable("userId") int userId) {

        return discussionGroupService.addUserToDiscussionGroup(groupId,userId);
    }

    @PutMapping(value="/discussiongroup/update/")
    public ResponseEntity<String> editDiscussionGroup(@RequestBody DiscussionGroup discussionGroup) {

       return discussionGroupService.editDiscussionGroup(discussionGroup);
    }

    @DeleteMapping(value = "/discussiongroups/delete/{groupId}")
    public ResponseEntity<String> deleteDiscussionGroup(@PathVariable("groupId") int groupId) {

        return discussionGroupService.deleteDiscussionGroup(groupId);
    }


    @GetMapping(value="/discussiongroups/{id}")
    public ResponseEntity<DiscussionGroup> getDiscussionGroupById(@PathVariable("id") int id) {

        return discussionGroupService.getDiscussionGroupById(id);
    }

    @GetMapping("/discussiongroup/member/{groupId}")
    public ResponseEntity<Set<User>> getAllDiscussionGroupMemberByGroupId(@PathVariable("groupId") int groupId) {
        return discussionGroupService.getAllDiscussionGroupMembeByGroupIdr(groupId);
    }



    @DeleteMapping(value="/discussiongroups/deleteuser/{groupId}/{userId}")
    public ResponseEntity<String> deleteUserFromDiscussionGroup(@PathVariable("groupId") int groupId,@PathVariable("userId") int userId){
        return discussionGroupService.deleteUserFromDiscussionGroup(groupId,userId);
    }

    @GetMapping(value="/user/discussiongroups/{userId}")
    public ResponseEntity<List<DiscussionGroup>> getDiscussionGroupsByUserId(@PathVariable("userId")int userId){
        return discussionGroupService.getDiscussionGroupsByUserId(userId);
    }

    @GetMapping("/discussiongroups/")
    public ResponseEntity<List<DiscussionGroup>> getAllDiscussionGroups() {
        return discussionGroupService.getAllDiscussionGroups();
    }
    @GetMapping("/visibilityCheck/{groupId}/{userId}")
    public boolean checkVisibilityForMe(@PathVariable("groupId") int groupId,
                                        @PathVariable("userId") int userId){
        return discussionGroupService.checkVisibilityForMe(groupId, userId);
    }

}
