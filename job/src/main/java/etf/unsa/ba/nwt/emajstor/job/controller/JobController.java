package etf.unsa.ba.nwt.emajstor.job.controller;

import etf.unsa.ba.nwt.emajstor.job.model.Job;
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
@RequestMapping("/api/job")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

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
}
