package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponse;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Vipin Mohan
 * Controller class for handling the Restaurant endpoint requests
 */
@RestController
@RequestMapping("/")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    /**
     * GET method for handling get all restaurant request
     * @return restaurantDetailsResponseList
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, path = "restaurant", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RestaurantDetailsResponse>> getAllRestaurant() {
        List<RestaurantEntity> allRestaurant = restaurantService.getAllRestaurant();
        List<RestaurantDetailsResponse> restaurantDetailsResponseList = new ArrayList<>();
        for (RestaurantEntity q : allRestaurant) {
            RestaurantDetailsResponse restaurantDetailsResponse = new RestaurantDetailsResponse();
            restaurantDetailsResponse.setRestaurantName(q.getRestaurant_name());
            restaurantDetailsResponse.setId(UUID.fromString(q.getUuid()));
            restaurantDetailsResponse.setPhotoURL(q.getPhoto_url());
            restaurantDetailsResponse.setCustomerRating(q.getCustomer_rating());
            restaurantDetailsResponse.setAveragePrice(q.getAverage_price_for_two());
            restaurantDetailsResponse.setNumberCustomersRated(q.getNumber_of_customers_rated());

            restaurantDetailsResponseList.add(restaurantDetailsResponse);
        }
        return new ResponseEntity<List<RestaurantDetailsResponse>>(restaurantDetailsResponseList, HttpStatus.OK);
    }
}

