package com.epam.esm.web.dto.tag;

import com.epam.esm.persistence.model.entity.Certificate;
import com.epam.esm.persistence.model.entity.Tag;
import com.epam.esm.web.dto.certificate.CertificateDTO;
import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

/**
 * {@link Tag} DTO class.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public class TagDTO extends RepresentationModel<CertificateDTO> {
    private int id;
    private String name;

    /**
     * Constructor.
     *
     * @param name Name of the Tag.
     * @param id ID of the Tag.
     */
    public TagDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Default constructor.
     */
    public TagDTO() {
        // Default constructor for Model mapper purposes.
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagDTO tagDTO = (TagDTO) o;
        return id == tagDTO.id && Objects.equals(name, tagDTO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "TagDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
