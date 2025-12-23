package nl.avflexologic.wbje.dtos.job;

import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Inkomend contract voor het aanmaken en bijwerken van een Job.
 */
public class JobRequestDTO {
    public String jobNumber;
    @NotNull
    public LocalDateTime jobDate;
    public String jobName;
    public Integer cylinderWidth;
    public Integer cylinderCircumference;
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
