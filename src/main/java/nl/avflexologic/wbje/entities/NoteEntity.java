package nl.avflexologic.wbje.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "notes")
public class NoteEntity {

    @Id
    private Long jobId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "job_id")
    private JobEntity job;

    @Lob
    @Column(name = "info")
    private String info;

    protected NoteEntity() { }

    public NoteEntity(String info) {
        this.info = info;
    }

    public Long getJobId() { return jobId; }
    public JobEntity getJob() { return job; }
    public void setJob(JobEntity job) { this.job = job; }
    public String getInfo() { return info; }
    public void setInfo(String info) { this.info = info; }
}
