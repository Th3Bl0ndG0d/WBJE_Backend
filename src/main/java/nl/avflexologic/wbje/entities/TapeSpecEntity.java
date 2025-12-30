package nl.avflexologic.wbje.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "tape_specs")
public class TapeSpecEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tape_name", length = 255)
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
}
