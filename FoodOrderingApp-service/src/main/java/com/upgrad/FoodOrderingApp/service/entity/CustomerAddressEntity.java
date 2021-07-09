package com.upgrad.FoodOrderingApp.service.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author zeelani
 * Entity class representing the customer address table
 */

@NamedQueries({
    @NamedQuery(name = "customer_address.fetchByCustomer", query = "SELECT c from CustomerAddressEntity c where c.customer = :customer_entity AND c.address.active = :active"),
    @NamedQuery(name = "customer_address.fetchByAddress", query = "SELECT c from CustomerAddressEntity c where c.address = :address_entity")
})

@Entity
@Table(name = "customer_address")
public class CustomerAddressEntity implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CustomerEntity customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private AddressEntity address;

    // adding parameterized constructor for testing purpose
    public CustomerAddressEntity(CustomerEntity customer, AddressEntity address) {
        this.customer = customer;
        this.address = address;
    }

    public CustomerAddressEntity() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public AddressEntity getAddress() {
        return address;
    }

    public void setAddress(AddressEntity address) {
        this.address = address;
    }
}
