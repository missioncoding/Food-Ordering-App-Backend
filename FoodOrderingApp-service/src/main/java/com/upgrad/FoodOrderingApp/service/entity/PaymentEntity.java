package com.upgrad.FoodOrderingApp.service.entity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author zeelani
 * Entity representing payment table
 */

@NamedQueries({
        @NamedQuery(name = "payment.fetchByUuid",query = "SELECT p FROM PaymentEntity p WHERE p.uuid = :uuid"),
        @NamedQuery(name = "payment.fetchAll",query = "SELECT p FROM PaymentEntity p")
})

@Entity
@Table(name = "payment",uniqueConstraints = {@UniqueConstraint(columnNames = {"uuid"})})
public class PaymentEntity implements Serializable {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "uuid")
    @Size(max = 200)
    @NotNull
    private String uuid;

    @Column(name = "payment_name")
    @Size(max = 255)
    private String paymentName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }
}
