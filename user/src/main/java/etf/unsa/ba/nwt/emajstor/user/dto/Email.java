package etf.unsa.ba.nwt.emajstor.user.dto;

import java.util.Map;

public class Email {

    String to;

    final String from = "slanjeobavijesti@gmail.com";

    String subject;

    String text;

    String template;

    Map<String, Object> properties;

    public Email() {
    }

    public Email(String to, String subject, String text) {
        this.to = to;
        this.subject = subject;
        this.text = text;
    }

    public Email(String to, String subject, String text, String template, Map<String, Object> properties) {
        this.to = to;
        this.subject = subject;
        this.text = text;
        this.template = template;
        this.properties = properties;
    }

    public void setTo(String to) {
        this.to = to;
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