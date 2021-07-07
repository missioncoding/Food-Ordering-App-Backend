package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RestaurantDao {

    @PersistenceContext
    private EntityManager entityManager;

<<<<<<< HEAD
    /**
     * Fetch restaurant using name
     * @param restaurantName
     * @return
     */
    public List<RestaurantEntity> fetchByName(String restaurantName) {
        try {
            String restaurantNameLow = "%"+restaurantName.toLowerCase()+"%"; // to make a check with lower
            List<RestaurantEntity> restaurantEntities = entityManager.createNamedQuery("restaurant.fetchByName", RestaurantEntity.class).setParameter("restaurant_name_low",restaurantNameLow).getResultList();
            return restaurantEntities;
        }catch (NoResultException nre){
            return null;
        }

    }

    /**
     * Update the restaurant
     * @param restaurantEntity
     * @return
     */
    public RestaurantEntity updateRating(RestaurantEntity restaurantEntity) {
        entityManager.merge(restaurantEntity);
        return restaurantEntity;
    }

    /**
     * Fetch all restaurants by rating
     * @return
     */
    public List<RestaurantEntity> fetchByRating(){
        try{
            List<RestaurantEntity> restaurantEntities = entityManager.createNamedQuery("restaurant.fetchByRating",RestaurantEntity.class).getResultList();
            return restaurantEntities;
        }catch (NoResultException nre){
            return null;
        }
    }

    /**
     * Fetch restaurant using UUID
     * @param uuid
     * @return
     */
    public RestaurantEntity fetchByUuid(String uuid) {
=======
    public  List<RestaurantEntity> getAllRestaurants() {
        return entityManager.createNamedQuery("fetchAll",RestaurantEntity.class).getResultList();
    }

    public  List<RestaurantEntity> getRestaurantByName(String restaurantName) {
        return entityManager.createNamedQuery("fetchRestaurant", RestaurantEntity.class).setParameter("restaurantName", restaurantName).getResultList();
    }

    public RestaurantEntity getRestaurantByUuid(String uuid) {
>>>>>>> 593795181ed6dd7e05627612f643ea78447c27c0
        try {
            RestaurantEntity restaurantEntity = entityManager.createNamedQuery("restByUuid",RestaurantEntity.class).setParameter("uuid",uuid).getSingleResult();
            return restaurantEntity;
        }catch (NoResultException nre){
            return null;
        }

    }

}