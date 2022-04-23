package etf.unsa.ba.nwt.emajstor.user.controller;

import etf.unsa.ba.nwt.emajstor.user.model.Role;
import etf.unsa.ba.nwt.emajstor.user.model.User;
import etf.unsa.ba.nwt.emajstor.user.request.LoginRequest;
import etf.unsa.ba.nwt.emajstor.user.request.SignupRequest;
import etf.unsa.ba.nwt.emajstor.user.request.UpdateRequest;
import etf.unsa.ba.nwt.emajstor.user.response.LoginResponseBody;
import etf.unsa.ba.nwt.emajstor.user.security.JwtUtils;
import etf.unsa.ba.nwt.emajstor.user.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtils jwtTokenUtil;
    private final AuthService userService;

    @Autowired
    public AuthController(JwtUtils jwtTokenUtil, AuthService userService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<LoginResponseBody> signup(@RequestBody @Valid SignupRequest signupRequest) {
        User user = userService.signup(signupRequest);
        ArrayList<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");
        String token = jwtTokenUtil.generateToken(user);
        return ResponseEntity.ok().body(new LoginResponseBody(
                "Bearer",
                token,
                user.getId(),
                user.getUsername(),
                user.getCity(),
                user.getLocationLongitude(),
                user.getLocationLatitude(),
                user.getDateCreated(),
                user.getContactInfo().getFirstName(),
                user.getContactInfo().getLastName(),
                user.getContactInfo().getEmail(),
                user.getContactInfo().getNumber(),
                roles
        ));
    }

    @PutMapping("/update-profile")
    public ResponseEntity<LoginResponseBody> updateProfile(@RequestBody @Valid UpdateRequest updateProfileRequest) {
        User user = userService.updateProfile(updateProfileRequest);
        String token = jwtTokenUtil.generateToken(user);
        ArrayList<String> roles = new ArrayList<>();
        roles.add(user.getRole().name());
        if (user.getRole() == Role.ROLE_ADMIN) {
            roles.add("ROLE_USER");
        }
        return ResponseEntity.ok().body(new LoginResponseBody(
                "Bearer",
                token,
                user.getId(),
                user.getUsername(),
                user.getCity(),
                user.getLocationLongitude(),
                user.getLocationLatitude(),
                user.getDateCreated(),
                user.getContactInfo().getFirstName(),
                user.getContactInfo().getLastName(),
                user.getContactInfo().getEmail(),
                user.getContactInfo().getNumber(),
                roles
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseBody> login(@RequestBody @Valid LoginRequest loginRequest) {
        User user = userService.login(loginRequest);
        String token = jwtTokenUtil.generateToken(user);
        ArrayList<String> roles = new ArrayList<>();
        roles.add(user.getRole().name());
        if (user.getRole() == Role.ROLE_ADMIN) {
            roles.add("ROLE_USER");
        }
        return ResponseEntity.ok().body(new LoginResponseBody(
                "Bearer",
                token,
                user.getId(),
                user.getUsername(),
                user.getCity(),
                user.getLocationLongitude(),
                user.getLocationLatitude(),
                user.getDateCreated(),
                user.getContactInfo().getFirstName(),
                user.getContactInfo().getLastName(),
                user.getContactInfo().getEmail(),
                user.getContactInfo().getNumber(),
                roles
        ));
    }

}
