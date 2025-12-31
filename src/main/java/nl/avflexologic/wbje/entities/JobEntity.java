package nl.avflexologic.wbje.entities;


import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "jobs")
public class JobEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_number", length = 40)
    private String jobNumber;

    @Column(name = "job_date", nullable = false)
    private LocalDateTime jobDate;

    @Column(name = "job_name", length = 255)
    private String jobName;

    @Column(name = "cylinder_width")
    private Integer cylinderWidth;

    @Column(name = "cylinder_circumference")
    private Integer cylinderCircumference;

    @Column(name = "info", length = 255)
    private String info;




    protected JobEntity() { }

    public JobEntity(LocalDateTime jobDate) {
        this.jobDate = jobDate;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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




    @OneToOne(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private NoteEntity note;

    public NoteEntity getNote() { return note; }

    /** Beheert de 1-1 relatie consistent. */
    public void setNote(NoteEntity note) {
        if (note == null) {
            if (this.note != null) this.note.setJob(null);
            this.note = null;
            return;
        }
        note.setJob(this);
        this.note = note;
    }

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CylinderEntity> cylinders = new ArrayList<>();

    public List<CylinderEntity> getCylinders() {
        return cylinders;
    }
    /** Keeps the 1..* relationship consistent. */
    public void addCylinder(CylinderEntity cylinder) {
        if (cylinder == null) return;
        if (!this.cylinders.contains(cylinder)) {
            this.cylinders.add(cylinder);
        }
        cylinder.setJob(this);
    }

    /** Orphan removal deletes the cylinder when it is removed from the job. */
    public void removeCylinder(CylinderEntity cylinder) {
        if (cylinder == null) return;

        this.cylinders.remove(cylinder);

        if (cylinder.getJob() == this) {
            cylinder.setJob(null);
        }
    }

}
