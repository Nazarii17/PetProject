package com.tkachuk.pet.service.impl;


import com.tkachuk.pet.entity.User;
import com.tkachuk.pet.service.MailSender;
import com.tkachuk.pet.util.constants.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class MailSenderImpl implements MailSender {

    @Value("${hostname}")
    private String hostname;
    @Value("${spring.mail.username}")
    private String username;

    private final JavaMailSender mailSender;

    @Autowired
    public MailSenderImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(String emailTo, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(username);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }

    public void sendNotification(User userFromDb, Notification notification) {
        send(userFromDb.getEmail(), notification.name(), notification.getValue());
    }

    @Override
    public void sendNotification(User userFromDb, List<Notification> notifications) {
        if (!notifications.isEmpty()) {
            if (notifications.size() > 1) {
                String message = createMessage(notifications);
                send(userFromDb.getEmail(), Notification.PROFILE_UPDATED.name(), message);
            } else {
                send(userFromDb.getEmail(), notifications.get(0).name(), notifications.get(0).getValue());
            }
        }
    }

    private String createMessage(List<Notification> notifications) {
        String message = buildMessage(notifications);
        return fixGrammar(notifications, message);
    }

    private String buildMessage(List<Notification> notifications) {
        StringBuilder message = new StringBuilder(Notification.MESSAGE_BEGINNING.getValue());
        String messageEnding = getMessageEnding(notifications.get(0));
        for (int i = 0; i < notifications.size(); i++) {
            Notification notification = notifications.get(i);
            if (i != notifications.size() - 1) {
                message.append(cleanUpNotification(notification.getValue(),messageEnding)).append(", ");
            } else {
                message.append(cleanUpNotification(notification.getValue(),messageEnding));
            }
        }
        message.append(messageEnding);
        return message.toString();
    }

    private String getMessageEnding(Notification notification) {
        String mEnding = notification.getValue().replace(Notification.MESSAGE_BEGINNING.getValue(), "");
        String word = mEnding.split(" ")[0];
        mEnding = mEnding.replace(word,"");
        return mEnding;
    }

    private String fixGrammar(List<Notification> notifications, String message) {
        if (notifications.size() > 1) {
            message = message.replace("was", "were");
        }
        return message;
    }

    private String cleanUpNotification(String value, String messageEnding) {
        value = value.replace(Notification.MESSAGE_BEGINNING.getValue(), "");
        value = value.replace(messageEnding, "");
        return value;
    }

    public void sendActivationMessage(User user) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome on board. Please, visit next link: http://%s/activate/%s",
                    user.getUsername(),
                    hostname,
                    user.getActivationCode()
            );
            send(user.getEmail(), Notification.ACTIVATION_CODE.getValue(), message);
        }
    }
}
