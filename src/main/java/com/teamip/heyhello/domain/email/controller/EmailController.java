package com.teamip.heyhello.domain.email.controller;

import com.teamip.heyhello.domain.email.dto.EmailRequestDto;
import com.teamip.heyhello.domain.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;
    @PostMapping
    public String sendEmail(@RequestBody EmailRequestDto emailRequestDto) {
        return emailService.sendEmailToUser(emailRequestDto);
    }
}
