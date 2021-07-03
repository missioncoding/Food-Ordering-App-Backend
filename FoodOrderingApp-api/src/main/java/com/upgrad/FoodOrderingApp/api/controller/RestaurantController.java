package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponse;
import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponseAddress;
import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponseAddressState;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
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
    /**
     * GET method for handling get restaurant based on restaurant name request
     * @param restaurantName
     * @return restaurantDetailsResponseList
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, path = "/restaurant/name/{restaurant_name}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<RestaurantDetailsResponse>> getRestaurant(
            @PathVariable("restaurant_name") final String restaurantName)
            throws RestaurantNotFoundException {
        List<RestaurantEntity> foundRestaurant = restaurantService.getRestaurant(restaurantName);
        List<RestaurantDetailsResponse> restaurantFoundResponseList = new ArrayList<>();
        for (RestaurantEntity q : foundRestaurant) {
            RestaurantDetailsResponse restaurantFoundResponse = new RestaurantDetailsResponse();
            restaurantFoundResponse.setRestaurantName(q.getRestaurant_name());
            restaurantFoundResponse.setId(UUID.fromString(q.getUuid()));
            restaurantFoundResponse.setPhotoURL(q.getPhoto_url());
            restaurantFoundResponse.setCustomerRating(q.getCustomer_rating());
            restaurantFoundResponse.setAveragePrice(q.getAverage_price_for_two());
            restaurantFoundResponse.setNumberCustomersRated(q.getNumber_of_customers_rated());

            restaurantFoundResponseList.add(restaurantFoundResponse);
        }
        if (restaurantFoundResponseList.isEmpty()) {
            return new ResponseEntity<List<RestaurantDetailsResponse>>(restaurantFoundResponseList, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<List<RestaurantDetailsResponse>>(restaurantFoundResponseList, HttpStatus.OK);
        }
    }
    /**
     * GET method for handling get restaurant based on restaurant id request
     * @param restaurant_id
     * @return restaurantDetailsResponseList
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, path = "/api/restaurant/{restaurant_id}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<RestaurantDetailsResponse>> getRestaurantById(
            @PathVariable("restaurant_id") final String restaurant_id)
            throws RestaurantNotFoundException {

        RestaurantEntity restaurantEntities = restaurantService.getRestaurantById(restaurant_id);
        List<RestaurantDetailsResponse> restaurantDetailsResponseList = new ArrayList<>();
        RestaurantDetailsResponse restaurantFoundResponse = new RestaurantDetailsResponse();
        restaurantFoundResponse.setRestaurantName(restaurantEntities.getRestaurant_name());
        restaurantFoundResponse.setId(UUID.fromString(restaurantEntities.getUuid()));
        restaurantFoundResponse.setPhotoURL(restaurantEntities.getPhoto_url());
        restaurantFoundResponse.setCustomerRating(restaurantEntities.getCustomer_rating());
        restaurantFoundResponse.setAveragePrice(restaurantEntities.getAverage_price_for_two());
        restaurantFoundResponse.setNumberCustomersRated(restaurantEntities.getNumber_of_customers_rated());

        restaurantDetailsResponseList.add(restaurantFoundResponse);

        if (restaurantDetailsResponseList.isEmpty()) {
            return new ResponseEntity<List<RestaurantDetailsResponse>>(restaurantDetailsResponseList, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<List<RestaurantDetailsResponse>>(restaurantDetailsResponseList, HttpStatus.OK);
        }
    }
}

