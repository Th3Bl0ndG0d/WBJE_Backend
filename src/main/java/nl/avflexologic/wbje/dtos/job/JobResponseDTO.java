package nl.avflexologic.wbje.dtos.job;

import java.time.LocalDateTime;

/**
 * Uitgaand contract voor Job-gegevens.
 */
public class JobResponseDTO {
    public Long id;
    public String jobNumber;
    public LocalDateTime jobDate;
    public String jobName;
    public Integer cylinderWidth;
    public Integer cylinderCircumference;
    public String info;
    public String noteInfo;
}

