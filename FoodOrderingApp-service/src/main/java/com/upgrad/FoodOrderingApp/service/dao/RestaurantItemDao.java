package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantItemEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author zeelani
 * Repository for handling Restaurant item DB operations
 */

@Repository
public class RestaurantItemDao {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Fetch all items in the restaurant
     * @param restaurantEntity
     * @return
     */
    public List<RestaurantItemEntity> getItemsByRestaurant(RestaurantEntity restaurantEntity) {
        try {
            List<RestaurantItemEntity> restaurantItemEntities = entityManager.createNamedQuery("restaurant_item.fetchAllItems",RestaurantItemEntity.class).setParameter("restaurant",restaurantEntity).getResultList();
            return restaurantItemEntities;
        }catch (NoResultException nre){
            return null;
        }
    }
}
