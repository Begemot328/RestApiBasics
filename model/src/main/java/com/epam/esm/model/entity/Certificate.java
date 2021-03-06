package com.epam.esm.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Certificate extends Entity {

    private String name;
    private String description;
    private BigDecimal price;
    private int duration;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate createDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate lastUpdateDate;

    /**
     * Default constructor
     */
    public Certificate() {
    }

    /**
     * Constructor
     *
     * @param name name of the certificate
     * @param description description of the certificate
     * @param price price of the certificate
     * @param duration days to expiring of the certificate
     * @param createDate creation date of the certificate
     * @param lastUpdateDate last update date of the certificate
     */
    public Certificate(String name, String description, BigDecimal price,
                       int duration, LocalDate createDate,
                       LocalDate lastUpdateDate) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
    }

    /**
     * Name getter
     *
     * @return name of the certificate
     */
    public String getName() {
        return name;
    }

    /**
     * Name setter
     *
     * @param  name name of the certificate
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Description getter
     *
     * @return description of the certificate
     */
    public String getDescription() {
        return description;
    }

    /**
     * Descritption setter
     *
     * @param  description name of the certificate
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Price getter
     *
     * @return price of the certificate
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Price setter
     *
     * @param price name of the certificate
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * Duration getter
     *
     * @return price of the certificate
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Duration setter
     *
     * @param duration duration of the certificate
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Create date getter
     *
     * @return  date of the certificate creation
     */
    public LocalDate getCreateDate() {
        return createDate;
    }

    /**
     * Create date setter
     *
     * @param createDate date of the certificate creation
     */
    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    /**
     * Last update date getter
     *
     * @return  date of the certificate last update
     */
    public LocalDate getLastUpdateDate() {
        return lastUpdateDate;
    }

    /**
     * Last update date setter
     *
     * @param lastUpdateDate date of the certificate last update
     */
    public void setLastUpdateDate(LocalDate lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Certificate)) return false;
        Certificate that = (Certificate) o;
        return that.price.equals(price) && duration == that.duration && name.equals(that.name) && Objects.equals(description, that.description) && createDate.equals(that.createDate) && lastUpdateDate.equals(that.lastUpdateDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, price, duration, createDate, lastUpdateDate);
    }

    @Override
    public String toString() {
        return "Certificate{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", duration=" + duration +
                ", createDate=" + createDate +
                ", lastUpdateDate=" + lastUpdateDate +
                ", id=" + id +
                '}';
    }
}
