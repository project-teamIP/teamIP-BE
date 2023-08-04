package com.teamip.heyhello.domain.email.service;

import com.teamip.heyhello.domain.email.dto.EmailRequestDto;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {
    @Value("${spring.mail.username}")
    private String sender;
    private final JavaMailSender javaMailSender;

    private String code;
    private final int AUTH_CODE_LEN = 8;


    public String sendEmailToUser(EmailRequestDto emailRequestDto) {
        createRandomCode();
        MimeMessage message = createMessage(emailRequestDto);
        javaMailSender.send(message);

        return code;
    }

    private MimeMessage createMessage(EmailRequestDto emailRequestDto) {
        MimeMessage message = javaMailSender.createMimeMessage();
        String content = getHtmlContent();
        try {
            message.addRecipients(Message.RecipientType.TO, emailRequestDto.getAddress());
            message.setSubject("\"헤이, 안녕?\" 서비스 인증용 이메일");
            message.setText(content, "utf-8", "html");
            message.setFrom(sender);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }


        return message;
    }


    private String getHtmlContent() {
        StringBuilder content = new StringBuilder();
        content.append("<div style='margin:20px;'>");
        content.append("<h1> 안녕하세요 헤이, 안녕! 입니다.</h1>");
        content.append("<br>");
        content.append("<p>아래 코드를 복사해 입력해주세요.<p>");
        content.append("<br>");
        content.append("<p>감사합니다.<p>");
        content.append("<br>");
        content.append("<div align='center' style='border:1px solid black; font-family:verdana';>");
        content.append("<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>");
        content.append("<div style='font-size:130%'>");
        content.append("CODE : <strong>");
        content.append(code);
        content.append("</strong><div><br/> ");
        content.append("</div>");

        return content.toString();
    }

    private void createRandomCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        StringBuilder sb = new StringBuilder(AUTH_CODE_LEN);

        for(int i=0;i<AUTH_CODE_LEN;i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }

        code = sb.toString();
    }
}
