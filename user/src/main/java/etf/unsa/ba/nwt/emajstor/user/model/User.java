package etf.unsa.ba.nwt.emajstor.user.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private UUID id;

    @Column(nullable = false, unique = true)
    @NotBlank
    private String username;

    @Column(nullable = false)
    @NotBlank
    @Size(max = 128)
    private String password;

    private String city;

    private double locationLongitude;

    private double locationLatitude;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreated;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contact_info_id", referencedColumnName = "id", nullable = false)
    private ContactInfo contactInfo;

    public User() {
    }

    public User(String username,
                String password,
                String city,
                double locationLongitude,
                double locationLatitude,
                LocalDateTime dateCreated,
                ContactInfo contactInfo) {
        this.username = username;
        this.password = password;
        this.city = city;
        this.locationLongitude = locationLongitude;
        this.locationLatitude = locationLatitude;
        this.dateCreated = dateCreated;
        this.contactInfo = contactInfo;
    }

    public User(String username,
                String password,
                String city,
                double locationLongitude,
                double locationLatitude,
                LocalDateTime dateCreated,
                Role role,
                ContactInfo contactInfo) {
        this.username = username;
        this.password = password;
        this.city = city;
        this.locationLongitude = locationLongitude;
        this.locationLatitude = locationLatitude;
        this.dateCreated = dateCreated;
        this.role = role;
        this.contactInfo = contactInfo;
    }

    public User(UUID id,
                String username,
                String password,
                String city,
                double locationLongitude,
                double locationLatitude,
                LocalDateTime dateCreated,
                Role role,
                ContactInfo contactInfo) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.city = city;
        this.locationLongitude = locationLongitude;
        this.locationLatitude = locationLatitude;
        this.dateCreated = dateCreated;
        this.role = role;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public List<GrantedAuthority> fetchAuthorities() {
        Set<Role> rl = new HashSet<>();
        rl.add(this.getRole());
        if (this.getRole() == Role.ROLE_ADMIN)  rl.add(Role.ROLE_ADMIN);
        // reserved for future roles
        return rl.stream().map(role -> new SimpleGrantedAuthority(role.name())).collect(Collectors.toList());
    }
}
