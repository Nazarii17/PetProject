package com.tkachuk.pet.service;

import com.tkachuk.pet.entity.User;
import com.tkachuk.pet.constants.Notification;

import java.util.List;

public interface MailSender {

    void send(String emailTo, String subject, String message);

    void sendNotification(User userFromDb, Notification notification);

    void sendNotification(User userFromDb, List<Notification> notifications);

    void sendActivationMessage(User user);
}
