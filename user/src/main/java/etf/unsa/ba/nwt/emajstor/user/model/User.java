package etf.unsa.ba.nwt.emajstor.user.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
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

    private Point location;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreated;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contact_info_id", referencedColumnName = "id", nullable = false)
    private ContactInfo contactInfo;

    public User() {
    }

    public User(String username,
                String password,
                String city,
                Point location,
                LocalDateTime dateCreated,
                Role role
               ) {
        this.username = username;
        this.password = password;
        this.city = city;
        this.location = location;
        this.dateCreated = dateCreated;
        this.role = role;
    }

    public User(UUID id,
                String username,
                String password,
                String city,
                Point location,
                LocalDateTime dateCreated,
                Role role
                ) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.city = city;
        this.location = location;
        this.dateCreated = dateCreated;
        this.role = role;
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
