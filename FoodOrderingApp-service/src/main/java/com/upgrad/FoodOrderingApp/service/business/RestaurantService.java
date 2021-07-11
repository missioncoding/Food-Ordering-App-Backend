package com.upgrad.FoodOrderingApp.service.business;

import com.upgrad.FoodOrderingApp.service.common.ApplicationUtil;
import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantCategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * @author zeelani
 * Service class to handle the restaurant related business logic
 */

@Service
public class RestaurantService {

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    RestaurantDao restaurantDao;

    @Autowired
    ApplicationUtil applicationUtil;

    @Autowired
    RestaurantCategoryDao restaurantCategoryDao;

    /**
     * Method to get the restaurants by rating
     * @return list of restaurant entities
     */
    public List<RestaurantEntity> restaurantsByRating(){
        List<RestaurantEntity> restaurantEntities = restaurantDao.fetchByRating();
        return restaurantEntities;
    }

    /**
     * Method to get restaurants By Name and returns list of RestaurantEntity
     * @param restaurantName
     * @return list of restaurant entities
     * @throws RestaurantNotFoundException
     */
    public List<RestaurantEntity> restaurantsByName(String restaurantName)throws RestaurantNotFoundException{
        if(restaurantName == null || restaurantName ==""){
            throw new RestaurantNotFoundException("RNF-003","Restaurant name field should not be empty");
        }
        List<RestaurantEntity> restaurantEntities = restaurantDao.fetchByName(restaurantName);
        return restaurantEntities;
    }


    /**
     * Method to get the restaurant by category
     * @param categoryId
     * @return list of restaurant entities
     * @throws CategoryNotFoundException
     */
    public List<RestaurantEntity> restaurantByCategory(String categoryId) throws CategoryNotFoundException {
        // check whether the category id is null
        if(categoryId == null || categoryId == ""){
            throw new CategoryNotFoundException("CNF-001","Category id field should not be empty");
        }
        // get the category entity by the id
        CategoryEntity categoryEntity = categoryDao.fetchByUuid(categoryId);
        if(categoryEntity == null){
            throw new CategoryNotFoundException("CNF-002","No category by this id");
        }
        // get all the list of restaurants using the above category
        List<RestaurantCategoryEntity> restaurantCategoryEntities = restaurantCategoryDao.fetchRestaurantByCategory(categoryEntity);

        //Creating new restaurantEntity List and add only the restaurant for the corresponding category.
        List<RestaurantEntity> restaurantEntities = new LinkedList<>();
        restaurantCategoryEntities.forEach(restaurantCategoryEntity -> {
            restaurantEntities.add(restaurantCategoryEntity.getRestaurant());
        });
        return restaurantEntities;
    }


    /**
     * Method to get restaurant By UUID and returns RestaurantEntity
     * @param restaurantUuid
     * @return
     * @throws RestaurantNotFoundException
     */
    public RestaurantEntity restaurantByUUID(String restaurantUuid)throws RestaurantNotFoundException{
        // Check whether the restaurant id is null
        if(restaurantUuid == null||restaurantUuid == "") {
            throw new RestaurantNotFoundException("RNF-002","Restaurant id field should not be empty");
        }
        // get the restaurant entity using the id
        RestaurantEntity restaurantEntity = restaurantDao.fetchByUuid(restaurantUuid);
        if (restaurantEntity == null){
            throw new RestaurantNotFoundException("RNF-001","No restaurant by this id");
        }
        return restaurantEntity;
    }

    /**
     * Method to update the restaurant rating
     * @param restaurantEntity
     * @param customerRating
     * @return
     * @throws InvalidRatingException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public RestaurantEntity updateRestaurantRating(RestaurantEntity restaurantEntity, Double customerRating) throws InvalidRatingException {
        // validate the rating
        if(!applicationUtil.validateCustomerRating(customerRating.toString())){
            throw new InvalidRatingException("IRE-001","Restaurant should be in the range of 1 to 5");
        }
        // calculating the new rating
        DecimalFormat format = new DecimalFormat("##.0");
        double curr_restaurantRating = restaurantEntity.getCustomerRating();
        Integer totalcustomersrated = restaurantEntity.getNumberCustomersRated();

        //calculating the weighted average of the entire rating
        // formula ((current_rating*no.of customers) + 1 * currentcustomerrating) / 1+ no.of customers
        double newRating = ((curr_restaurantRating * totalcustomersrated) + customerRating) / (1 + totalcustomersrated);
        restaurantEntity.setNumberCustomersRated(totalcustomersrated+1);
        restaurantEntity.setCustomerRating(Double.parseDouble(format.format(newRating)));

        // merging the resturant entity with current entity in db
        RestaurantEntity updatedRestaurantEntity = restaurantDao.updateRating(restaurantEntity);

        return updatedRestaurantEntity;

    }
}

