package nl.avflexologic.wbje.dtos.job;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * Response payload representing a job that is stored in the system.
 */
@Schema(description = "Response payload representing a job that is stored in the system.")
public class JobResponseDTO {

    @Schema(description = "Database identifier of the job.", example = "42")
    public Long id;

    @Schema(description = "External job number used in planning or ERP.", example = "JOB-2025-001")
    public String jobNumber;

    @Schema(description = "Scheduled date and time of the job.", example = "2025-01-15T08:30:00")
    public LocalDateTime jobDate;

    @Schema(description = "Short descriptive name of the job.", example = "Print job for customer X – 4 colors")
    public String jobName;

    @Schema(description = "Effective cylinder width in millimetres.", example = "850")
    public Integer cylinderWidth;

    @Schema(description = "Effective cylinder circumference in millimetres.", example = "1100")
    public Integer cylinderCircumference;

    @Schema(description = "Optional internal note or technical information for this job.", example = "Use stickyback type A, check register on station 3.")
    public String info;

    @Schema(description = "Optional operator note attached to the job.", example = "Repeat run, verify plate wear on station 2.")
    public String noteInfo;
}

