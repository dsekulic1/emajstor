package etf.unsa.ba.nwt.emajstor.communication.controller;

import etf.unsa.ba.nwt.emajstor.communication.model.Email;
import etf.unsa.ba.nwt.emajstor.communication.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
public class EmailController  {

    private final EmailService emailSenderService;

    @PostMapping("/send/html")
    public void sendHtmlMessage(@RequestBody Email email) throws MessagingException {
        emailSenderService.sendHtmlMessage(email);
    }

    @PostMapping("/send/simple")
    public void sendSimpleMessage(@RequestBody Email email) {
        emailSenderService.sendSimpleMessage(email);
    }
}
