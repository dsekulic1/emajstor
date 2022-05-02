package etf.unsa.ba.nwt.emajstor.communication.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Entity
@Table(name="chat")
public class ChatMessage {
    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(nullable = false)
    @NotBlank
    @Size(min = 2, max = 255)
    private String text;

    @Column(nullable = false)
    private String senderUsername;

    @Column(nullable = false)
    private int chatId;

    public ChatMessage() {
    }

    public ChatMessage(String text, String senderUsername, int chatId) {
        this.text = text;
        this.senderUsername = senderUsername;
        this.chatId = chatId;
    }

    public ChatMessage(UUID id, String text, String senderUsername, int chatId) {
        this.id = id;
        this.text = text;
        this.senderUsername = senderUsername;
        this.chatId = chatId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }
}
