package nl.avflexologic.wbje.services;


import nl.avflexologic.wbje.dtos.cylinder.CylinderRequestDTO;
import nl.avflexologic.wbje.dtos.cylinder.CylinderResponseDTO;

import java.util.List;

public interface CylinderService {

    /**
     * Creates a new cylinder within the given job.
     */
    CylinderResponseDTO createCylinder(Long jobId, CylinderRequestDTO request);

    /**
     * Returns a single cylinder belonging to a job.
     */
    CylinderResponseDTO getCylinderById(Long jobId, Long cylinderId);

    /**
     * Returns all cylinders for a given job.
     */
    List<CylinderResponseDTO> getCylindersByJob(Long jobId);

    /**
     * Updates an existing cylinder belonging to a job.
     */
    CylinderResponseDTO updateCylinder(Long jobId, Long cylinderId, CylinderRequestDTO request);

    /**
     * Deletes a cylinder belonging to a job.
     */
    void deleteCylinder(Long jobId, Long cylinderId);
}
