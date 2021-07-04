package com.upgrad.FoodOrderingApp.service.businness;
import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantCategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/* Category Service for Category Entitites */
@Service
public class CategoryService {

    /* Auto wiring restaurantDao, resrestaurantCategoryDao, category DAO */
    @Autowired
    RestaurantDao restaurantDao;

    @Autowired
    RestaurantCategoryDao restaurantCategoryDao;

    @Autowired
    CategoryDao categoryDao;

    /* get Categories of Restaurant through restaurant UUID */

    public List<CategoryEntity> getCategoriesByRestaurant(String restaurantUuid) {

        RestaurantEntity restaurantEntity = restaurantDao.getRestaurantById(restaurantUuid);

        /* Getting Categories through Restaurant Entity and returning categories attached to Restaurant Entity */
        List<RestaurantCategoryEntity> restaurantCategoryEntities = restaurantCategoryDao.getRestaurantCategories(restaurantEntity);

        List<CategoryEntity> categoryEntities = new LinkedList<>();
        restaurantCategoryEntities.forEach(restaurantCategoryEntity -> {
            categoryEntities.add(restaurantCategoryEntity.getCategory());
        });
        return categoryEntities;
    }
}