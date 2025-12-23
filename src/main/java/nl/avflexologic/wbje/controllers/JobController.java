package nl.avflexologic.wbje.controllers;

import nl.avflexologic.wbje.dtos.job.JobRequestDTO;
import nl.avflexologic.wbje.dtos.job.JobResponseDTO;
import nl.avflexologic.wbje.services.JobService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


/**
 * REST controller, job-related endpoints.
 */
@RestController
@RequestMapping("/jobs")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService= jobService;
    }
    /**
     * Creates a new job based on the incoming request data.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JobResponseDTO createJob(@RequestBody JobRequestDTO request) {
        return jobService.createJob(request);
    }

    /**
     * Returns a single job for the given identifier.
     */
    @GetMapping("/{id}")
    public JobResponseDTO getJobById(@PathVariable Long id) {
        return jobService.getJobById(id);
    }


}
