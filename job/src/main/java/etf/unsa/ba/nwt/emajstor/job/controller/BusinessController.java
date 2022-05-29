package etf.unsa.ba.nwt.emajstor.job.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import etf.unsa.ba.nwt.emajstor.job.exception.NotFoundException;
import etf.unsa.ba.nwt.emajstor.job.model.Business;
import etf.unsa.ba.nwt.emajstor.job.service.BusinessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
public class BusinessController {

    private final BusinessService businessService;

    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @PostMapping
    public ResponseEntity<Business> addBusiness(@RequestBody @Valid Business business) {
        return ResponseEntity.ok(businessService.addBusiness(business));
    }

    @PostMapping("/{business}")
    public ResponseEntity<Business> addOrGetBusiness(@PathVariable String business) {
        return ResponseEntity.ok(businessService.addOrGetBusiness(business));
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

    private Business applyPatchToBusiness(
            JsonPatch patch,Business targetBusiness) throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode patched = patch.apply(objectMapper.convertValue(targetBusiness, JsonNode.class));
        return objectMapper.treeToValue(patched, Business.class);
    }

    @PatchMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<Business> updateBusiness(@PathVariable String id, @RequestBody JsonPatch patch) {
        try {
            Business business = businessService.getBusinessById(UUID.fromString(id));
            Business businessPatched = applyPatchToBusiness(patch, business);
            businessService.updateBusinessById(businessPatched, businessPatched.getId());
            return ResponseEntity.ok(businessPatched);
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
