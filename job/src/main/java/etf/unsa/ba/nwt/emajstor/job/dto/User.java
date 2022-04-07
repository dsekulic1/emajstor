package etf.unsa.ba.nwt.emajstor.job.dto;


import java.time.LocalDateTime;
import java.util.UUID;

public class User {
    private UUID id;
    private String username;
    private String password;
    private String city;
    private double locationLongitude;
    private double locationLatitude;
    private LocalDateTime dateCreated;
    private ContactInfo contactInfo;

    public User() {
    }

    public User(UUID id, String username, String password, String city, double locationLongitude, double locationLatitude, LocalDateTime dateCreated, ContactInfo contactInfo) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.city = city;
        this.locationLongitude = locationLongitude;
        this.locationLatitude = locationLatitude;
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

    public double getLocationLongitude() {
        return locationLongitude;
    }

    public double getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(double locationLatitude) {
        this.locationLatitude = locationLatitude;
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

    public void setLocationLongitude(double locationLongitude) {
    }
}
