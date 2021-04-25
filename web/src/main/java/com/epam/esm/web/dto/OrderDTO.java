package com.epam.esm.web.dto;

import com.epam.esm.model.entity.Entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.util.Objects;

public class OrderDTO extends Entity {
    private CertificateDTO certificate;
    private UserDTO user;
    private double amount;
    private int quantity;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime purchaseDate;

    public OrderDTO(CertificateDTO certificate, UserDTO user, double amount, int quantity, LocalDateTime purchaseDate) {
        this.certificate = certificate;
        this.user = user;
        this.amount = amount;
        this.quantity = quantity;
        this.purchaseDate = purchaseDate;
    }

    public OrderDTO() {
    }

    public CertificateDTO getCertificate() {
        return certificate;
    }

    public void setCertificate(CertificateDTO certificate) {
        this.certificate = certificate;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDTO order = (OrderDTO) o;
        return Double.compare(order.amount, amount) == 0 && quantity == order.quantity
                && certificate.equals(order.certificate) && user.equals(order.user)
                && purchaseDate.equals(order.purchaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(certificate, user, amount, quantity, purchaseDate);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", certificate=" + certificate +
                ", user=" + user +
                ", amount=" + amount +
                ", quantity=" + quantity +
                ", purchaseDate=" + purchaseDate +
                '}';
    }
}
