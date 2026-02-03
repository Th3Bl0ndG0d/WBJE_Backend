package nl.avflexologic.wbje.mappers;

import nl.avflexologic.wbje.dtos.cylinder.CylinderRequestDTO;
import nl.avflexologic.wbje.dtos.cylinder.CylinderResponseDTO;
import nl.avflexologic.wbje.entities.CylinderEntity;
import nl.avflexologic.wbje.entities.JobEntity;
import nl.avflexologic.wbje.entities.TapeSpecEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CylinderDTOMapper {

    public CylinderResponseDTO mapToDto(CylinderEntity cylinder) {
        if (cylinder == null) return null;

        Long jobId = (cylinder.getJob() != null) ? cylinder.getJob().getId() : null;
        Long tapeSpecId = (cylinder.getTapeSpec() != null) ? cylinder.getTapeSpec().getId() : null;

        return new CylinderResponseDTO(
                cylinder.getId(),
                cylinder.getCylinderNr(),
                cylinder.getColor(),
                cylinder.getCylinderInfo(),
                jobId,
                tapeSpecId
        );
    }

    public List<CylinderResponseDTO> mapToDto(List<CylinderEntity> cylinders) {
        List<CylinderResponseDTO> result = new ArrayList<>();
        if (cylinders == null) return result;

        for (CylinderEntity cylinder : cylinders) {
            result.add(mapToDto(cylinder));
        }
        return result;
    }

    public CylinderEntity mapToEntity(CylinderRequestDTO dto) {
        throw new UnsupportedOperationException(
                "Cylinder mapping requires a JobEntity and TapeSpecEntity. Use mapToEntity(dto, job, tapeSpec)."
        );
    }

    /**
     * Creates a CylinderEntity based on request input and required parent references.
     */
    public CylinderEntity mapToEntity(CylinderRequestDTO dto, JobEntity job, TapeSpecEntity tapeSpec) {
        if (dto == null) return null;

        CylinderEntity cylinder = new CylinderEntity(job, tapeSpec, dto.cylinderNr());
        cylinder.setColor(dto.color());
        cylinder.setCylinderInfo(dto.cylinderInfo());
        return cylinder;
    }

    /**
     * Applies request input to an existing entity.
     * Job ownership is managed by the Job aggregate.
     */
    public void updateEntity(CylinderEntity cylinder, CylinderRequestDTO dto, TapeSpecEntity tapeSpec) {
        if (cylinder == null || dto == null) return;

        cylinder.setCylinderNr(dto.cylinderNr());
        cylinder.setColor(dto.color());
        cylinder.setCylinderInfo(dto.cylinderInfo());
        cylinder.setTapeSpec(tapeSpec);
    }
}
