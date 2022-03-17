package etf.unsa.ba.nwt.emajstor.user.service;

import etf.unsa.ba.nwt.emajstor.user.repository.ContactInfoRepository;
import etf.unsa.ba.nwt.emajstor.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ContactInfoRepository contactInfoRepository;


    public UserService(UserRepository userRepository, ContactInfoRepository contactInfoRepository) {
        this.userRepository = userRepository;
        this.contactInfoRepository = contactInfoRepository;
    }
}
