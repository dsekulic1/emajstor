package etf.unsa.ba.nwt.emajstor.communication.model;

import lombok.Data;


import java.util.Map;

@Data
public class Email {
    String to;

    String from;

    String subject;

    String text;

    String template;

    Map<String, Object> properties;

    public Email() {
    }

    public Email(String to, String from, String subject, String text) {
        this.to = to;
        this.from = from;
        this.subject = subject;
        this.text = text;
    }

    public Email(String to, String from, String subject, String text, String template, Map<String, Object> properties) {
        this.to = to;
        this.from = from;
        this.subject = subject;
        this.text = text;
        this.template = template;
        this.properties = properties;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }

    public String getTemplate() {
        return template;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }
}