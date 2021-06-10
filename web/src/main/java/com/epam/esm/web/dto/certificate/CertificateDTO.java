package com.epam.esm.web.dto.certificate;

import com.epam.esm.persistence.model.entity.Certificate;
import com.epam.esm.persistence.model.entity.User;
import com.epam.esm.web.dto.tag.TagDTO;
import com.epam.esm.web.dto.user.UserDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link Certificate} DTO class.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public class CertificateDTO extends RepresentationModel<CertificateDTO> {
    private int id;
    private String name;
    private String description;
    private BigDecimal price;
    private int duration;
    private List<TagDTO> tags = new ArrayList<>();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime lastUpdateDate;

    /**
     * Constructor.
     *
     * @param name Name of the Certificate.
     * @param description Description of the Certificate.
     * @param price Price of the Certificate.
     * @param duration Duration of the Certificate in days.
     * @param createDate Create date of the Certificate.
     * @param lastUpdateDate Last update date of the Certificate.
     */
    public CertificateDTO(String name, String description, BigDecimal price,
                          int duration, LocalDateTime createDate,
                          LocalDateTime lastUpdateDate) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
    }

    /**
     * Default constructor.
     */
    public CertificateDTO() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public List<TagDTO> getTags() {
        return tags;
    }

    public void setTags(List<TagDTO> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CertificateDTO that = (CertificateDTO) o;

        return new EqualsBuilder().appendSuper(super.equals(o))
                .append(id, that.id).append(duration, that.duration)
                .append(name, that.name).append(description, that.description)
                .append(price, that.price).append(tags, that.tags).append(createDate, that.createDate)
                .append(lastUpdateDate, that.lastUpdateDate).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode()).append(id).append(name)
                .append(description).append(price).append(duration).append(tags)
                .append(createDate).append(lastUpdateDate).toHashCode();
    }

    @Override
    public String toString() {
        return "CertificateDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", duration=" + duration +
                ", tags=" + tags +
                ", createDate=" + createDate +
                ", lastUpdateDate=" + lastUpdateDate +
                '}';
    }
}
