package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author zeelani
 * Repository class handling Restaurant category db operations
 */

@Repository
public class RestaurantCategoryDao {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Fetch restaurant based on category
     * @param categoryEntity
     * @return
     */
    public List<RestaurantCategoryEntity> fetchRestaurantByCategory(CategoryEntity categoryEntity) {
        try {
            List<RestaurantCategoryEntity> restaurantCategoryEntities = entityManager.createNamedQuery("restaurant_category.fetchRestaurantByCategory",RestaurantCategoryEntity.class).setParameter("category",categoryEntity).getResultList();
            return restaurantCategoryEntities;
        }catch (NoResultException nre){
            return null;
        }
    }

    /**
     * Fetch categories based on restaurant
     * @param restaurantEntity
     * @return
     */
    public List<RestaurantCategoryEntity> fetchCategoriesByRestaurant(RestaurantEntity restaurantEntity){
        try {
            List<RestaurantCategoryEntity> restaurantCategoryEntity = entityManager.createNamedQuery("restaurant_category.fetchCategoryByRestaurant",RestaurantCategoryEntity.class).
                    setParameter("restaurant",restaurantEntity).getResultList();
            return restaurantCategoryEntity;
        }catch (NoResultException nre){
            return null;
        }

    }
}
