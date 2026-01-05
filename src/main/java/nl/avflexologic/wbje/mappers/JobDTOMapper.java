package nl.avflexologic.wbje.mappers;

import nl.avflexologic.wbje.dtos.job.JobRequestDTO;
import nl.avflexologic.wbje.dtos.job.JobResponseDTO;
import nl.avflexologic.wbje.entities.JobEntity;
import nl.avflexologic.wbje.entities.NoteEntity;

public class JobDTOMapper {

    public static JobEntity mapToEntity(JobRequestDTO dto) {
        JobEntity job = new JobEntity(dto.jobDate);
        job.setJobNumber(dto.getJobNumber());
        job.setJobName(dto.getJobName());
        job.setCylinderWidth(dto.getCylinderWidth());
        job.setCylinderCircumference(dto.getCylinderCircumference());
        job.setInfo(dto.getInfo());

        if (dto.getNoteInfo() != null && !dto.getNoteInfo().isBlank()) {
            job.setNote(new NoteEntity(dto.getNoteInfo()));
        } else {
            job.setNote(null);
        }
        return job;
    }

    /**
     * Applies request fields to an existing JobEntity.
     * The entity identity is not replaced; only mutable state is updated.
     */
    public static void updateEntity(JobEntity job, JobRequestDTO dto) {
        job.setJobNumber(dto.getJobNumber());
        job.setJobDate(dto.getJobDate());
        job.setJobName(dto.getJobName());
        job.setCylinderWidth(dto.getCylinderWidth());
        job.setCylinderCircumference(dto.getCylinderCircumference());
        job.setInfo(dto.getInfo());

        if (dto.getNoteInfo() != null && !dto.getNoteInfo().isBlank()) {
            if (job.getNote() == null) {
                job.setNote(new NoteEntity(dto.getNoteInfo()));
            } else {
                job.getNote().setInfo(dto.getNoteInfo());
            }
        } else {
            job.setNote(null);
        }
    }

    public static JobResponseDTO mapToDto(JobEntity job) {
        JobResponseDTO dto = new JobResponseDTO();
        dto.id = job.getId();
        dto.jobNumber = job.getJobNumber();
        dto.jobDate = job.getJobDate();
        dto.jobName = job.getJobName();
        dto.cylinderWidth = job.getCylinderWidth();
        dto.cylinderCircumference = job.getCylinderCircumference();
        dto.info = job.getInfo();

        if (job.getNote() != null) {
            dto.noteInfo = job.getNote().getInfo();
        }

        return dto;
    }
}
