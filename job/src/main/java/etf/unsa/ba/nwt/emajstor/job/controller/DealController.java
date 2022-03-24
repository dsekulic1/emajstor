package etf.unsa.ba.nwt.emajstor.job.controller;

import etf.unsa.ba.nwt.emajstor.job.model.Deal;
import etf.unsa.ba.nwt.emajstor.job.model.Job;
import etf.unsa.ba.nwt.emajstor.job.service.DealService;
import etf.unsa.ba.nwt.emajstor.job.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/deal")
@RequiredArgsConstructor
public class DealController {

    private final DealService dealService;

    @PostMapping
    public ResponseEntity<Deal> addDeal(@RequestBody @Valid Deal deal) {
        return ResponseEntity.ok(dealService.addDeal(deal));
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<Deal>> getAllDeals() {
        return ResponseEntity.ok(dealService.getAllDeals());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Deal> getDealById(@PathVariable UUID id) {
        return ResponseEntity.ok(dealService.getDealById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Deal> updateDealById(@PathVariable UUID id, @RequestBody @Valid Deal deal) {
        return ResponseEntity.ok(dealService.updateDealById(deal, id));
    }

    @PutMapping
    public ResponseEntity<Deal> updateDeal(@RequestBody @Valid Deal deal) {
        return ResponseEntity.ok(dealService.updateDealById(deal, deal.getId()));
    }
}
