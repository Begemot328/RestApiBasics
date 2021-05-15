package com.epam.esm.model.entity;

import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Certificate class.
 *
 * @author Yury Zmushko
 * @version 2.0
 */

@Entity
public class Certificate extends CustomEntity {

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false, columnDefinition = "double")
    private BigDecimal price;

    @Column(nullable = false)
    private int duration;


    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @Column(name = "last_update_date", nullable = false)
    private LocalDateTime lastUpdateDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "certificate_tag",
            joinColumns = @JoinColumn(name = "certificate_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags = new ArrayList<>();

    /**
     * Default constructor.
     */
    public Certificate() {
    }

    /**
     * Constructor.
     *
     * @param name     Name of the certificate.
     * @param price    Price of the certificate.
     * @param duration Days to expiring of the certificate.
     */
    public Certificate(String name, BigDecimal price,
                       int duration) {
        this.name = name;
        this.price = price;
        this.duration = duration;
    }

    /**
     * Name getter.
     *
     * @return Name of the certificate.
     */
    public String getName() {
        return name;
    }

    /**
     * Name setter.
     *
     * @param name Name of the certificate
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Description getter.
     *
     * @return Description of the certificate.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Description setter.
     *
     * @param description Description of the certificate
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Price getter.
     *
     * @return Price of the certificate.
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Price setter.
     *
     * @param price Price of the certificate.
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * Duration getter.
     *
     * @return Duration of the certificate.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Duration setter.
     *
     * @param duration Duration of the certificate.
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Create date getter.
     *
     * @return Date of the certificate creation.
     */
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    /**
     * Create date setter.
     *
     * @param createDate Date of the certificate creation.
     */
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    /**
     * Last update date getter.
     *
     * @return Date of the certificate last update.
     */
    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    /**
     * Last update date setter.
     *
     * @param lastUpdateDate Date of the certificate last update.
     */
    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    /**
     * Tags collection getter.
     *
     * @return Tags of the certificate.
     */
    public List<Tag> getTags() {
        return tags;
    }

    /**
     * Tags collection setter.
     *
     * @param tags Tags of the certificate.
     */
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    /**
     * Equality check.
     *
     * @param o Object to check equality.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Certificate that = (Certificate) o;
        return duration == that.duration
                && Objects.equals(name, that.name)
                && Objects.equals(description, that.description)
                && Objects.equals(price, that.price)
                && Objects.equals(createDate, that.createDate)
                && Objects.equals(lastUpdateDate, that.lastUpdateDate)
                && CollectionUtils.isEqualCollection(tags, that.tags);
    }

    /**
     * Hach code calculator.
     *
     * @return Hash code value.
     */
    @Override
    public int hashCode() {
        int hashCode = Objects.hash(name, description, price, duration, createDate, lastUpdateDate);
        for (Tag tag : tags) {
            hashCode += tag.hashCode();
        }
        return hashCode;
    }

    /**
     * String value generator.
     *
     * @return {@link String} interpretation of object.
     */
    @Override
    public String toString() {
        return "Certificate{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", duration=" + duration +
                ", createDate=" + createDate +
                ", lastUpdateDate=" + lastUpdateDate +
                ", tags=" + tags +
                '}';
    }
}
