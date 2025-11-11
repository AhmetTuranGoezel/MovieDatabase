package com.backend.service;


import com.backend.model.Watchparty;
import com.backend.repository.UserRepository;
import com.backend.repository.WatchpartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class WatchpartyService {

    @Autowired
    WatchpartyRepository watchpartyRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;


    public ResponseEntity<String> createWatchparty(Watchparty watchparty) {
     watchpartyRepository.save(watchparty);
     emailService.sendMessage(watchparty.getHost().getEmail(),"Watchparty invitation accepted",
                              "Hello "+watchparty.getHost().getUsername()+",\n\n"+" your friend "+watchparty.getGuest().getUsername()+
                               " accepted your watchparty invitation to the movie: \n\n"+watchparty.getMovie().getTitle()+"\n\n"+ "on: "+
                                watchparty.getDate()+ " at: "+watchparty.getTime());

     return new ResponseEntity<>("Watchparty created!", HttpStatus.OK);
    }


    public ResponseEntity<List<Watchparty>> getWatchpartysByUserId(int userId) {

        List<Watchparty> watchpartyListByUser = new LinkedList<>();

        for (Watchparty watchparty: watchpartyRepository.findAll()) {

            if (watchparty.getHost().getAccountID()==userId || watchparty.getGuest().getAccountID() == userId) {
                watchpartyListByUser.add(watchparty);
            }

        }
        return new ResponseEntity<>(watchpartyListByUser,HttpStatus.OK);
    }
}
