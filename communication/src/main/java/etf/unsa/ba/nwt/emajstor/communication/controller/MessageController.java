package etf.unsa.ba.nwt.emajstor.communication.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import etf.unsa.ba.nwt.emajstor.communication.exception.NotFoundException;
import etf.unsa.ba.nwt.emajstor.communication.model.Message;
import etf.unsa.ba.nwt.emajstor.communication.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<Message> addMessage(@RequestBody @Valid Message message) {
        return ResponseEntity.ok(messageService.addMessage(message));
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable UUID id) {
        return ResponseEntity.ok(messageService.getMessageById(id));
    }

    @GetMapping(path = "/receiver/{id}")
    public ResponseEntity<List<Message>> findAllByReceiver(@PathVariable UUID id) {
        return ResponseEntity.ok(messageService.findAllByReceiver(id));
    }

    @GetMapping(path = "/sender/{id}")
    public ResponseEntity<List<Message>> findAllBySender(@PathVariable UUID id) {
        return ResponseEntity.ok(messageService.findAllBySender(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Message> updateMessageById(@PathVariable UUID id, @RequestBody @Valid Message message ) {
        return ResponseEntity.ok(messageService.updateMessageById(message, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Message> deleteMessageById(@PathVariable UUID id) {
        return ResponseEntity.ok(messageService.deleteMessageById(id));
    }

    private Message applyPatchToMessage(
            JsonPatch patch, Message targetMessage) throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode patched = patch.apply(objectMapper.convertValue(targetMessage, JsonNode.class));
        return objectMapper.treeToValue(patched, Message.class);
    }

    @PatchMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<Message> updateReview(@PathVariable String id, @RequestBody JsonPatch patch) {
        try {
            Message message = messageService.getMessageById(UUID.fromString(id));
            Message messagePatched = applyPatchToMessage(patch, message);
            messageService.updateMessageById(messagePatched, messagePatched.getId());
            return ResponseEntity.ok(messagePatched);
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
