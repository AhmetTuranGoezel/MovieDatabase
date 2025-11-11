package com.backend.controller;

import com.backend.model.WatchpartyInvitation;
import com.backend.service.WatchpartyInvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WatchpartyInvitationController {

    @Autowired
    WatchpartyInvitationService watchpartyInvitationService;

    @PostMapping(value="/watchpartyinvitation/create/{recipientId}")
    public ResponseEntity<String> createWatchpartyInvitation(@RequestBody WatchpartyInvitation watchpartyInvitation, @PathVariable ("recipientId") int recipientId) {
        return watchpartyInvitationService.createWatchpartyInvitation(watchpartyInvitation,recipientId);
    }

    @DeleteMapping(value = "/watchpartyinvitation/delete/{watchpartyInvitationId}")
    public ResponseEntity<String> deleteWatchpartyInvitation(@PathVariable ("watchpartyInvitationId") int watchpartyInvitationId){
        return watchpartyInvitationService.deleteWatchpartyInvitation(watchpartyInvitationId);
    }

    @GetMapping(value="/watchpartyinvitations/{userId}")
    public ResponseEntity <List<WatchpartyInvitation>> getWatchpartyInvitationsByUserId(@PathVariable("userId") int userId) {

        return watchpartyInvitationService.getWatchpartyInvitationsByUserId(userId);
    }

    @GetMapping(value="/watchpartyinvitation/{watchpartyInvId}")
    public ResponseEntity <WatchpartyInvitation> getWatchpartyInvitationById(@PathVariable("watchpartyInvId") int watchpartyInvId){
        return watchpartyInvitationService.getWatchpartyInvitationById(watchpartyInvId);
    }





}
