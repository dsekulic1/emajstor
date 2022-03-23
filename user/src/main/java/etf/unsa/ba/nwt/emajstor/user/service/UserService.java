package etf.unsa.ba.nwt.emajstor.user.service;

import etf.unsa.ba.nwt.emajstor.user.exception.BadRequestException;
import etf.unsa.ba.nwt.emajstor.user.exception.ConflictException;
import etf.unsa.ba.nwt.emajstor.user.exception.NotFoundException;
import etf.unsa.ba.nwt.emajstor.user.model.ContactInfo;
import etf.unsa.ba.nwt.emajstor.user.model.User;
import etf.unsa.ba.nwt.emajstor.user.repositories.ContactInfoRepository;
import etf.unsa.ba.nwt.emajstor.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ContactInfoRepository contactInfoRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User addUser(User user) {
        try {
            ContactInfo contactInfo = user.getContactInfo();
            if (contactInfo == null) {
                throw new BadRequestException("Contact info can not be null.");
            }

            if (contactInfoRepository.existsByEmail(contactInfo.getEmail())) {
                throw new ConflictException("Email already taken.");
            } else if (userRepository.existsByUsernameIgnoreCase(user.getUsername())) {
                throw new ConflictException("Username already taken.");
            } else {
               contactInfo = contactInfoRepository.save(contactInfo);
               user.setContactInfo(contactInfo);
               return userRepository.save(user);
            }
        } catch (ConflictException exception) {
            throw exception;
        }
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("User with id " + id.toString() + " does not exist."));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("User with username " + username.toString() + " does not exist."));
    }

    public User updateUserById(User user, UUID id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new NotFoundException("User with id " + id + " does not exist.");
        } else {
            try {
                ContactInfo contactInfo = contactInfoRepository.save(user.getContactInfo());
                user.setContactInfo(contactInfo);
                return userRepository.save(user);
            } catch (Exception exception) {
                throw new BadRequestException("Email or username already exists. Please change it");
            }
        }
    }

    public User deleteUserById(UUID id) {
        User user = getUserById(id);

        if(user != null) {
            userRepository.deleteById(user.getId());
            contactInfoRepository.delete(user.getContactInfo());
        }

        return user;
    }

}
