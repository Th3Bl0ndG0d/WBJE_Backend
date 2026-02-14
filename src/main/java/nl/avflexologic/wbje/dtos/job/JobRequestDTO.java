package nl.avflexologic.wbje.dtos.job;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import nl.avflexologic.wbje.dtos.note.NoteRequestDTO;

/**
 * Request payload used to create or update a job.
 */
@Schema(description = "Request payload used to create or update a job.")
public class JobRequestDTO {
    @Schema(
            description = "External job number used in planning or ERP.",
            example = "JOB-2025-001"
    )
    @Size(max = 40, message = "jobNumber must not exceed 40 characters.")
    public String jobNumber;
    @Schema(
            description = "Scheduled date and time of the job.",
            example = "2025-01-15T08:30:00",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "jobDate is required.")
    public LocalDateTime jobDate;
    @Schema(
            description = "Short descriptive name of the job.",
            example = "Print job for customer X – 4 colors"
    )
    @Size(max = 255, message = "jobName must not exceed 255 characters.")
    public String jobName;
    @Schema(
            description = "Effective cylinder width in millimetres.",
            example = "850",
            minimum = "0",
            maximum = "168000"
    )
    @Min(0)
    @Max(168000)
    public Integer cylinderWidth;
    @Schema(
            description = "Effective cylinder circumference in micrometers.",
            example = "1100",
            minimum = "0",
            maximum = "1250000"
    )
    @Min(0)
    @Max(1250000)
    public Integer cylinderCircumference;
    @Schema(description = "Optional internal note or technical information for this job.", example = "Use stickyback type A, check register on station 3.")
    @Size(max = 255, message = "info must not exceed 255 characters.")
    public String info;
    @Schema(description = "Optional note attached to the job.")
    public NoteRequestDTO note;




    public JobRequestDTO(LocalDateTime jobDate) {
        this.jobDate = jobDate;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public LocalDateTime getJobDate() {
        return jobDate;
    }

    public void setJobDate(LocalDateTime jobDate) {
        this.jobDate = jobDate;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Integer getCylinderWidth() {
        return cylinderWidth;
    }

    public void setCylinderWidth(Integer cylinderWidth) {
        this.cylinderWidth = cylinderWidth;
    }

    public Integer getCylinderCircumference() {
        return cylinderCircumference;
    }

    public void setCylinderCircumference(Integer cylinderCircumference) {
        this.cylinderCircumference = cylinderCircumference;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public NoteRequestDTO getNote() {
        return note;
    }

    public void setNote(NoteRequestDTO note) {
        this.note = note;
    }

}
