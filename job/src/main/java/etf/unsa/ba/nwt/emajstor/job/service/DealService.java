package etf.unsa.ba.nwt.emajstor.job.service;

import etf.unsa.ba.nwt.emajstor.job.exception.BadRequestException;
import etf.unsa.ba.nwt.emajstor.job.model.Deal;
import etf.unsa.ba.nwt.emajstor.job.repositories.DealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DealService {

    private final DealRepository dealRepository;

    public List<Deal> getAllDeals() {
        return dealRepository.findAll();
    }

    public Deal addDeal(Deal deal) {
        return dealRepository.save(deal);
    }

    public Deal getDealById(UUID id) {
        return dealRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Deal with id " + id.toString() + " does not exist."));
    }

    public Deal updateDealById(Deal deal, UUID id) {
        if (!dealRepository.existsById(id)) {
            throw new BadRequestException("Deal with id " + id.toString() + " does not exist.");
        }

        return dealRepository.save(deal);
    }
}
