package etf.unsa.ba.nwt.emajstor.user.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Entity
@Table(name="contactInfo")
public class ContactInfo {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private UUID id;

    @Column(nullable = false)
    @NotBlank
    @Size(min = 2, max = 50)
    private String firstName;

    @Column(nullable = false)
    @NotBlank
    @Size(min = 2, max = 50)
    private String lastName;

    @Column(nullable = false, unique = true)
    @NotBlank
    @Size(max = 320)
    @Email
    private String email;

    @Column(nullable = false)
    @NotBlank
    @Size(min = 9, max = 17)
    private String number;

    public ContactInfo() {
    }

    public ContactInfo(String firstName,
                       String lastName,
                       String email,
                       String number) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.number = number;
    }

    public ContactInfo(UUID id,
                       String firstName,
                       String lastName,
                       String email,
                       String number) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.number = number;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
}
