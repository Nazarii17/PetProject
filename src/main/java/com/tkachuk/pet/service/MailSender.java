package com.tkachuk.pet.service;

import com.tkachuk.pet.entity.User;
import com.tkachuk.pet.util.constants.Notifications;

public interface MailSender {

    void send(String emailTo, String subject, String message);

    void sendNotification(User userFromDb, Notifications notifications);

    void sendActivationMessage(User user);
}
