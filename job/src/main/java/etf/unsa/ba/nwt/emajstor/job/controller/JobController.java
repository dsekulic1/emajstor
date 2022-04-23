package etf.unsa.ba.nwt.emajstor.job.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import etf.unsa.ba.nwt.emajstor.job.exception.NotFoundException;
import etf.unsa.ba.nwt.emajstor.job.model.Job;
import etf.unsa.ba.nwt.emajstor.job.service.JobService;
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

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/job")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public ResponseEntity<Job> addJob(@RequestBody @Valid Job job) {
        return ResponseEntity.ok(jobService.addJob(job));
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<Job>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable UUID id) {
        return ResponseEntity.ok(jobService.getJobById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Job> updateJobById(@PathVariable UUID id, @RequestBody @Valid Job job) {
        return ResponseEntity.ok(jobService.updateJobById(job, id));
    }

    @PutMapping
    public ResponseEntity<Job> updateJob(@RequestBody @Valid Job job) {
        return ResponseEntity.ok(jobService.updateJobById(job, job.getId()));
    }

    private Job applyPatchToJob(
            JsonPatch patch, Job targetJob) throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode patched = patch.apply(objectMapper.convertValue(targetJob, JsonNode.class));
        return objectMapper.treeToValue(patched, Job.class);
    }

    @PatchMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<Job> updateJob(@PathVariable String id, @RequestBody JsonPatch patch) {
        try {
            Job user = jobService.getJobById(UUID.fromString(id));
            Job userPatched = applyPatchToJob(patch, user);
            jobService.updateJobById(userPatched, userPatched.getId());
            return ResponseEntity.ok(userPatched);
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
