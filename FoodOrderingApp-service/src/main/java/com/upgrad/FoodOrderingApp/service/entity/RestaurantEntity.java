package com.upgrad.FoodOrderingApp.service.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author zeelani
 * class representing restaurant table entity
 */

@NamedQueries({
    @NamedQuery(name = "restaurant.fetchByRating",query = "SELECT r FROM RestaurantEntity r ORDER BY r.customerRating DESC"),
    @NamedQuery(name = "restaurant.fetchByUuid",query = "SELECT r FROM RestaurantEntity r WHERE r.uuid = :uuid"),
    @NamedQuery(name = "restaurant.fetchByName",query = "SELECT r FROM  RestaurantEntity r WHERE LOWER(r.restaurantName) LIKE :restaurant_name_low"),
})

@Entity
@Table(name = "restaurant",uniqueConstraints = {@UniqueConstraint(columnNames = {"uuid"})})
public class RestaurantEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid")
    @Size(max = 200)
    @NotNull
    private String uuid;

    @Column(name = "restaurant_name")
    @Size(max = 50)
    @NotNull
    private String restaurantName;

    @Column(name = "photo_url")
    @Size(max = 255)
    private String photoUrl;

    @Column(name = "customer_rating")
    @NotNull
    private double customerRating;

    @Column(name = "average_price_for_two")
    @NotNull
    private Integer avgPrice;

    @Column(name = "number_of_customers_rated")
    @NotNull
    private Integer numberCustomersRated;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AddressEntity address;

    // adding parameterized constructor for testing purpose
    public RestaurantEntity(@Size(max = 200) @NotNull String uuid, @Size(max = 50) @NotNull String restaurantName, @Size(max = 255) String photoUrl, @NotNull double customerRating, @NotNull Integer avgPrice, @NotNull Integer numberCustomersRated, AddressEntity address) {
        this.uuid = uuid;
        this.restaurantName = restaurantName;
        this.photoUrl = photoUrl;
        this.customerRating = customerRating;
        this.avgPrice = avgPrice;
        this.numberCustomersRated = numberCustomersRated;
        this.address = address;
    }

    public RestaurantEntity() {}

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

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public double getCustomerRating() {
        return customerRating;
    }

    public void setCustomerRating(double customerRating) {
        this.customerRating = customerRating;
    }

    public Integer getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(Integer avgPrice) {
        this.avgPrice = avgPrice;
    }

    public Integer getNumberCustomersRated() {
        return numberCustomersRated;
    }

    public void setNumberCustomersRated(Integer numberCustomersRated) {
        this.numberCustomersRated = numberCustomersRated;
    }

    public AddressEntity getAddress() {
        return address;
    }

    public void setAddress(AddressEntity address) {
        this.address = address;
    }

}
