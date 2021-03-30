package com.epam.esm.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Certificate extends Entity{

    private String name;
    private String description;
    private BigDecimal price;
    private int duration;
    private LocalDate createDate;
    private LocalDate lastUpdateDate;

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

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDate lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
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
