package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.business.CategoryService;
import com.upgrad.FoodOrderingApp.service.business.CustomerService;
import com.upgrad.FoodOrderingApp.service.business.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * @author Vipin Mohan
 * Controller class for handling the Restaurant endpoint requests
 */
@RestController
@RequestMapping("/")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CustomerService customerService;

    /**
     * GET method for handling get all restaurant request
     * @return restaurantListResponse
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, path = "restaurant", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestaurantListResponse> restaurantsByRating() throws CategoryNotFoundException {
        List<RestaurantEntity> allRestaurant = restaurantService.restaurantsByRating();

        RestaurantListResponse restaurantListResponse = new RestaurantListResponse().restaurants((List<RestaurantList>) fetchDetails(allRestaurant));
        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);
    }
    /**
     * GET method for handling get restaurant based on restaurant name request
     * @param restaurantName
     * @return restaurantListResponse
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, path = "/restaurant/name/{restaurant_name}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> restaurantsByName(
            @PathVariable("restaurant_name") final String restaurantName)
            throws RestaurantNotFoundException, CategoryNotFoundException {
        List<RestaurantEntity> foundRestaurant = restaurantService.getRestaurantsByName(restaurantName);
        RestaurantListResponse restaurantListResponse = new RestaurantListResponse().restaurants(fetchDetails(foundRestaurant));
        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);

    }
    /**
     * GET method for handling get restaurant based on restaurant id request
     * @param restaurant_id
     * @return restaurantListResponse
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, path = "/api/restaurant/{restaurant_id}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> restaurantByUUID(
            @PathVariable("restaurant_id") final String restaurant_id)
            throws RestaurantNotFoundException, CategoryNotFoundException {

        RestaurantEntity restaurantEntities = restaurantService.getRestaurantByUUID(restaurant_id);

        /*Get Restaurant categories */
        List<CategoryEntity> categoryEntities = categoryService.fetchCategoriesByRestaurant(restaurantEntities.getUuid());
        String categories = new String();
        //To concat the category names.
        ListIterator<CategoryEntity> listIterator = categoryEntities.listIterator();
        while (listIterator.hasNext()) {
            categories = categories + listIterator.next().getCategoryName();
            if (listIterator.hasNext()) {
                categories = categories + ", ";
            }
        }
        List<RestaurantList> restaurantDetailsResponseList = new ArrayList<>();

        List<RestaurantList> restaurantLists = new LinkedList<>();
        RestaurantList restaurantFoundResponse = new RestaurantList();
        restaurantFoundResponse.setRestaurantName(restaurantEntities.getRestaurantName());
        restaurantFoundResponse.setId(UUID.fromString(restaurantEntities.getUuid()));
        restaurantFoundResponse.setPhotoURL(restaurantEntities.getPhotoUrl());
        restaurantFoundResponse.setCustomerRating(BigDecimal.valueOf(restaurantEntities.getCustomerRating()));
        restaurantFoundResponse.setAveragePrice(restaurantEntities.getAvgPrice());
        restaurantFoundResponse.setNumberCustomersRated(restaurantEntities.getNumberCustomersRated());
        restaurantFoundResponse.categories(categories);
        restaurantFoundResponse.address(new RestaurantDetailsResponseAddress()
                .id(UUID.fromString(restaurantEntities.getAddress().getUuid()))
                .flatBuildingName(restaurantEntities.getAddress().getFlatBuilNo())
                .locality(restaurantEntities.getAddress().getLocality())
                .city(restaurantEntities.getAddress().getCity())
                .pincode(restaurantEntities.getAddress().getPincode())
                .state(new RestaurantDetailsResponseAddressState()
                        .id(UUID.fromString(restaurantEntities.getAddress().getState().getStateUuid()))
                        .stateName(restaurantEntities.getAddress().getState().getStateName())
                )
        );
        restaurantDetailsResponseList.add(restaurantFoundResponse);
        RestaurantListResponse restaurantListResponse = new RestaurantListResponse().restaurants(restaurantDetailsResponseList);
        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);

    }

    /**
     * GET method for handling get restaurant based on category id request
     * @param category_id
     * @return restaurantListResponse
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, path = "/restaurant/category/{category_id}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurantsByCategoryId(@PathVariable final String category_id) throws CategoryNotFoundException {
        CategoryEntity categoryEntity = categoryService.fetchCategoryById(category_id);
        List<RestaurantEntity> restaurantEntities = restaurantService.getRestaurantByCategory(category_id);
        RestaurantListResponse restaurantListResponse = new RestaurantListResponse().restaurants(fetchDetails(restaurantEntities));

        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);

    }

    /**
     * Common method to get all response details
     * @param detailList
     * @return
     * @throws CategoryNotFoundException
     */
    public List<RestaurantList> fetchDetails(List<RestaurantEntity> detailList) throws CategoryNotFoundException {
        List<RestaurantList> restaurantLists = new LinkedList<>();
        for (RestaurantEntity q : detailList) {
            List<CategoryEntity> categoryEntities = categoryService.fetchCategoriesByRestaurant(q.getUuid());
            String categories = new String();
            //To concat the category names.
            ListIterator<CategoryEntity> listIterator = categoryEntities.listIterator();
            while (listIterator.hasNext()) {
                categories = categories + listIterator.next().getCategoryName();
                if (listIterator.hasNext()) {
                    categories = categories + ", ";
                }
            }
            //Creating the RestaurantDetailsResponseAddressState for the RestaurantDetailsResponseAddress
            RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState()
                    .id(UUID.fromString(q.getAddress().getState().getStateUuid()))
                    .stateName(q.getAddress().getState().getStateName());

            //Creating the RestaurantDetailsResponseAddress for the RestaurantList
            RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress()
                    .id(UUID.fromString(q.getAddress().getUuid()))
                    .city(q.getAddress().getCity())
                    .flatBuildingName(q.getAddress().getFlatBuilNo())
                    .locality(q.getAddress().getLocality())
                    .pincode(q.getAddress().getPincode())
                    .state(restaurantDetailsResponseAddressState);

            //Creating RestaurantList to add to list of RestaurantList
            RestaurantList restaurantList = new RestaurantList()
                    .id(UUID.fromString(q.getUuid()))
                    .restaurantName(q.getRestaurantName())
                    .averagePrice(q.getAvgPrice())
                    .categories(categories)
                    .customerRating(BigDecimal.valueOf(q.getCustomerRating()))
                    .numberCustomersRated(q.getNumberCustomersRated())
                    .photoURL(q.getPhotoUrl())
                    .address(restaurantDetailsResponseAddress);

            //Adding it to the list
            restaurantLists.add(restaurantList);


        }
        return restaurantLists;
    }

    /**
     *
     * @param customerRating
     * @param restaurant_id
     * @param authorization
     * @return
     * @throws RestaurantNotFoundException
     * @throws InvalidRatingException
     * @throws AuthorizationFailedException
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.PUT, path = "/restaurant/{restaurant_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantUpdatedResponse> updateRestaurantDetails(@RequestParam Double customerRating ,
                                                                             @PathVariable("restaurant_id") final String restaurant_id,
                                                                             @RequestHeader("authorization") final String authorization)
            throws RestaurantNotFoundException, InvalidRatingException, AuthorizationFailedException
    {
        RestaurantEntity restaurantEntity = restaurantService.getRestaurantByUUID(restaurant_id);

        String bearerToken = null;
        try {
            bearerToken = authorization.split("Bearer ")[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            bearerToken = authorization;
        }

        CustomerEntity userAuthToken = customerService.getCustomer(authorization);

        RestaurantEntity updatedRestaurantEntity = restaurantService.updateRestaurantRating(restaurantEntity,customerRating);
        RestaurantUpdatedResponse restUpdateResponse = new RestaurantUpdatedResponse()
                .id(UUID.fromString(updatedRestaurantEntity.getUuid()))
                .status("RESTAURANT RATING UPDATED SUCCESSFULLY");
        return new ResponseEntity<RestaurantUpdatedResponse>(restUpdateResponse, HttpStatus.OK);
    }
}

