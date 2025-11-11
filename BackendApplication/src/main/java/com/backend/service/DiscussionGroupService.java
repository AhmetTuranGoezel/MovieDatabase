package com.backend.service;

import com.backend.model.DiscussionGroup;
import com.backend.model.User;
import com.backend.repository.DiscussionGroupRepository;
import com.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class DiscussionGroupService {

    @Autowired
    DiscussionGroupRepository discussionGroupRepository;

    @Autowired
    UserRepository userRepository;
    public ResponseEntity<String> createDiscussionGroup(DiscussionGroup discussionGroup) {

        List <DiscussionGroup> discussionGroupList = discussionGroupRepository.findAll();

        for(DiscussionGroup discussionGroups : discussionGroupList){

            if(discussionGroup.getGroupName().equals(discussionGroups.getGroupName())){
                return new ResponseEntity<>("Groupname already exist. ", HttpStatus.BAD_REQUEST);
            }
        }
        discussionGroupRepository.save(discussionGroup);
        return new ResponseEntity<>("Discussion group created", HttpStatus.OK);

    }


    public ResponseEntity<DiscussionGroup> getDiscussionGroupById(int id) {

       return new ResponseEntity<>(discussionGroupRepository.findById(id).get(),HttpStatus.OK);
    }

    public ResponseEntity<String> addUserToDiscussionGroup(int groupId, int userId) {

      Set<User> usersInGroup = discussionGroupRepository.findById(groupId).get().getGroupMember();

      for (User user: usersInGroup) {

          if (user.getAccountID()==userId) {

              return new ResponseEntity<>("User is already in the group!",HttpStatus.BAD_REQUEST);
          }

      }

      usersInGroup.add(userRepository.findById(userId).get());
      discussionGroupRepository.save(discussionGroupRepository.findById(groupId).get());

      return new ResponseEntity<>("User added to the group!",HttpStatus.OK);

    }

    public ResponseEntity<String> editDiscussionGroup(DiscussionGroup discussionGroup) {

        List <DiscussionGroup> discussionGroupList = discussionGroupRepository.findAll();

        for(DiscussionGroup discussionGroups : discussionGroupList){

            if(discussionGroup.getGroupId()!=discussionGroups.getGroupId() && discussionGroup.getGroupName().equals(discussionGroups.getGroupName())){
                    return new ResponseEntity<>("Group name already exist. ", HttpStatus.BAD_REQUEST);
                }
            }
        discussionGroupRepository.save(discussionGroup);
        return new ResponseEntity<>("Discussion group updated!", HttpStatus.OK);
    }

    public ResponseEntity<String> deleteDiscussionGroup(int groupId) {
        discussionGroupRepository.deleteById(groupId);
        return new ResponseEntity<>("Discussion group deleted!",HttpStatus.OK);
    }

    public ResponseEntity<String> deleteUserFromDiscussionGroup(int groupId, int userId) {

        discussionGroupRepository.findById(groupId).get().getGroupMember().remove(userRepository.findById(userId).get());
        discussionGroupRepository.save(discussionGroupRepository.findById(groupId).get());

        if(discussionGroupRepository.findById(groupId).get().getGroupMember().isEmpty()){
            discussionGroupRepository.deleteById(groupId);
        }

        return new ResponseEntity<>("You have been removed from the discussion group",HttpStatus.OK);
    }

    public ResponseEntity<List<DiscussionGroup>> getDiscussionGroupsByUserId(int userId) {
        List<DiscussionGroup> discussionGroupsList = new LinkedList<>();
        for (DiscussionGroup discussionGroup:discussionGroupRepository.findAll()) {
            for (User user: discussionGroup.getGroupMember()) {
                if (user.getAccountID()==userId) {
                    discussionGroupsList.add(discussionGroup);
                }
            }
        }
        return new ResponseEntity<>(discussionGroupsList,HttpStatus.OK);
    }

    public ResponseEntity<List<DiscussionGroup>> getAllDiscussionGroups() {

        return new ResponseEntity<>(discussionGroupRepository.findAll(),HttpStatus.OK);
    }


    public boolean checkVisibilityForMe(int groupId, int userId){

        User me = userRepository.findById(userId).get();
        Set<User> groupMember = discussionGroupRepository.findById(groupId).get().getGroupMember();
        Set<User> myFriends = me.getFriends();

        if(groupMember.contains(me)){
            return true;
        }else{
            for(User user:myFriends){
                if(groupMember.contains(user)) {
                    return true;
                }
            }
        }
        return false;
    }


    public ResponseEntity<Set<User>> getAllDiscussionGroupMembeByGroupIdr(int groupId) {
        return new ResponseEntity<>(discussionGroupRepository.findById(groupId).get().getGroupMember(),HttpStatus.OK);
    }

}
