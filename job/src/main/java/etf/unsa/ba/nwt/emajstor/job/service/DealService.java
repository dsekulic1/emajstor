package etf.unsa.ba.nwt.emajstor.job.service;

import etf.unsa.ba.nwt.emajstor.job.dto.User;
import etf.unsa.ba.nwt.emajstor.job.exception.BadRequestException;
import etf.unsa.ba.nwt.emajstor.job.model.Deal;
import etf.unsa.ba.nwt.emajstor.job.repositories.DealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.naming.ServiceUnavailableException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DealService {

    private final DealRepository dealRepository;
    private final RestTemplate restTemplate;

    public List<Deal> getAllDeals() {
        return dealRepository.findAll();
    }

    public Deal addDeal(Deal deal) throws ServiceUnavailableException {

        try {
            if (getUser(deal.getUser()) != null || getUser(deal.getJob().getUser()) != null) {
                return dealRepository.save(deal);
            } else {
                throw new BadRequestException("User or worker does not exist.");
            }
        } catch (Exception exception) {
            throw exception;
        }
    }

    public Deal getDealById(UUID id) {
        return dealRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Deal with id " + id.toString() + " does not exist."));
    }

    public Deal updateDealById(Deal deal, UUID id) throws ServiceUnavailableException {
        if (!dealRepository.existsById(id)) {
            throw new BadRequestException("Deal with id " + id.toString() + " does not exist.");
        }

        try {
            if (getUser(deal.getUser()) != null || getUser(deal.getJob().getUser()) != null) {
                return dealRepository.save(deal);
            } else {
                throw new BadRequestException("User or worker does not exist.");
            }
        } catch (Exception exception) {
            throw exception;
        }
    }

    public User getUser(final UUID id) throws ServiceUnavailableException {
        try {
            return restTemplate.getForObject(
                    "http://user/api/users/{id}",
                    User.class,
                    id
            );
        } catch (ResourceAccessException ex) {
            throw new ServiceUnavailableException("Error while communicating with another microservice.");
        }
    }
}
