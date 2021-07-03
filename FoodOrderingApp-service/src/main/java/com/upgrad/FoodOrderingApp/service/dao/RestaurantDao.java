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
}