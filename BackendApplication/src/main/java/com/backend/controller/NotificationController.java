package com.backend.controller;

import com.backend.model.Notification;
import com.backend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @PostMapping(value = "/notification/create/")
    public ResponseEntity<String> createNotification(@RequestBody Notification notification) {
        return notificationService.createNotification(notification);
    }

    @GetMapping(value="/notifications/{userId}")
    public ResponseEntity<List<Notification>> getNotificationsByUserId(@PathVariable("userId") int userId) {

        return notificationService.getNotificationsByUserId(userId);
    }

    @DeleteMapping(value = "/notification/delete/{notificationId}")
    public ResponseEntity<String> deleteNotification(@PathVariable("notificationId") int notificationId){
        return notificationService.deleteNotification(notificationId);
    }
}
