package etf.unsa.ba.nwt.emajstor.job.controller;

import etf.unsa.ba.nwt.emajstor.job.model.Business;
import etf.unsa.ba.nwt.emajstor.job.service.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/api/business")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService businessService;

    @PostMapping
    public ResponseEntity<Business> addBusiness(@RequestBody @Valid Business business) {
        return ResponseEntity.ok(businessService.addBusiness(business));
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<Business>> getAllPersons() {
        return ResponseEntity.ok(businessService.getAllBusiness());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Business> getBusinessByName(@PathVariable String name) {
        return ResponseEntity.ok(businessService.getBusinessByName(name));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Business> getBusinessById(@PathVariable UUID id) {
        return ResponseEntity.ok(businessService.getBusinessById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Business> getBusinessById(@PathVariable UUID id, @RequestBody @Valid Business business) {
        return ResponseEntity.ok(businessService.updateBusinessById(business, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Business> deleteBusinessById(@PathVariable UUID id) {
        return ResponseEntity.ok(businessService.deleteBusinessById(id));
    }

}
