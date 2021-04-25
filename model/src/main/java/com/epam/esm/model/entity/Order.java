package com.epam.esm.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Order extends Entity {
    private Certificate certificate;
    private User user;
    private BigDecimal amount;
    private int quantity;
    private LocalDateTime purchaseDate;

    public Order() {
    }

    public Order(Certificate certificate, User user, BigDecimal amount, int quantity, LocalDateTime purchaseDate) {
        this.certificate = certificate;
        this.user = user;
        this.amount = amount;
        this.quantity = quantity;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
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
        Order order = (Order) o;
        return quantity == order.quantity && Objects.equals(certificate, order.certificate) && Objects.equals(user, order.user) && Objects.equals(amount, order.amount) && Objects.equals(purchaseDate, order.purchaseDate);
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
