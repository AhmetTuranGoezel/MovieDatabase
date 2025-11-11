package com.backend.service;

import com.backend.model.WatchpartyInvitation;
import com.backend.repository.MovieRepository;
import com.backend.repository.UserRepository;
import com.backend.repository.WatchpartyInvitationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class WatchpartyInvitationService {

    @Autowired
    WatchpartyInvitationRepository watchpartyInvitationRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EmailService emailService;

    @Autowired
    MovieRepository movieRepository;



    public ResponseEntity<String> createWatchpartyInvitation(WatchpartyInvitation watchpartyInvitation, int recipientId) {
        watchpartyInvitationRepository.save(watchpartyInvitation);
        emailService.sendMessage(userRepository.findById(recipientId).get().getEmail(),"Invitation: Watchparty",
                "Your friend: "+watchpartyInvitation.getSender().getUsername()+" sent you a watch party invitation for the movie:\n\n"+watchpartyInvitation.getMovie().getTitle()+
                        "\n\n"+ "with the optional text: "+watchpartyInvitation.getText()+"\n\n\n"+
                            "Note: The request can be accepted or rejected by clicking on the notification in the community view or in the home view.");
        return new ResponseEntity<>("Watchparty invitation created", HttpStatus.OK);
    }


    public ResponseEntity<String> deleteWatchpartyInvitation(int watchpartyInvitationId) {

        watchpartyInvitationRepository.deleteById(watchpartyInvitationId);
        return new ResponseEntity<>("You don't accepted the invitation!",HttpStatus.OK);
    }

    public ResponseEntity <List<WatchpartyInvitation>> getWatchpartyInvitationsByUserId(int userId) {

        List<WatchpartyInvitation> watchpartyInvitations = new LinkedList<>();

        for (WatchpartyInvitation watchpartyInvitation: watchpartyInvitationRepository.findAll()) {
           if (watchpartyInvitation.getRecipient().getAccountID()==userId) {
               watchpartyInvitations.add(watchpartyInvitation);
           }
        }
        return new ResponseEntity<>(watchpartyInvitations,HttpStatus.OK);
    }

    public ResponseEntity<WatchpartyInvitation> getWatchpartyInvitationById(int watchpartyInvId) {
        return new ResponseEntity<>(watchpartyInvitationRepository.findById(watchpartyInvId).get(),HttpStatus.OK);
    }
}
