package etf.unsa.ba.nwt.emajstor.communication.dto;

import org.springframework.data.geo.Point;

import java.time.LocalDateTime;
import java.util.UUID;

public class User {
    private UUID id;
    private String username;
    private String password;
    private String city;
    private Point location;
    private LocalDateTime dateCreated;
    private ContactInfo contactInfo;

    public User() {
    }

    public User(UUID id, String username, String password, String city, Point location, LocalDateTime dateCreated, ContactInfo contactInfo) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.city = city;
        this.location = location;
        this.dateCreated = dateCreated;
        this.contactInfo = contactInfo;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }
}

