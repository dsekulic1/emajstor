package etf.unsa.ba.nwt.emajstor.communication.controller;

import etf.unsa.ba.nwt.emajstor.communication.model.ChatMessage;

import etf.unsa.ba.nwt.emajstor.communication.service.ChatMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/chat/message")
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    public ChatMessageController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @PostMapping
    public ResponseEntity<ChatMessage> addMessage(@RequestBody @Valid ChatMessage chatMessage) {
        return ResponseEntity.ok(chatMessageService.addChatMessage(chatMessage));
    }
}
