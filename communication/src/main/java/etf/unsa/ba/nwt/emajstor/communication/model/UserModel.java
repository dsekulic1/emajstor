package etf.unsa.ba.nwt.emajstor.communication.model;

import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import java.util.UUID;

@Entity
public class UserModel {
    @Id
    @Type(type = "uuid-char")
    private UUID id;

    @Size(max = 255)
    private String username;

    @Size(max = 255)
    private String email;

    public UserModel() {
    }

    public UserModel(UUID id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
