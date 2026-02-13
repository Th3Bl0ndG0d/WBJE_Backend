package nl.avflexologic.wbje.entities;

import jakarta.persistence.*;

@Entity
@Table(
        name = "tape_specs",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_tape_specs_tape_name", columnNames = "tape_name")
        }
)
public class TapeSpecEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tape_name", length = 255, nullable = false)
    private String tapeName;

    @Column(name = "tape_type", length = 255)
    private String tapeType;

    @Column(name = "thickness")
    private Integer thickness;

    @Column(name = "info", length = 255)
    private String info;

    public TapeSpecEntity() {
        // Required by JPA.
    }

    public Long getId() {
        return id;
    }

    public String getTapeName() {
        return tapeName;
    }

    public void setTapeName(String tapeName) {
        this.tapeName = tapeName;
    }

    public String getTapeType() {
        return tapeType;
    }

    public void setTapeType(String tapeType) {
        this.tapeType = tapeType;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TapeSpecEntity that = (TapeSpecEntity) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
