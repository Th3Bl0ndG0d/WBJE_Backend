package nl.avflexologic.wbje.services;
import nl.avflexologic.wbje.dtos.job.JobRequestDTO;
import nl.avflexologic.wbje.dtos.job.JobResponseDTO;
import java.util.List;

public interface JobService {

    /**
     * Creates a new job aggregate based on the given request data.
     */
    JobResponseDTO createJob(JobRequestDTO request);
    /**
     * Returns a single job by its identifier.
     */
    JobResponseDTO getJobById(Long id);

    List<JobResponseDTO> getAllJobs();

    JobResponseDTO updateJob(Long id, JobRequestDTO request);

    void deleteJob(Long id);

}
