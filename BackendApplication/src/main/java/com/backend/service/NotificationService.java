package com.backend.service;

import com.backend.model.Notification;
import com.backend.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class NotificationService {
    
    @Autowired
    NotificationRepository notificationRepository;

    public ResponseEntity<String> createNotification(Notification notification) {
        notificationRepository.save(notification);
        return new ResponseEntity<>("Notification created!", HttpStatus.OK);
    }

    public ResponseEntity<List<Notification>> getNotificationsByUserId(int userId) {
        List<Notification> notifications = new LinkedList<>();
        for (Notification notification: notificationRepository.findAll()) {
            if(notification.getNotificatedUser().getAccountID()==userId){
                notifications.add(notification);
            }
        }
        return new ResponseEntity<>(notifications,HttpStatus.OK);
    }

    public ResponseEntity<String> deleteNotification(int notificationId) {
        notificationRepository.deleteById(notificationId);
        return new ResponseEntity<>("Notification deleted!",HttpStatus.OK);
    }
}
