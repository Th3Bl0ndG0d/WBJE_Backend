package nl.avflexologic.wbje.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(
        name = "cylinders",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_cylinder_job_nr", columnNames = {"job_id", "cylinder_nr"})
        }
)
public class CylinderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cylinder_nr", nullable = false)
    private Integer cylinderNr;

    @Column(name = "color", length = 255)
    private String color;

    @Column(name = "cylinder_info", length = 255)
    private String cylinderInfo;

    /**
     * Owning side of Job -> Cylinder.
     * The foreign key is stored in this entity.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_id", nullable = false)
    private JobEntity job;

    /**
     * Cylinder uses exactly one TapeSpec.
     * Multiple cylinders may reuse the same TapeSpec.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tape_spec_id", nullable = false)
    private TapeSpecEntity tapeSpec;

    /**
     * Cylinder contains 0..* reports.
     */
    @OneToMany(mappedBy = "cylinder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReportEntity> reports = new ArrayList<>();

    protected CylinderEntity() {
        // Required by JPA.
    }

    /**
     * Creates a cylinder within a job using a tape specification.
     * job, tapeSpec and cylinderNr are mandatory.
     */
    public CylinderEntity(JobEntity job, TapeSpecEntity tapeSpec, Integer cylinderNr) {
        this.job = Objects.requireNonNull(job, "job is required.");
        this.tapeSpec = Objects.requireNonNull(tapeSpec, "tapeSpec is required.");
        this.cylinderNr = Objects.requireNonNull(cylinderNr, "cylinderNr is required.");
    }

    public Long getId() {
        return id;
    }

    public Integer getCylinderNr() {
        return cylinderNr;
    }

    public void setCylinderNr(Integer cylinderNr) {
        this.cylinderNr = Objects.requireNonNull(cylinderNr, "cylinderNr is required.");
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCylinderInfo() {
        return cylinderInfo;
    }

    public void setCylinderInfo(String cylinderInfo) {
        this.cylinderInfo = cylinderInfo;
    }

    public JobEntity getJob() {
        return job;
    }

    /** Managed by JobEntity to keep the relationship consistent. */
    void setJob(JobEntity job) {
        this.job = Objects.requireNonNull(job, "job is required.");
    }

    public TapeSpecEntity getTapeSpec() {
        return tapeSpec;
    }

    public void setTapeSpec(TapeSpecEntity tapeSpec) {
        this.tapeSpec = Objects.requireNonNull(tapeSpec, "tapeSpec is required.");
    }

    public List<ReportEntity> getReports() {
        return reports;
    }

    /** Keeps the 1..* relationship consistent. */
    public void addReport(ReportEntity report) {
        if (report == null) return;

        report.setCylinder(this);
        this.reports.add(report);
    }

    /** Orphan removal deletes the report when it is removed from the cylinder. */
    public void removeReport(ReportEntity report) {
        if (report == null) return;

        this.reports.remove(report);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CylinderEntity that = (CylinderEntity) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
