package com.tkachuk.pet.service;


import com.tkachuk.pet.entity.User;
import com.tkachuk.pet.util.Notifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class MailSender {

    @Value("${hostname}")
    private String hostname;
    @Value("${spring.mail.username}")
    private String username;

    private final JavaMailSender mailSender;
    private final UserService userService;

    @Autowired
    public MailSender(JavaMailSender mailSender, UserService userService) {
        this.mailSender = mailSender;
        this.userService = userService;
    }

    public void send(String emailTo, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(username);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }

    public void sendNotification(User userFromDb, Notifications notifications) {
        send(userFromDb.getEmail(), notifications.name(), notifications.getValue());
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
            send(user.getEmail(), "Activation code", message);
        }
    }

    public void sendToAll(@RequestParam("message") String message, @RequestParam("subject") String subject) {
        for (User user : userService.findAll()) {
            send(user.getEmail(), subject, message);
        }
    }
}
