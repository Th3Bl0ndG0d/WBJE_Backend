package nl.avflexologic.wbje.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(
        name = "reports",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_report_cylinder_nr",
                        columnNames = {"cylinder_id", "report_nr"})
        }
)
public class ReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "report_nr", nullable = false)
    private Integer reportNr;

    @Column(name = "report_width")
    private Integer reportWidth;

    @Column(name = "x_offset")
    private Integer xOffset;

    @Column(name = "y_offset")
    private Integer yOffset;

    /**
     * Owning side of Cylinder -> Report.
     * The foreign key is stored in this entity.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cylinder_id", nullable = false)
    private CylinderEntity cylinder;

    /**
     * Report uses exactly one ReportSpec.
     * Multiple reports may reuse the same ReportSpec.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "report_spec_id", nullable = false)
    private ReportSpecEntity reportSpec;

    protected ReportEntity() {
        // Required by JPA.
    }

    /**
     * Creates a report belonging to a cylinder using a report specification.
     * cylinder, reportSpec and reportNr are mandatory.
     */
    public ReportEntity(CylinderEntity cylinder, ReportSpecEntity reportSpec, Integer reportNr) {
        this.cylinder = Objects.requireNonNull(cylinder, "cylinder is required.");
        this.reportSpec = Objects.requireNonNull(reportSpec, "reportSpec is required.");
        this.reportNr = Objects.requireNonNull(reportNr, "reportNr is required.");
    }

    public Long getId() {
        return id;
    }

    public Integer getReportNr() {
        return reportNr;
    }

    public void setReportNr(Integer reportNr) {
        this.reportNr = Objects.requireNonNull(reportNr, "reportNr is required.");
    }

    public Integer getReportWidth() {
        return reportWidth;
    }

    public void setReportWidth(Integer reportWidth) {
        this.reportWidth = reportWidth;
    }

    public Integer getXOffset() {
        return xOffset;
    }

    public void setXOffset(Integer xOffset) {
        this.xOffset = xOffset;
    }

    public Integer getYOffset() {
        return yOffset;
    }

    public void setYOffset(Integer yOffset) {
        this.yOffset = yOffset;
    }

    public CylinderEntity getCylinder() {
        return cylinder;
    }

    /** Managed by CylinderEntity to keep the relationship consistent. */
    void setCylinder(CylinderEntity cylinder) {
        this.cylinder = Objects.requireNonNull(cylinder, "cylinder is required.");
    }

    public ReportSpecEntity getReportSpec() {
        return reportSpec;
    }

    public void setReportSpec(ReportSpecEntity reportSpec) {
        this.reportSpec = Objects.requireNonNull(reportSpec, "reportSpec is required.");
    }
}
