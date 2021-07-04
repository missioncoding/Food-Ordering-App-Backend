package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * GET method for handling get all restaurant request
     * @return restaurantDetailsResponseList
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, path = "restaurant", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestaurantListResponse> getAllRestaurant() {
        List<RestaurantEntity> allRestaurant = restaurantService.getAllRestaurant();

        RestaurantListResponse restaurantListResponse = new RestaurantListResponse().restaurants((List<RestaurantList>) fetchDetails(allRestaurant));
        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);
    }
    /**
     * GET method for handling get restaurant based on restaurant name request
     * @param restaurantName
     * @return restaurantDetailsResponseList
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, path = "/restaurant/name/{restaurant_name}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurant(
            @PathVariable("restaurant_name") final String restaurantName)
            throws RestaurantNotFoundException {
        List<RestaurantEntity> foundRestaurant = restaurantService.getRestaurant(restaurantName);
        RestaurantListResponse restaurantListResponse = new RestaurantListResponse().restaurants(fetchDetails(foundRestaurant));
        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);
    }
    /**
     * GET method for handling get restaurant based on restaurant id request
     * @param restaurant_id
     * @return restaurantDetailsResponseList
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, path = "/api/restaurant/{restaurant_id}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurantById(
            @PathVariable("restaurant_id") final String restaurant_id)
            throws RestaurantNotFoundException {

        RestaurantEntity restaurantEntities = restaurantService.restaurantByUUID(restaurant_id);

        /*Get Restaurant categories */
        List<CategoryEntity> categoryEntities = categoryService.getCategoriesByRestaurant(restaurantEntities.getUuid());
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
        restaurantFoundResponse.setRestaurantName(restaurantEntities.getRestaurant_name());
        restaurantFoundResponse.setId(UUID.fromString(restaurantEntities.getUuid()));
        restaurantFoundResponse.setPhotoURL(restaurantEntities.getPhoto_url());
        restaurantFoundResponse.setCustomerRating(restaurantEntities.getCustomer_rating());
        restaurantFoundResponse.setAveragePrice(restaurantEntities.getAverage_price_for_two());
        restaurantFoundResponse.setNumberCustomersRated(restaurantEntities.getNumber_of_customers_rated());
        restaurantFoundResponse.categories(categories);
        restaurantFoundResponse.address(new RestaurantDetailsResponseAddress()
                .id(UUID.fromString(restaurantEntities.getAddressEntity().getUuid()))
                .flatBuildingName(restaurantEntities.getAddressEntity().getFlat_buil_number())
                .locality(restaurantEntities.getAddressEntity().getLocality())
                .city(restaurantEntities.getAddressEntity().getCity())
                .pincode(restaurantEntities.getAddressEntity().getPincode())
                .state(new RestaurantDetailsResponseAddressState()
                        .id(UUID.fromString(restaurantEntities.getAddressEntity().getStateEntity().getUuid()))
                        .stateName(restaurantEntities.getAddressEntity().getStateEntity().getState_name())
                )
        );
        restaurantDetailsResponseList.add(restaurantFoundResponse);
        RestaurantListResponse restaurantListResponse = new RestaurantListResponse().restaurants(restaurantDetailsResponseList);
        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);
    }

    public List<RestaurantList> fetchDetails(List<RestaurantEntity> detailList) {
        List<RestaurantList> restaurantLists = new LinkedList<>();
        for (RestaurantEntity q : detailList) {
            List<CategoryEntity> categoryEntities = categoryService.getCategoriesByRestaurant(q.getUuid());
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
                    .id(UUID.fromString(q.getAddressEntity().getStateEntity().getUuid()))
                    .stateName(q.getAddressEntity().getStateEntity().getState_name());

            //Creating the RestaurantDetailsResponseAddress for the RestaurantList
            RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress()
                    .id(UUID.fromString(q.getAddressEntity().getUuid()))
                    .city(q.getAddressEntity().getCity())
                    .flatBuildingName(q.getAddressEntity().getFlat_buil_number())
                    .locality(q.getAddressEntity().getLocality())
                    .pincode(q.getAddressEntity().getPincode())
                    .state(restaurantDetailsResponseAddressState);

            //Creating RestaurantList to add to list of RestaurantList
            RestaurantList restaurantList = new RestaurantList()
                    .id(UUID.fromString(q.getUuid()))
                    .restaurantName(q.getRestaurant_name())
                    .averagePrice(q.getAverage_price_for_two())
                    .categories(categories)
                    .customerRating(q.getCustomer_rating())
                    .numberCustomersRated(q.getNumber_of_customers_rated())
                    .photoURL(q.getPhoto_url())
                    .address(restaurantDetailsResponseAddress);

            //Adding it to the list
            restaurantLists.add(restaurantList);


        }
        return restaurantLists;
    }


}

