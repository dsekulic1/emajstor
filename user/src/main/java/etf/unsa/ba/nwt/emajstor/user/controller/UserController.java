package etf.unsa.ba.nwt.emajstor.user.controller;

import etf.unsa.ba.nwt.emajstor.user.model.User;
import etf.unsa.ba.nwt.emajstor.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> addPerson(@RequestBody @Valid User user) {
        return ResponseEntity.ok(userService.addUser(user));
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<User>> getAllPersons() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserByUsername(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUserByID(@PathVariable UUID id, @RequestBody @Valid User user ) {
        return ResponseEntity.ok(userService.updateUserById(user, id));
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody @Valid User user ) {
        return ResponseEntity.ok(userService.updateUserById(user, user.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.deleteUserById(id));
    }
}
