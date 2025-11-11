package com.backend.controller;

import com.backend.model.Watchparty;
import com.backend.service.WatchpartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WatchpartyController {

    @Autowired
    WatchpartyService watchpartyService;

    @PostMapping(value = "/watchparty/create/")
    public ResponseEntity<String> createWatchparty(@RequestBody Watchparty watchparty) {
        return watchpartyService.createWatchparty(watchparty);
    }

    @GetMapping(value = "/watchpartys/{userId}")
    public ResponseEntity<List<Watchparty>> getWatchpartysByUserId(@PathVariable("userId") int userId) {
        return watchpartyService.getWatchpartysByUserId(userId);
    }








}
