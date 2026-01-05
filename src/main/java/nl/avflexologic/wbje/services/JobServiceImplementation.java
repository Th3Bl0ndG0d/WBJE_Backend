package nl.avflexologic.wbje.services;

import nl.avflexologic.wbje.dtos.job.JobRequestDTO;
import nl.avflexologic.wbje.dtos.job.JobResponseDTO;
import nl.avflexologic.wbje.entities.JobEntity;
import nl.avflexologic.wbje.exceptions.ResourceNotFoundException;
import nl.avflexologic.wbje.mappers.JobDTOMapper;
import nl.avflexologic.wbje.repositories.JobRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobServiceImplementation implements JobService {

    private final JobRepository jobRepository;

    public JobServiceImplementation(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public JobResponseDTO createJob(JobRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("Job request must not be null.");
        }

        JobEntity entity = JobDTOMapper.mapToEntity(request);
        JobEntity saved = jobRepository.save(entity);
        return JobDTOMapper.mapToDto(saved);
    }

    @Override
    public JobResponseDTO getJobById(Long id) {
        return jobRepository.findById(id)
                .map(JobDTOMapper::mapToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found for id: " + id));
    }

    /**
     * Returns all jobs in the system.
     */
    @Override
    public List<JobResponseDTO> getAllJobs() {
        List<JobEntity> entities = jobRepository.findAll();
        List<JobResponseDTO> result = new ArrayList<>();
        for (JobEntity entity : entities) {
            result.add(JobDTOMapper.mapToDto(entity));
        }
        return result;
    }

    /**
     * Updates an existing job. The job must exist before it can be updated.
     */
    @Override
    public JobResponseDTO updateJob(Long id, JobRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("Job request must not be null.");
        }

        JobEntity existing = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found for id: " + id));

        JobDTOMapper.updateEntity(existing, request);

        JobEntity saved = jobRepository.save(existing);
        return JobDTOMapper.mapToDto(saved);
    }

    /**
     * Deletes an existing job. The job must exist before it can be deleted.
     */
    @Override
    public void deleteJob(Long id) {
        JobEntity existing = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found for id: " + id));

        jobRepository.delete(existing);
    }
}
