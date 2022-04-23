package etf.unsa.ba.nwt.emajstor.user.security;

import etf.unsa.ba.nwt.emajstor.user.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

public class PersonDetails implements UserDetails {

    private final User user;
    private Collection<? extends GrantedAuthority> authorities;

    public PersonDetails(User user) {
        this.user = user;
    }

    public PersonDetails(User user, Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    public static PersonDetails build (User person) {
        return new PersonDetails(person, person.fetchAuthorities());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public UUID getId() {
        return user.getId();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PersonDetails user = (PersonDetails) o;
        return Objects.equals(getId(), user.getId());
    }
}

