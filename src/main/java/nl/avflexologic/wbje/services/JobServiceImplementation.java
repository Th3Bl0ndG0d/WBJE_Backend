package nl.avflexologic.wbje.services;

import nl.avflexologic.wbje.dtos.job.JobRequestDTO;
import nl.avflexologic.wbje.dtos.job.JobResponseDTO;
import nl.avflexologic.wbje.entities.JobEntity;
import nl.avflexologic.wbje.mappers.JobDTOMapper;
import nl.avflexologic.wbje.repositories.JobRepository;

import java.util.Optional;

public class JobServiceImplementation implements JobService {

    private final JobRepository jobRepository;

    public JobServiceImplementation(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public JobResponseDTO createJob(JobRequestDTO request) {
        validateRequest(request);

        JobEntity entity = JobDTOMapper.toEntity(request);
        JobEntity saved = jobRepository.save(entity);

        return JobDTOMapper.toDto(saved);
    }

    @Override
    public JobResponseDTO getJobById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Job id is required.");
        }

        Optional<JobEntity> result = jobRepository.findById(id);
        JobEntity entity = result.orElseThrow(() -> new IllegalArgumentException("Job not found for id: " + id));

        return JobDTOMapper.toDto(entity);
    }

    /**
     * Validates the minimal invariants of the incoming job request.
     */
    private void validateRequest(JobRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("JobRequestDTO is required.");
        }
        if (request.jobDate == null) {
            throw new IllegalArgumentException("jobDate is required.");
        }
    }
}
