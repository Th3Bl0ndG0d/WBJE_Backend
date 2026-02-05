package nl.avflexologic.wbje.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * JPA entity representing metadata of a job template that is stored on the filesystem.
 * <p>
 * The entity intentionally contains no binary file data. Only metadata is persisted, while the
 * physical template file is stored under the configured filesystem location. This approach keeps
 * the database footprint small and separates storage responsibilities.
 */
@Entity
@Table(name = "job_templates")
public class JobTemplateEntity {

    /**
     * Surrogate primary key for the job template record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Functional name of the template as provided by the client.
     * This name is intended for human-friendly selection and must be unique.
     */
    @Column(nullable = false, unique = true)
    private String templateName;

    /**
     * Physical file name on disk, typically a generated name (for example a UUID plus extension).
     * This value must be unique to avoid collisions in the filesystem.
     */
    @Column(nullable = false, unique = true)
    private String storedFileName;

    /**
     * Original file name provided by the client during upload. This value is returned to clients
     * when they inspect metadata or download the template.
     */
    @Column(nullable = false)
    private String originalFileName;

    /**
     * MIME type of the uploaded template file.
     */
    @Column(nullable = false)
    private String contentType;

    /**
     * Size of the uploaded file in bytes.
     */
    @Column(nullable = false)
    private long fileSize;

    /**
     * Timestamp indicating when the template was uploaded.
     */
    @Column(nullable = false)
    private LocalDateTime uploadedAt;

    /**
     * Default constructor required by JPA.
     */
    protected JobTemplateEntity() {
        // JPA only
    }

    /**
     * Convenience constructor for explicit instantiation.
     */
    public JobTemplateEntity(
            Long id,
            String templateName,
            String storedFileName,
            String originalFileName,
            String contentType,
            long fileSize,
            LocalDateTime uploadedAt
    ) {
        this.id = id;
        this.templateName = templateName;
        this.storedFileName = storedFileName;
        this.originalFileName = originalFileName;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.uploadedAt = uploadedAt;
    }

    public Long getId() {
        return id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public String getStoredFileName() {
        return storedFileName;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public String getContentType() {
        return contentType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public void setStoredFileName(String storedFileName) {
        this.storedFileName = storedFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobTemplateEntity that = (JobTemplateEntity) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }



}
