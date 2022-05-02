package etf.unsa.ba.nwt.emajstor.user.service;

import etf.unsa.ba.nwt.emajstor.user.exception.BadRequestException;
import etf.unsa.ba.nwt.emajstor.user.exception.ConflictException;
import etf.unsa.ba.nwt.emajstor.user.model.ContactInfo;
import etf.unsa.ba.nwt.emajstor.user.model.Role;
import etf.unsa.ba.nwt.emajstor.user.model.User;
import etf.unsa.ba.nwt.emajstor.user.repositories.ContactInfoRepository;
import etf.unsa.ba.nwt.emajstor.user.repositories.UserRepository;
import etf.unsa.ba.nwt.emajstor.user.request.LoginRequest;
import etf.unsa.ba.nwt.emajstor.user.request.SignupRequest;
import etf.unsa.ba.nwt.emajstor.user.request.UpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ContactInfoRepository contactInfoRepository;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, ContactInfoRepository contactInfoRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.contactInfoRepository = contactInfoRepository;
    }

    public User signup(SignupRequest signupRequest) {
        if (userRepository.existsByUsernameIgnoreCase(signupRequest.getUsername())) {
            throw new ConflictException("Username is already taken");
        }
        if (contactInfoRepository.existsByEmail(signupRequest.getEmail())) {
            throw new ConflictException("Email is already taken");
        }
        ContactInfo ci = new ContactInfo(
                signupRequest.getFirstName(),
                signupRequest.getLastName(),
                signupRequest.getEmail(),
                signupRequest.getNumber()
        );
        ContactInfo contactInfo = contactInfoRepository.save(ci);
        User p = new User(
                signupRequest.getUsername(),
                passwordEncoder.encode(signupRequest.getPassword()),
                signupRequest.getCity(),
                signupRequest.getLocationLongitude(),
                signupRequest.getLocationLatitude(),
                LocalDateTime.now(),
                contactInfo
                );
        // postavka role
        p.setRole(Role.ROLE_USER);
        User user = userRepository.save(p);
        return user;
    }

    public User updateProfile(UpdateRequest updateProfileRequest) {
        User user = userRepository.findByUsername(updateProfileRequest.getUsername())
                .orElseThrow(() -> new BadRequestException("Username doesn't exist"));

        Optional<ContactInfo> optionalContactInfo = contactInfoRepository.findByEmail(updateProfileRequest.getEmail());

        if (optionalContactInfo.isPresent() && !optionalContactInfo.get().getId().equals(user.getContactInfo().getId())) {
            throw new ConflictException("Email is already taken");
        }

        user.getContactInfo().setFirstName(updateProfileRequest.getFirstName());
        user.getContactInfo().setLastName(updateProfileRequest.getLastName());
        user.getContactInfo().setEmail(updateProfileRequest.getEmail());
        user.setCity(updateProfileRequest.getCity());
        user.setLocationLatitude(updateProfileRequest.getLocationLatitude());
        user.setLocationLongitude(updateProfileRequest.getLocationLongitude());
        user.getContactInfo().setNumber(updateProfileRequest.getNumber());

        return user;
    }

    public User login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + loginRequest.getUsername()));
        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new UsernameNotFoundException("Wrong username or password");
        }
        return user;
    }
}
