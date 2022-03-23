package etf.unsa.ba.nwt.emajstor.job.service;

import etf.unsa.ba.nwt.emajstor.job.exception.BadRequestException;
import etf.unsa.ba.nwt.emajstor.job.exception.ConflictException;
import etf.unsa.ba.nwt.emajstor.job.exception.NotFoundException;
import etf.unsa.ba.nwt.emajstor.job.model.Business;
import etf.unsa.ba.nwt.emajstor.job.repositories.BusinessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BusinessService {

    private final BusinessRepository businessRepository;

    public List<Business> getAllBusiness() {
        return businessRepository.findAll();
    }

    public Business addBusiness(Business business) {
        try {
            if (businessRepository.existsByNameIgnoreCase(business.getName())) {
                throw new ConflictException("Name already taken.");
            }
            return businessRepository.save(business);
        } catch (ConflictException conflictException) {
            throw conflictException;
        }
    }

    public Business getBusinessByName(String name) {
        return businessRepository.findByName(name)
                .orElseThrow(() -> new BadRequestException("Business with name " + name + " does not exist."));
    }

    public Business getBusinessById(UUID id) {
        return businessRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Business with id " + id + " does not exist."));
    }

    public Business updateBusinessById(Business business, UUID id) {
        if(!businessRepository.existsById(id)) {
            throw new NotFoundException("Business with id " + id + " does not exist.");
         } else if (businessRepository.existsByNameIgnoreCase(business.getName())) {
             throw new NotFoundException("Business with name " + business.getName() + " already exist.");
         } else {
             return businessRepository.save(business);
         }
    }

    public Business deleteBusinessById(UUID id) {
        Business business = getBusinessById(id);
        businessRepository.deleteById(id);

        return business;
    }
}
