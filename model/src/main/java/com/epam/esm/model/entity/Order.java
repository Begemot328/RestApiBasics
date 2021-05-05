package com.epam.esm.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Order class.
 *
 * @author Yury Zmushko
 * @version 2.0
 */
@Entity
@Table(name = "orders")
public class Order extends CustomEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "certificate_id", nullable = false)
    private Certificate certificate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "amount", nullable = false)
    private BigDecimal orderAmount;

    @Column(name = "quantity", nullable = false)
    private int certificateQuantity;

    @Column(name = "purchase_date", nullable = false)
    private LocalDateTime purchaseDate;

    /**
     * Default constructor.
     */
    public Order() {
    }

    /**
     * Constructor.
     *
     * @param certificate         Certificate of the order.
     * @param orderAmount         Whole amount of money of this order.
     * @param certificateQuantity Quantity of the certificates.
     * @param purchaseDate        Date when order was made.
     */
    public Order(Certificate certificate, User user, BigDecimal orderAmount, int certificateQuantity,
                 LocalDateTime purchaseDate) {
        this.certificate = certificate;
        this.user = user;
        this.orderAmount = orderAmount;
        this.certificateQuantity = certificateQuantity;
        this.purchaseDate = purchaseDate;
    }

    /**
     * Certificate getter.
     *
     * @return Certificate of the order.
     */
    public Certificate getCertificate() {
        return certificate;
    }

    /**
     * Certificate setter.
     *
     * @param certificate Certificate of the order.
     */
    public void setCertificate(Certificate certificate) {
        this.certificate = certificate;
    }

    /**
     * User getter.
     *
     * @return User of the order.
     */
    public User getUser() {
        return user;
    }

    /**
     * User setter.
     *
     * @param user User of the order.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Order amount getter.
     *
     * @return Amount of the order.
     */
    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    /**
     * Order amount setter.
     *
     * @param orderAmount Amount of the order.
     */
    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    /**
     * Certificate quantity getter.
     *
     * @return Order amount of the order.
     */
    public int getCertificateQuantity() {
        return certificateQuantity;
    }

    /**
     * Certificate quantity setter.
     *
     * @param certificateQuantity Amount of the order.
     */
    public void setCertificateQuantity(int certificateQuantity) {
        this.certificateQuantity = certificateQuantity;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    /**
     * Date of the order setter.
     *
     * @param purchaseDate Date of the order.
     */
    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
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
        Order order = (Order) o;
        return certificateQuantity == order.certificateQuantity
                && Objects.equals(certificate, order.certificate)
                && Objects.equals(user, order.user)
                && Objects.equals(orderAmount, order.orderAmount)
                && Objects.equals(purchaseDate, order.purchaseDate);
    }

    /**
     * Hach code calculator.
     *
     * @return Hash code value.
     */
    @Override
    public int hashCode() {
        int hashCode = Objects.hash(orderAmount, certificateQuantity, purchaseDate);
        hashCode += user.hashCode();
        hashCode += certificate.hashCode();
        return hashCode;
    }

    /**
     * String value generator.
     *
     * @return {@link String} interpretation of object.
     */
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
