package com.backend.test;

import com.backend.model.DiscussionGroup;
import com.backend.model.User;
import com.backend.repository.DiscussionGroupRepository;
import com.backend.repository.UserRepository;
import com.backend.service.DiscussionGroupService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DiscussionGroupTest {

    @InjectMocks
    DiscussionGroupService discussionGroupService = new DiscussionGroupService();

    @Mock
    DiscussionGroupRepository discussionGroupRepository;

    @Mock
    UserRepository userRepository;

    List<DiscussionGroup> discussionGroupList = new LinkedList<>();
     //Builder
    public DiscussionGroup discussionGroupBuilder(int groupId, String groupName,Set<User> groupMember,int visible){
        DiscussionGroup discussionGroup = new DiscussionGroup();
        discussionGroup.setGroupId(groupId);
        discussionGroup.setGroupName(groupName);
        discussionGroup.setGroupMember(groupMember);
        discussionGroup.setVisible(visible);
        return discussionGroup;
    }
    public User userBuilder(int accountId, String forename,String surname,String email ,String username){
        User user = new User();
        user.setAccountID(accountId);
        user.setForename(forename);
        user.setSurname(surname);
        user.setEmail(email);
        user.setUsername(username);
        return user;
    }

    public User userBuilderWithFriend(int accountId, String forename,String surname,String email ,String username,Set<User> friends){
        User user = new User();
        user.setAccountID(accountId);
        user.setForename(forename);
        user.setSurname(surname);
        user.setEmail(email);
        user.setUsername(username);
        user.setFriends(friends);
        return user;
    }
    public Set<User> groupMemberBuilder() {
        Set<User> groupMember = new HashSet<>();
       groupMember.add(userBuilder(1,"Emre","Kubat","emre.kubat@stud.uni-due.de","mQ"));
       groupMember.add(userBuilder(2,"Furkan","Bedirhanoglu","furkan.bedirhanoglu@stud.uni-due.de","vFurkii"));
       groupMember.add(userBuilder(3,"Dilan","Özyol","dilan.oezyol@stud.uni-due.de","Dilo62"));
       groupMember.add(userBuilder(4,"Robert","Heß","robert.hess@stud.uni-due.de","Pun1sher"));
       groupMember.add(userBuilder(5,"Ahmet","Gözel","ahmet.goezel@stud.uni-due.de","Owl"));
       return groupMember;
    }

    public Set<User> friendListBuilder() {
        Set<User> friendList = new HashSet<>();
        friendList.add(userBuilder(2,"Furkan","Bedirhanoglu","furkan.bedirhanoglu@stud.uni-due.de","vFurkii"));
        friendList.add(userBuilder(3,"Dilan","Özyol","dilan.oezyol@stud.uni-due.de","Dilo62"));
        friendList.add(userBuilder(4,"Robert","Heß","robert.hess@stud.uni-due.de","Pun1sher"));
        friendList.add(userBuilder(5,"Ahmet","Gözel","ahmet.goezel@stud.uni-due.de","Owl"));
        return friendList;
    }

    public Set<User> emptyGroupBuilder(){
        Set <User> emptyGroup = new HashSet<>();
        return emptyGroup;
    }

@Test
//Test, falls der Gruppenname vergeben ist.
   void createDiscussionGroup(){
         when(discussionGroupRepository.findAll()).thenReturn(List.of(new DiscussionGroup[]
                       {discussionGroupBuilder(1,"Gruppe P",groupMemberBuilder(),0),
                               discussionGroupBuilder(2,"Gruppe A",emptyGroupBuilder(),1)}));

         ResponseEntity<String> response = discussionGroupService.createDiscussionGroup(discussionGroupBuilder(3,"Gruppe P",emptyGroupBuilder(),1));

               assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
               assertEquals(response.getBody(),"Groupname already exist. ");
    }

    @Test
//Test, falls der Gruppenname nicht vergeben ist.
    void createNewDiscussionGroup(){
        when(discussionGroupRepository.findAll()).thenReturn(List.of(new DiscussionGroup[]
                {discussionGroupBuilder(1,"Gruppe P",groupMemberBuilder(),0),
                        discussionGroupBuilder(2,"Gruppe A",emptyGroupBuilder(),1)}));

        ResponseEntity<String> response = discussionGroupService.createDiscussionGroup(discussionGroupBuilder(3,"Gruppe O",emptyGroupBuilder(),1));

        assertEquals(response.getStatusCode(),HttpStatus.OK);
        assertEquals(response.getBody(),"Discussion group created");
    }

    @Test
    void getDiscussionGroupById() {
        int id = 1;

        when(discussionGroupRepository.findById(id)).thenReturn(Optional.ofNullable(discussionGroupBuilder(1,"Gruppe P",groupMemberBuilder(),1)));
    ResponseEntity<DiscussionGroup> discussiongroupById= discussionGroupService.getDiscussionGroupById(1);

       assertEquals(discussiongroupById.getBody().getGroupId(),1);
       assertEquals(discussiongroupById.getBody().getGroupMember().size(),groupMemberBuilder().size());
       assertEquals(discussiongroupById.getBody().getGroupName(),"Gruppe P");
       assertEquals(discussiongroupById.getStatusCode(),HttpStatus.OK);
    }

    @Test
    void deleteUserFromDiscussionGroup() {
     int userId =1;
     int groupId=1;

     when(discussionGroupRepository.findById(groupId)).thenReturn(Optional.ofNullable(discussionGroupBuilder(1,"Gruppe P",groupMemberBuilder(),1)));
     when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(userBuilder(1,"Emre","Kubat","emre.kubat@stud.uni-due.de","mQ")));
     ResponseEntity<String> response = discussionGroupService.deleteUserFromDiscussionGroup(groupId,userId);

     assertEquals(response.getStatusCode(),HttpStatus.OK);
     assertEquals(response.getBody(),"You have been removed from the discussion group");

    }

    @Test
    void checkVisibilityForMe() {
        int groupId=1;
        int userId=1;
        Set<User> member = friendListBuilder();
        User user = userBuilderWithFriend(userId,"Emre","Kubat","emre.kubat@stud.uni-due.de","mQ", member);
        DiscussionGroup group = discussionGroupBuilder(groupId, "Gruppe P", member, 1);

        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user));
        when(discussionGroupRepository.findById(groupId)).thenReturn(Optional.ofNullable(group));
        Boolean result = discussionGroupService.checkVisibilityForMe(groupId,userId);

        assertEquals(result,Boolean.TRUE);
    }


    @Test
    void getAllDiscussionGroupMemberByGroupId() {
       int groupId =1;

       DiscussionGroup discussionGroup = discussionGroupBuilder(1,"Gruppe P",groupMemberBuilder(),1);
       when(discussionGroupRepository.findById(1)).thenReturn(Optional.ofNullable(discussionGroup));

       ResponseEntity<Set<User>> groupMember=discussionGroupService.getAllDiscussionGroupMembeByGroupIdr(groupId);

       assertEquals(groupMember.getBody().size(),5);
       assertEquals(groupMember.getStatusCode(),HttpStatus.OK);
       assertEquals(groupMember.getBody().isEmpty(),Boolean.FALSE);
    }

    @Test
    void getAllDiscussionGroups(){
        discussionGroupList.add(discussionGroupBuilder(1,"Gruppe P",groupMemberBuilder(),1));
        discussionGroupList.add(discussionGroupBuilder(2,"Gruppe X",groupMemberBuilder(),1));
        discussionGroupList.add(discussionGroupBuilder(3,"Gruppe Z",groupMemberBuilder(),1));

        when(discussionGroupRepository.findAll()).thenReturn(discussionGroupList);

        ResponseEntity<List<DiscussionGroup>> discussionGroups = discussionGroupService.getAllDiscussionGroups();

        assertEquals(discussionGroups.getStatusCode(),HttpStatus.OK);
        assertEquals(discussionGroups.getBody().get(0).getGroupName(),"Gruppe P");
        assertEquals(discussionGroups.getBody().get(2).getGroupId(),3);
    }

    @Test
    void getDiscussionGroupsByUserId(){
       User user =  userBuilder(1,"Emre","Kubat","emre.kubat@stud.uni-due.de","mQ");

        discussionGroupList.add(discussionGroupBuilder(1,"Gruppe P",groupMemberBuilder(),1));
        discussionGroupList.add(discussionGroupBuilder(2,"Gruppe X",groupMemberBuilder(),1));
        discussionGroupList.add(discussionGroupBuilder(3,"Gruppe Z",groupMemberBuilder(),1));

        when(discussionGroupRepository.findAll()).thenReturn(discussionGroupList);

        ResponseEntity<List<DiscussionGroup>> discussionGroupsByUser = discussionGroupService.getDiscussionGroupsByUserId(user.getAccountID());

        assertEquals(discussionGroupsByUser.getBody().size(),3);
    }

    //Fall, dass der User bereits in der Gruppe ist.
    @Test
    void addUserToDiscussionGroupWhichIsMemberAlready(){
        User user =  userBuilder(1,"Emre","Kubat","emre.kubat@stud.uni-due.de","mQ");
        DiscussionGroup discussionGroup = discussionGroupBuilder(1,"Gruppe P",groupMemberBuilder(),1);
        when(discussionGroupRepository.findById(discussionGroup.getGroupId())).thenReturn(Optional.of(discussionGroup));

        ResponseEntity<String> response = discussionGroupService.addUserToDiscussionGroup(discussionGroup.getGroupId(),user.getAccountID());
        assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody(),"User is already in the group!");

    }

    @Test
    void addUserToDiscussionGroup(){
        User user =  userBuilder(6,"Itachi","Uchia","itachiuchia@outlook.de","Mr.Amaterasu");
        DiscussionGroup discussionGroup = discussionGroupBuilder(1,"Gruppe P",groupMemberBuilder(),1);
        when(discussionGroupRepository.findById(discussionGroup.getGroupId())).thenReturn(Optional.of(discussionGroup));
        when(userRepository.findById(user.getAccountID())).thenReturn(Optional.of(user));

        ResponseEntity<String> response = discussionGroupService.addUserToDiscussionGroup(discussionGroup.getGroupId(),user.getAccountID());
        assertEquals(response.getStatusCode(),HttpStatus.OK);
        assertEquals(response.getBody(),"User added to the group!");

    }

    //Fall, wenn Gruppenname bereits existiert
    @Test
    void editDiscussionGroupWithExistingName(){
        when(discussionGroupRepository.findAll()).thenReturn(List.of(new DiscussionGroup[]
                {discussionGroupBuilder(1,"Gruppe P",groupMemberBuilder(),0),
                        discussionGroupBuilder(2,"Gruppe A",emptyGroupBuilder(),1)}));

        ResponseEntity<String> response = discussionGroupService.editDiscussionGroup(discussionGroupBuilder(2,"Gruppe P",groupMemberBuilder(),0));

        assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody(),"Group name already exist. ");
    }

    //Fall, wenn Grupenname noch nicht existiert
    @Test
    void editDiscussionGroup(){
        when(discussionGroupRepository.findAll()).thenReturn(List.of(new DiscussionGroup[]
                {discussionGroupBuilder(1,"Gruppe P",groupMemberBuilder(),0),
                        discussionGroupBuilder(2,"Gruppe A",emptyGroupBuilder(),1)}));

        ResponseEntity<String> response = discussionGroupService.editDiscussionGroup(discussionGroupBuilder(2,"Gruppe Z",groupMemberBuilder(),0));

        assertEquals(response.getStatusCode(),HttpStatus.OK);
        assertEquals(response.getBody(),"Discussion group updated!");
    }

    @Test
    void deleteDiscussionGroup() {
        DiscussionGroup discussionGroup = discussionGroupBuilder(1,"Gruppe P",groupMemberBuilder(),1);
        ResponseEntity<String> response = discussionGroupService.deleteDiscussionGroup(discussionGroup.getGroupId());

        assertEquals(response.getBody(),"Discussion group deleted!");
        assertEquals(response.getStatusCode(),HttpStatus.OK);
    }





}
