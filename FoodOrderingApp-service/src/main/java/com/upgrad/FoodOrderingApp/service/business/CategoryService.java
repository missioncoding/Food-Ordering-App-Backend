package com.upgrad.FoodOrderingApp.service.business;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantCategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * @author zeelani
 * service class handling business logic for category
 */

@Service
public class CategoryService {

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    RestaurantDao restaurantDao;

    @Autowired
    RestaurantCategoryDao restaurantCategoryDao;

    /**
     * Method to fetch all the categories of a restaurant
     * @param restaurantUuid
     * @return
     */
    public List<CategoryEntity> fetchCategoriesByRestaurant(String restaurantUuid){
        // First get the restaurant entity
        RestaurantEntity restaurantEntity = restaurantDao.getRestaurantByUuid(restaurantUuid);
        // lookup for all categories
        List<RestaurantCategoryEntity> restaurantCategoryEntities = restaurantCategoryDao.fetchCategoriesByRestaurant(restaurantEntity);

        //Creating the list of the Category entity to be returned.
        List<CategoryEntity> categoryEntities = new LinkedList<>();
        restaurantCategoryEntities.forEach(restaurantCategoryEntity -> {
            categoryEntities.add(restaurantCategoryEntity.getCategory());
        });
        return categoryEntities;
    }

    /**
     * Fetch all categories. The list is ordered by name
     * @return
     */
    public List<CategoryEntity> fetchAllCategories() {
        List<CategoryEntity> categoryEntities = categoryDao.fetchAll();
        return categoryEntities;
    }

    /**
     * fetch the category by UUID
     * @param categoryUuid
     * @return
     * @throws CategoryNotFoundException
     */
    public CategoryEntity fetchCategoryById(String categoryUuid) throws CategoryNotFoundException {
        //check for null or empty category
        if(categoryUuid == null || categoryUuid == ""){
            throw new CategoryNotFoundException("CNF-001","Category id field should not be empty");
        }
        // fetching the data from DB
        CategoryEntity categoryEntity = categoryDao.fetchByUuid(categoryUuid);
        if(categoryEntity == null){
            throw new CategoryNotFoundException("CNF-002","No category by this id");
        }
        return categoryEntity;
    }
}
