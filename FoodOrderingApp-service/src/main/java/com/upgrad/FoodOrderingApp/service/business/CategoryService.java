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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    @PersistenceContext
    EntityManager entityManager;

    /* get Categories of Restaurant through restaurant UUID */

    /* get categories tagged to Restaurant */
    public List<CategoryEntity> getCategoriesByRestaurant(String restaurantId) {
        //Calls getRestaurantByUuid of restaurantDao to get RestaurantEntity
        RestaurantEntity restaurantEntity = restaurantDao.getRestaurantByUuid(restaurantId);

        //Calls getCategoriesByRestaurant of restaurantCategoryDao to get list of RestaurantCategoryEntity
        List<RestaurantCategoryEntity> restaurantCategoryEntities = restaurantCategoryDao.getRestaurantCategories(restaurantEntity);

        //Creating the list of the Category entity to be returned.
        List<CategoryEntity> categoryEntities = new LinkedList<>();
        restaurantCategoryEntities.forEach(restaurantCategoryEntity -> {
            categoryEntities.add(restaurantCategoryEntity.getCategory());
        });
        return categoryEntities;
    }

    public CategoryEntity getCategoryById(String uuid) throws CategoryNotFoundException {
        CategoryEntity categoryEntity = categoryDao.fetchByUuid(uuid);
        if(uuid == "") {
            throw new CategoryNotFoundException("CNF-001","Category id field should not be empty");
        } else if(categoryEntity == null) {
            throw new CategoryNotFoundException("CNF-002","No category by this id");
        } else {
            return categoryEntity;
        }
    }


}
