package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantDao restaurantDao;

    @Transactional
    public List<RestaurantEntity> getAllRestaurant() {
        return  restaurantDao.getAllRestaurants();
    }
    @Transactional
    public List<RestaurantEntity> getRestaurant(String restaurantName) throws RestaurantNotFoundException {
        if (restaurantName == null || restaurantName.isEmpty()) {
            throw new RestaurantNotFoundException("RNF-003", "Restaurant name field should not be empty");
        }

        return restaurantDao.getRestaurant(restaurantName);
    }

}
