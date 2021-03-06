package com.upgrad.FoodOrderingApp.service.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author zeelani
 * class representing restaurant category table
 */


@NamedQueries({
    @NamedQuery(name = "restaurant_category.fetchCategoryByRestaurant",query = "SELECT r FROM RestaurantCategoryEntity r WHERE r.restaurant= :restaurant ORDER BY r.category.categoryName ASC "),
    @NamedQuery(name = "restaurant_category.fetchRestaurantByCategory",query = "SELECT r FROM RestaurantCategoryEntity r WHERE r.category = :category ORDER BY r.restaurant.customerRating DESC "),
})

@Entity
@Table(name = "restaurant_category")
public class RestaurantCategoryEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private RestaurantEntity restaurant;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private CategoryEntity category;

    // adding parameterized constructor for testing purpose
    public RestaurantCategoryEntity(@NotNull RestaurantEntity restaurant, @NotNull CategoryEntity category) {
        this.restaurant = restaurant;
        this.category = category;
    }

    public RestaurantCategoryEntity() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RestaurantEntity getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantEntity restaurant) {
        this.restaurant = restaurant;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }
}
