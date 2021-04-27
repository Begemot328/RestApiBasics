package com.epam.esm.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Order extends Entity {
    private Certificate certificate;
    private User user;
    private BigDecimal orderAmount;
    private int certificateQuantity;
    private LocalDateTime purchaseDate;

    public Order() {
    }

    public Order(Certificate certificate, User user, BigDecimal orderAmount, int certificateQuantity,
                 LocalDateTime purchaseDate) {
        this.certificate = certificate;
        this.user = user;
        this.orderAmount = orderAmount;
        this.certificateQuantity = certificateQuantity;
        this.purchaseDate = purchaseDate;
    }

    public Certificate getCertificate() {
        return certificate;
    }

    public void setCertificate(Certificate certificate) {
        this.certificate = certificate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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
        Order order = (Order) o;
        return certificateQuantity == order.certificateQuantity && Objects.equals(certificate, order.certificate)
                && Objects.equals(user, order.user) && Objects.equals(orderAmount, order.orderAmount)
                && Objects.equals(purchaseDate, order.purchaseDate);
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
