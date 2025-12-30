package nl.avflexologic.wbje.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "report_specs")
public class ReportSpecEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "report_name", length = 255)
    private String reportName;

    @Column(name = "report_type", length = 255)
    private String reportType;

    @Column(name = "thickness")
    private Integer thickness;

    @Column(name = "info", length = 255)
    private String info;

    protected ReportSpecEntity() {
        // Required by JPA.
    }

    public Long getId() {
        return id;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public Integer getThickness() {
        return thickness;
    }

    public void setThickness(Integer thickness) {
        this.thickness = thickness;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
