package etf.unsa.ba.nwt.emajstor.job.service;

import etf.unsa.ba.nwt.emajstor.job.model.UserFoto;
import etf.unsa.ba.nwt.emajstor.job.repositories.UserFotoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserFotoService {
    private final UserFotoRepository userFotoRepository;

    public UserFotoService(UserFotoRepository userFotoRepository) {
        this.userFotoRepository = userFotoRepository;
    }

    public UserFoto addUserFoto(UserFoto userFoto) {
        return userFotoRepository.save(userFoto);
    }

    public List<UserFoto> getAllUserFoto() {
        return userFotoRepository.findAll();
    }

    public UserFoto getUserFotoById(UUID id) {
       List<UserFoto> userFotoList = getAllUserFoto();
       if (userFotoList.isEmpty()) return new UserFoto();

       Optional<UserFoto> uF = Optional.of(userFotoList.stream().filter(userFoto -> id.equals(userFoto.getUserId())).findFirst().get());
       if (uF.isPresent())
       return uF.get();

       return new UserFoto();
    }

    public UserFoto updateUserFotoById(UserFoto userFoto, UUID id) {
        Optional<UserFoto> optionalUserFoto = userFotoRepository.findById(id);
        if (optionalUserFoto.isPresent()) {
            UserFoto userFoto1 = optionalUserFoto.get();
            userFoto1.setFileEntity(userFoto.getFileEntity());
            return userFotoRepository.save(userFoto1);
        }
        return userFotoRepository.save(userFoto);
    }
}
