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
    private String senderLocation;

    @Column(nullable = false)
    private String receiverUsername;

    public ChatMessage() {
    }

    public ChatMessage(String text, String senderUsername, String senderLocation, String receiverUsername) {
        this.text = text;
        this.senderUsername = senderUsername;
        this.senderLocation = senderLocation;
        this.receiverUsername = receiverUsername;
    }

    public ChatMessage(UUID id, String text, String senderUsername, String senderLocation, String receiverUsername) {
        this.id = id;
        this.text = text;
        this.senderUsername = senderUsername;
        this.senderLocation = senderLocation;
        this.receiverUsername = receiverUsername;
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

    public String getSenderLocation() {
        return senderLocation;
    }

    public void setSenderLocation(String senderLocation) {
        this.senderLocation = senderLocation;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }
}
