package etf.unsa.ba.nwt.emajstor.review.dto;

import java.util.UUID;

public class Message {
    private UUID id;
    private String text;
    private UUID sender;
    private UUID receiver;

    public Message() {
    }

    public Message(String text, UUID sender, UUID receiver) {
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
