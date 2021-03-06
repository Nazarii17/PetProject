package com.tkachuk.pet.controller;


import com.tkachuk.pet.service.MailSender;
import com.tkachuk.pet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/email")
public class MailController {

    private final MailSender mailSender;
    private final UserService userService;

    @Autowired
    public MailController(MailSender mailSender, UserService userService) {
        this.mailSender = mailSender;
        this.userService = userService;
    }

    @GetMapping("/to-all")
    public String getPage(Map<String, Object> model) {
        return "emailToAll";
    }

    @PostMapping("/to-all")
    public String sendToAll(@RequestParam(value = "message") String message,
                            @RequestParam(value = "subject") String subject) {

        userService.sendToAll(message, subject);
        return "redirect:/email/to-all";
    }

    @PostMapping("/to-one")
    public String sendToOne(@RequestParam(value = "email") String email,
                            @RequestParam(value = "message") String message,
                            @RequestParam(value = "subject") String subject) {

        mailSender.send(email, subject, message);

        return "redirect:/email/to-all";
    }

//    @PostMapping("/to-one")
//    public ResponseEntity<String> sendToOne(@RequestParam(value = "email") String email,
//                                    @RequestParam(value = "message") String message,
//                                    @RequestParam(value = "subject") String subject) {
//
//        mailSender.send(email, subject, message);
//
//        return ResponseEntity.ok(message);
//    }
}
