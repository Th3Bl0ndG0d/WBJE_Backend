package nl.avflexologic.wbje.dtos.job;

import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;
import jakarta.validation.constraints.Size;
/**
 * Inkomend contract voor het aanmaken en bijwerken van een Job.
 */
public class JobRequestDTO {
    @Size(max = 40, message = "jobNumber must not exceed 40 characters.")
    public String jobNumber;
//    @NotNull(message = "jobDate is required.")
    public LocalDateTime jobDate;
    @Size(max = 255, message = "jobName must not exceed 255 characters.")
    public String jobName;
    public Integer cylinderWidth;
    public Integer cylinderCircumference;
    @Size(max = 255, message = "info must not exceed 255 characters.")
    public String info;
    public String noteInfo;


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

    public String getNoteInfo() {
        return noteInfo;
    }

    public void setNoteInfo(String noteInfo) {
        this.noteInfo = noteInfo;
    }
}
