package nl.avflexologic.wbje.services;

import nl.avflexologic.wbje.dtos.cylinder.CylinderRequestDTO;
import nl.avflexologic.wbje.dtos.cylinder.CylinderResponseDTO;
import nl.avflexologic.wbje.entities.CylinderEntity;
import nl.avflexologic.wbje.entities.JobEntity;
import nl.avflexologic.wbje.entities.TapeSpecEntity;
import nl.avflexologic.wbje.exceptions.EntityInUseException;
import nl.avflexologic.wbje.exceptions.ResourceNotFoundException;
import nl.avflexologic.wbje.mappers.CylinderDTOMapper;
import nl.avflexologic.wbje.repositories.CylinderRepository;
import nl.avflexologic.wbje.repositories.JobRepository;
import nl.avflexologic.wbje.repositories.ReportRepository;
import nl.avflexologic.wbje.repositories.TapeSpecRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@Transactional
public class CylinderServiceImplementation implements CylinderService {

    private final JobRepository jobRepository;
    private final CylinderRepository cylinderRepository;
    private final TapeSpecRepository tapeSpecRepository;
    private final CylinderDTOMapper cylinderDTOMapper;
    public CylinderServiceImplementation(
            JobRepository jobRepository,
            CylinderRepository cylinderRepository,
            TapeSpecRepository tapeSpecRepository,
            CylinderDTOMapper cylinderDTOMapper
    ) {
        this.jobRepository = jobRepository;
        this.cylinderRepository = cylinderRepository;
        this.tapeSpecRepository = tapeSpecRepository;
        this.cylinderDTOMapper = cylinderDTOMapper;
    }

    @Override
    public CylinderResponseDTO createCylinder(Long jobId, CylinderRequestDTO request) {

        JobEntity job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));

        TapeSpecEntity tapeSpec = tapeSpecRepository.findById(request.tapeSpecId())
                .orElseThrow(() -> new ResourceNotFoundException("TapeSpec not found with id: " + request.tapeSpecId()));

        //prevent duplicate cylinderNr within same job
        if (cylinderRepository.existsByJobIdAndCylinderNr(jobId, request.cylinderNr())) {
            throw new IllegalArgumentException(
                    "Cylinder number " + request.cylinderNr() + " already exists within this job."
            );
        }

        CylinderEntity cylinder = cylinderDTOMapper.mapToEntity(request, job, tapeSpec);

        /*
         * The aggregate root is Job.
         * Adding via Job ensures the bidirectional association remains consistent.
         */
        job.addCylinder(cylinder);

        CylinderEntity saved = cylinderRepository.save(cylinder);
        return cylinderDTOMapper.mapToDto(saved);
    }


    @Override
    @Transactional(readOnly = true)
    public CylinderResponseDTO getCylinderById(Long jobId, Long cylinderId) {
        CylinderEntity cylinder = cylinderRepository.findByIdAndJob_Id(cylinderId, jobId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cylinder not found with id: " + cylinderId + " for job: " + jobId
                ));

        return cylinderDTOMapper.mapToDto(cylinder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CylinderResponseDTO> getCylindersByJob(Long jobId) {
        if (!jobRepository.existsById(jobId)) {
            throw new ResourceNotFoundException("Job not found with id: " + jobId);
        }

        List<CylinderEntity> cylinders = cylinderRepository.findAllByJob_Id(jobId);
        return cylinderDTOMapper.mapToDto(cylinders);
    }

    @Override
    public CylinderResponseDTO updateCylinder(Long jobId, Long cylinderId, CylinderRequestDTO request) {
        CylinderEntity cylinder = cylinderRepository.findByIdAndJob_Id(cylinderId, jobId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cylinder not found with id: " + cylinderId + " for job: " + jobId
                ));

        TapeSpecEntity tapeSpec = tapeSpecRepository.findById(request.tapeSpecId())
                .orElseThrow(() -> new ResourceNotFoundException("TapeSpec not found with id: " + request.tapeSpecId()));

        cylinderDTOMapper.updateEntity(cylinder, request, tapeSpec);

        CylinderEntity saved = cylinderRepository.save(cylinder);
        return cylinderDTOMapper.mapToDto(saved);
    }

    @Override
    public void deleteCylinder(Long jobId, Long cylinderId) {
        CylinderEntity cylinder = cylinderRepository.findByIdAndJob_Id(cylinderId, jobId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cylinder not found with id: " + cylinderId + " for job: " + jobId
                ));

        /*
         * Removing via Job enforces orphanRemoval semantics if configured.
         * This keeps the aggregate consistent even if the repository is used elsewhere.
         */
        JobEntity job = cylinder.getJob();
        if (job != null) {
            job.removeCylinder(cylinder);
        }

        cylinderRepository.delete(cylinder);
    }
}
