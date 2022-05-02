package etf.unsa.ba.nwt.emajstor.user.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class LoginResponseBody {
    private String tokenType;
    private String jwt;
    private UUID id;
    private String username;
    private String password;
    private String city;
    private double locationLongitude;
    private double locationLatitude;
    private LocalDateTime dateCreated;
    private String firstName;
    private String lastName;
    private String email;
    private String number;
    private List<String> roles;

    public LoginResponseBody() {
    }

    public LoginResponseBody(String tokenType, String jwt, UUID id, String username, String city, double locationLongitude, double locationLatitude, LocalDateTime dateCreated, String firstName, String lastName, String email, String number, List<String> roles) {
        this.tokenType = tokenType;
        this.jwt = jwt;
        this.id = id;
        this.username = username;
        this.city = city;
        this.locationLongitude = locationLongitude;
        this.locationLatitude = locationLatitude;
        this.dateCreated = dateCreated;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.number = number;
        this.roles = roles;
    }

    public LoginResponseBody(String tokenType, String jwt, UUID id, String username, String password, String city, double locationLongitude, double locationLatitude, LocalDateTime dateCreated, String firstName, String lastName, String email, String number, List<String> roles) {
        this.tokenType = tokenType;
        this.jwt = jwt;
        this.id = id;
        this.username = username;
        this.password = password;
        this.city = city;
        this.locationLongitude = locationLongitude;
        this.locationLatitude = locationLatitude;
        this.dateCreated = dateCreated;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.number = number;
        this.roles = roles;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(double locationLongitude) {
        this.locationLongitude = locationLongitude;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
