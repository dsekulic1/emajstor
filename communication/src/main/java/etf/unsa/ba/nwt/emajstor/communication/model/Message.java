package etf.unsa.ba.nwt.emajstor.communication.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Entity
@Table(name="message")
public class Message {
    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(nullable = false)
    @NotBlank
    @Size(min = 2, max = 255)
    private String text;

    @Column(name="sender_id", nullable = false)
    @Type(type = "uuid-char")
    private UUID sender;

    @Column(name="receiver_id", nullable = false)
    @Type(type = "uuid-char")
    private UUID receiver;

    @OneToOne(mappedBy = "message")
    private NotificationHistory notificationHistory;

    public Message() {
    }

    public Message(String text, UUID sender, UUID receiver) {
        this.text = text;
        this.sender = sender;
        this.receiver = receiver;
    }

    public Message(UUID id, String text, UUID sender, UUID receiver) {
        this.id = id;
        this.text = text;
        this.sender = sender;
        this.receiver = receiver;
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

    public UUID getSender() {
        return sender;
    }

    public void setSender(UUID sender) {
        this.sender = sender;
    }

    public UUID getReceiver() {
        return receiver;
    }

    public void setReceiver(UUID receiver) {
        this.receiver = receiver;
    }
}
