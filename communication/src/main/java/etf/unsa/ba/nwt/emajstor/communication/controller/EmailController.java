package etf.unsa.ba.nwt.emajstor.communication.controller;

import etf.unsa.ba.nwt.emajstor.communication.dto.RegisterDTO;
import etf.unsa.ba.nwt.emajstor.communication.model.Email;
import etf.unsa.ba.nwt.emajstor.communication.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/email")
public class EmailController  {

    private final EmailService emailSenderService;

    public EmailController(final EmailService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @PostMapping("/send/html")
    public void sendHtmlMessage(@RequestBody Email email) throws MessagingException {
        emailSenderService.sendHtmlMessage(email);
    }

    @PostMapping("/send/register")
    public void sendHtmlMessage(@RequestBody RegisterDTO registerDTO) throws MessagingException {
        emailSenderService.sendRegisterEmail(registerDTO);
    }

    @PostMapping("/send/simple")
    public void sendSimpleMessage(@RequestBody Email email) {
        emailSenderService.sendSimpleMessage(email);
    }
}
