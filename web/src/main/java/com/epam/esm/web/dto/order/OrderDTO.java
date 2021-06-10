package com.epam.esm.web.dto.order;

import com.epam.esm.persistence.model.entity.Certificate;
import com.epam.esm.web.dto.certificate.CertificateDTO;
import com.epam.esm.web.dto.user.UserDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * {@link com.epam.esm.persistence.model.entity.Order} DTO class.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
public class OrderDTO extends RepresentationModel<OrderDTO> {
    private int id;
    private CertificateDTO certificate;
    private UserDTO user;
    private BigDecimal orderAmount;
    private int certificateQuantity;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime purchaseDate;

    /**
     * Constructor.
     *
     * @param certificate {@link CertificateDTO} of the order.
     * @param user {@link UserDTO} of the order.
     * @param orderAmount Money amount of the order.
     * @param certificateQuantity Quantity of certificates.
     * @param purchaseDate Purchase date of the order.
     */
    public OrderDTO(CertificateDTO certificate, UserDTO user, BigDecimal orderAmount,
                    int certificateQuantity, LocalDateTime purchaseDate) {
        this.certificate = certificate;
        this.user = user;
        this.orderAmount = orderAmount;
        this.certificateQuantity = certificateQuantity;
        this.purchaseDate = purchaseDate;
    }

    /**
     * Default constructor.
     */
    public OrderDTO() {
        // Default constructor for Model mapper purposes.
    }

    public CertificateDTO getCertificate() {
        return certificate;
    }

    public void setCertificate(CertificateDTO certificate) {
        this.certificate = certificate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
