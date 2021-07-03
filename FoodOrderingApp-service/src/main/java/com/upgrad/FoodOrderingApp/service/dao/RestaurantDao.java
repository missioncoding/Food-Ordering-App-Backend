package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

@Repository
public class RestaurantDao {
    @PersistenceContext
    private EntityManager entityManager;

    public  List<RestaurantEntity> getAllRestaurants() {
        return entityManager.createNamedQuery("fetchAll",RestaurantEntity.class).getResultList();
    }

    public  List<RestaurantEntity> getRestaurant(String restaurantName) {
        return entityManager.createNamedQuery("fetchRestaurant", RestaurantEntity.class).setParameter("restaurantName", restaurantName).getResultList();
    }

    public RestaurantEntity getRestaurantById(String uuid) {
        try {
            RestaurantEntity restaurantEntity = entityManager.createNamedQuery("restByUuid",RestaurantEntity.class).setParameter("uuid",uuid).getSingleResult();
            return restaurantEntity;
        }catch (NoResultException nre){
            return null;
        }

    }

}