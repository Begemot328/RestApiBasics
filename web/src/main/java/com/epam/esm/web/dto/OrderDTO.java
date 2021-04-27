package com.epam.esm.web.dto;

import com.epam.esm.model.entity.Entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class OrderDTO extends Entity {
    private CertificateDTO certificate;
    private UserDTO user;
    private BigDecimal orderAmount;
    private int certificateQuantity;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime purchaseDate;

    public OrderDTO(CertificateDTO certificate, UserDTO user, BigDecimal orderAmount,
                    int certificateQuantity, LocalDateTime purchaseDate) {
        this.certificate = certificate;
        this.user = user;
        this.orderAmount = orderAmount;
        this.certificateQuantity = certificateQuantity;
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

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public int getCertificateQuantity() {
        return certificateQuantity;
    }

    public void setCertificateQuantity(int certificateQuantity) {
        this.certificateQuantity = certificateQuantity;
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
        return orderAmount.compareTo(order.orderAmount) == 0
                && certificateQuantity == order.certificateQuantity
                && certificate.equals(order.certificate) && user.equals(order.user)
                && purchaseDate.equals(order.purchaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(certificate, user, orderAmount, certificateQuantity, purchaseDate);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", certificate=" + certificate +
                ", user=" + user +
                ", amount=" + orderAmount +
                ", quantity=" + certificateQuantity +
                ", purchaseDate=" + purchaseDate +
                '}';
    }
}
