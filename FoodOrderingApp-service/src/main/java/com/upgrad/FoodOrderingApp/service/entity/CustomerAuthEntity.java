package com.upgrad.FoodOrderingApp.service.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * @author zeelani
 * Entity class representing the customer auth table
 */

@NamedQueries({
    @NamedQuery(name = "customer_auth.fetchByAccessToken", query = "SELECT c from CustomerAuthEntity c where c.accessToken = :access_Token"),
})

@Entity
@Table(name = "customer_auth",uniqueConstraints = {@UniqueConstraint(columnNames = {"uuid"})})
public class CustomerAuthEntity implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid")
    @Size (max = 200)
    @NotNull
    private String uuid;

    @ManyToOne()
    @JoinColumn(name = "customer_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CustomerEntity customer;

    @Column(name = "access_token")
    @Size(max = 500)
    private String accessToken;


    @Column(name = "login_at")
    private ZonedDateTime loginAt;

    @Column(name = "logout_at")
    private ZonedDateTime logoutAt;

    @Column(name = "expires_at")
    private ZonedDateTime expiresAt;

    // adding parameterized constuctor for testing purpose
    public CustomerAuthEntity(@Size(max = 200) @NotNull String uuid, CustomerEntity customer, @Size(max = 500) String accessToken, ZonedDateTime loginAt, ZonedDateTime logoutAt, ZonedDateTime expiresAt) {
        this.uuid = uuid;
        this.customer = customer;
        this.accessToken = accessToken;
        this.loginAt = loginAt;
        this.logoutAt = logoutAt;
        this.expiresAt = expiresAt;
    }

    public CustomerAuthEntity() {}

    public Integer getId() {
        return this.id;
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

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public ZonedDateTime getLoginAt() {
        return loginAt;
    }

    public void setLoginAt(ZonedDateTime loginAt) {
        this.loginAt = loginAt;
    }

    public ZonedDateTime getLogoutAt() {
        return logoutAt;
    }

    public void setLogoutAt(ZonedDateTime logoutAt) {
        this.logoutAt = logoutAt;
    }

    public ZonedDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(ZonedDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
