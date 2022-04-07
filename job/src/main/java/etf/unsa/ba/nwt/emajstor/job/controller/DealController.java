package etf.unsa.ba.nwt.emajstor.job.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import etf.unsa.ba.nwt.emajstor.job.exception.NotFoundException;
import etf.unsa.ba.nwt.emajstor.job.model.Deal;
import etf.unsa.ba.nwt.emajstor.job.model.Job;
import etf.unsa.ba.nwt.emajstor.job.service.DealService;
import etf.unsa.ba.nwt.emajstor.job.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;

import javax.naming.ServiceUnavailableException;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/deal")
@RequiredArgsConstructor
public class DealController {

    private final DealService dealService;

    @PostMapping
    public ResponseEntity<Deal> addDeal(@RequestBody @Valid Deal deal) throws ServiceUnavailableException {
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
    public ResponseEntity<Deal> updateDealById(@PathVariable UUID id, @RequestBody @Valid Deal deal) throws ServiceUnavailableException {
        return ResponseEntity.ok(dealService.updateDealById(deal, id));
    }

    @PutMapping
    public ResponseEntity<Deal> updateDeal(@RequestBody @Valid Deal deal) throws ServiceUnavailableException {
        return ResponseEntity.ok(dealService.updateDealById(deal, deal.getId()));
    }

    private Deal applyPatchToDeal(
            JsonPatch patch, Deal targetDeal) throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode patched = patch.apply(objectMapper.convertValue(targetDeal, JsonNode.class));
        return objectMapper.treeToValue(patched, Deal.class);
    }

    @PatchMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<Deal> updateDeal(@PathVariable String id, @RequestBody JsonPatch patch) throws ServiceUnavailableException {
        try {
            Deal deal = dealService.getDealById(UUID.fromString(id));
            Deal dealPatched = applyPatchToDeal(patch, deal);
            dealService.updateDealById(dealPatched, dealPatched.getId());
            return ResponseEntity.ok(dealPatched);
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (ResourceAccessException ex) {
            throw new ServiceUnavailableException("Error while communicating with another microservice.");
        }
    }
}
