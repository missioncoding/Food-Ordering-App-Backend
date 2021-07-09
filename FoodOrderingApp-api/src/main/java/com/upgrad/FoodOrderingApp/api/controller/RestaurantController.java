package com.upgrad.FoodOrderingApp.api.controller;


import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.business.*;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
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
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

/**
 * @author vipin,zeelani
 * Restaurant Controller Handles all  the restaurant related endpoints
 */

@CrossOrigin
@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {

    // injecting the service classes to handle business logic operations
    @Autowired
    ItemService itemService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    CustomerService customerService;

    @Autowired
    RestaurantService restaurantService;

    /**
     * Get method fetch retrieve all restaurants
     * @return RestaurantListResponse
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET,path = "",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getAllRestaurants() {
        List<RestaurantList> restaurantLists = null;
        try {
            // fetching the restaurant list based on the criteria with a private method
            restaurantLists = getRestaurantList("RATING", null);
        } catch (RestaurantNotFoundException | CategoryNotFoundException r) {
            // this case can be ignored as we are not searching with name or category
            restaurantLists = new LinkedList<>();
        }
        if (!restaurantLists.isEmpty()) {
            //Creating the RestaurantListResponse by adding the list of RestaurantList
            RestaurantListResponse restaurantListResponse = new RestaurantListResponse().restaurants(restaurantLists);
            return new ResponseEntity<RestaurantListResponse>(restaurantListResponse,HttpStatus.OK);
        } else {
            return new ResponseEntity<RestaurantListResponse>(new RestaurantListResponse(),HttpStatus.OK);
        }
    }


    /**
     * Get method to fetch all restaurants by name
     * @param restaurantName
     * @return RestaurantListResponse
     * @throws RestaurantNotFoundException
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET,path = "/name/{restaurant_name}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurantByName (@PathVariable(value = "restaurant_name") final String restaurantName)throws RestaurantNotFoundException {
        // fetching the restaurant list based on the criteria with a private method
        List<RestaurantList> restaurantLists = null;
        try {
            restaurantLists = getRestaurantList("NAME", restaurantName);
        } catch (CategoryNotFoundException c) {
            // this case can be ignored as we are not searching with any category
            // this condition will never happen
            restaurantLists = new LinkedList<>();
        }
        if (!restaurantLists.isEmpty()) {
            //Creating the RestaurantListResponse by adding the list of RestaurantList
            RestaurantListResponse restaurantListResponse = new RestaurantListResponse().restaurants(restaurantLists);
            return new ResponseEntity<RestaurantListResponse>(restaurantListResponse,HttpStatus.OK);
        } else {
            return new ResponseEntity<RestaurantListResponse>(new RestaurantListResponse(),HttpStatus.OK);
        }

    }

    /**
     * private method to fetch the restaurants based on the criteria and condition
     * @param withCriteria
     * @param byCondition
     * @return List<RestaurantList>
     * @throws RestaurantNotFoundException
     * @throws CategoryNotFoundException
     */
    private List<RestaurantList> getRestaurantList(String withCriteria, String byCondition) throws RestaurantNotFoundException, CategoryNotFoundException {
        List<RestaurantEntity> restaurantEntities = null;
        // deciding on the business logic function based on the criteria
        switch (withCriteria) {
            case "RATING" : {
                restaurantEntities = restaurantService.restaurantsByRating();
                break;
            }
            case "NAME" : {
                restaurantEntities = restaurantService.restaurantsByName(byCondition);
                break;
            }
            case "CATEGORY" : {
                restaurantEntities = restaurantService.restaurantByCategory(byCondition);
                break;
            }
            default: {
                // set to a empty list to return in case non matched
                restaurantEntities = new LinkedList<>();
            }
        }
        // initializing the end result list
        List<RestaurantList> restaurantLists = new LinkedList<>();
        if (!restaurantEntities.isEmpty()) {
            for (RestaurantEntity restaurantEntity : restaurantEntities) {

                //Calls  getCategoriesByRestaurant to get categories of the corresponding restaurant.
                List<CategoryEntity> categoryEntities = categoryService.getCategoriesByRestaurant(restaurantEntity.getUuid());
                String categories = new String();
                ListIterator<CategoryEntity> listIterator = categoryEntities.listIterator();
                //To concat the category names.
                while (listIterator.hasNext()) {
                    categories = categories + listIterator.next().getCategoryName();
                    if (listIterator.hasNext()) {
                        categories = categories + ", ";
                    }
                }

                //Creating the RestaurantDetailsResponseAddressState for the RestaurantDetailsResponseAddress
                RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState()
                        .id(UUID.fromString(restaurantEntity.getAddress().getState().getUuid()))
                        .stateName(restaurantEntity.getAddress().getState().getStateName());

                //Creating the RestaurantDetailsResponseAddress for the RestaurantList
                RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress()
                        .id(UUID.fromString(restaurantEntity.getAddress().getUuid()))
                        .city(restaurantEntity.getAddress().getCity())
                        .flatBuildingName(restaurantEntity.getAddress().getFlatBuilNo())
                        .locality(restaurantEntity.getAddress().getLocality())
                        .pincode(restaurantEntity.getAddress().getPincode())
                        .state(restaurantDetailsResponseAddressState);

                //Creating RestaurantList to add to list of RestaurantList
                RestaurantList restaurantList = new RestaurantList()
                        .id(UUID.fromString(restaurantEntity.getUuid()))
                        .restaurantName(restaurantEntity.getRestaurantName())
                        .averagePrice(restaurantEntity.getAvgPrice())
                        .categories(categories)
                        .customerRating(BigDecimal.valueOf(restaurantEntity.getCustomerRating()))
                        .numberCustomersRated(restaurantEntity.getNumberCustomersRated())
                        .photoURL(restaurantEntity.getPhotoUrl())
                        .address(restaurantDetailsResponseAddress);

                //Adding it to the list
                restaurantLists.add(restaurantList);

            }
        }
        return restaurantLists;
    }


    /**
     * Get the restaurants based on the category
     * @param categoryId
     * @return RestaurantListResponse
     * @throws CategoryNotFoundException
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET,path = "/category/{category_id}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurantByCategoryId(@PathVariable(value = "category_id")String categoryId) throws CategoryNotFoundException {

        List<RestaurantList> restaurantLists = null;
        try {
            // fetching the restaurant list based on the criteria with a private method
            restaurantLists = getRestaurantList("CATEGORY", categoryId);
        } catch (RestaurantNotFoundException r) {
            // this case can be ignored as we are not searching with any restaurant name
            restaurantLists = new LinkedList<>();
        }
        if (!restaurantLists.isEmpty()) {
            //Creating the RestaurantListResponse by adding the list of RestaurantList
            RestaurantListResponse restaurantListResponse = new RestaurantListResponse().restaurants(restaurantLists);
            return new ResponseEntity<RestaurantListResponse>(restaurantListResponse,HttpStatus.OK);
        } else {
            return new ResponseEntity<RestaurantListResponse>(new RestaurantListResponse(),HttpStatus.OK);
        }
    }


    /**
     * Get method to retrieve the detail response of a restaurant
     * @param restaurantUuid
     * @return RestaurantDetailsResponse
     * @throws RestaurantNotFoundException
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET,path = "/{restaurant_id}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantDetailsResponse>getRestaurantByRestaurantId(@PathVariable(value = "restaurant_id") final String restaurantUuid)throws RestaurantNotFoundException{

        //Calls restaurantByUUID method of restaurantService to get the restaurant entity.
        RestaurantEntity restaurantEntity = restaurantService.restaurantByUUID(restaurantUuid);

        //Calls  getCategoriesByRestaurant to get categories of the corresponding restaurant.
        List<CategoryEntity> categoryEntities = categoryService.getCategoriesByRestaurant(restaurantUuid);

        //Creating category Lists  for the response
        List<CategoryList> categoryLists = new LinkedList<>();
        for (CategoryEntity categoryEntity:categoryEntities){  //Looping for each CategoryEntity in categoryEntities

            //Calls getItemsByCategoryAndRestaurant of itemService to get list of itemEntities.
            List<ItemEntity> itemEntities = itemService.getItemsByCategoryAndRestaurant(restaurantUuid ,categoryEntity.getUuid());
            //Creating Item List for the CategoryList.
            List<ItemList> itemLists = new LinkedList<>();
            itemEntities.forEach(itemEntity -> {
                ItemList itemList = new ItemList()
                        .id(UUID.fromString(itemEntity.getUuid()))
                        .itemName(itemEntity.getitemName())
                        .price(itemEntity.getPrice())
                        .itemType(ItemList.ItemTypeEnum.valueOf(itemEntity.getType().getValue()));

                itemLists.add(itemList);
            });

            //Creating new category list to add listof category list
            CategoryList categoryList = new CategoryList()
                    .itemList(itemLists)
                    .id(UUID.fromString(categoryEntity.getUuid()))
                    .categoryName(categoryEntity.getCategoryName());

            //adding to the categoryLists
            categoryLists.add(categoryList);
        }

        //Creating the RestaurantDetailsResponseAddressState for the RestaurantDetailsResponseAddress
        RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState()
                .id(UUID.fromString(restaurantEntity.getAddress().getState().getUuid()))
                .stateName(restaurantEntity.getAddress().getState().getStateName());

        //Creating the RestaurantDetailsResponseAddress for the RestaurantList
        RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress()
                .id(UUID.fromString(restaurantEntity.getAddress().getUuid()))
                .city(restaurantEntity.getAddress().getCity())
                .flatBuildingName(restaurantEntity.getAddress().getFlatBuilNo())
                .locality(restaurantEntity.getAddress().getLocality())
                .pincode(restaurantEntity.getAddress().getPincode())
                .state(restaurantDetailsResponseAddressState);

        //Creating the RestaurantDetailsResponse by adding the list of categoryList and other details.
        RestaurantDetailsResponse restaurantDetailsResponse = new RestaurantDetailsResponse()
                .restaurantName(restaurantEntity.getRestaurantName())
                .address(restaurantDetailsResponseAddress)
                .averagePrice(restaurantEntity.getAvgPrice())
                .customerRating(BigDecimal.valueOf(restaurantEntity.getCustomerRating()))
                .numberCustomersRated(restaurantEntity.getNumberCustomersRated())
                .id(UUID.fromString(restaurantEntity.getUuid()))
                .photoURL(restaurantEntity.getPhotoUrl())
                .categories(categoryLists);

        return new ResponseEntity<RestaurantDetailsResponse>(restaurantDetailsResponse,HttpStatus.OK);
    }

    /**
     * PUT method to update the restaurant details
     * @param authorization
     * @param restaurantUuid
     * @param customerRating
     * @return
     * @throws AuthorizationFailedException
     * @throws RestaurantNotFoundException
     * @throws InvalidRatingException
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.PUT,path = "/{restaurant_id}",params = "customer_rating",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantUpdatedResponse> updateRestaurantDetails(@RequestHeader ("authorization")final String authorization,@PathVariable(value = "restaurant_id")final String restaurantUuid,@RequestParam(value = "customer_rating")final Double customerRating) throws AuthorizationFailedException, RestaurantNotFoundException, InvalidRatingException {

        //Access the accessToken from the request Header
        final String accessToken = authorization.split("Bearer ")[1];

        //Calls customerService getCustomerMethod to check the validity of the customer.this methods returns the customerEntity.
        CustomerEntity customerEntity = customerService.getCustomer(accessToken);

        //Calls restaurantByUUID method of restaurantService to get the restaurant entity.
        RestaurantEntity restaurantEntity = restaurantService.restaurantByUUID(restaurantUuid);

        //Calls updateRestaurantRating and passes restaurantentity found and customer rating and return the updated entity.
        RestaurantEntity updatedRestaurantEntity = restaurantService.updateRestaurantRating(restaurantEntity,customerRating);

        //Creating RestaurantUpdatedResponse containing the UUID of the updated Restaurant and the success message.
        RestaurantUpdatedResponse restaurantUpdatedResponse = new RestaurantUpdatedResponse()
                .id(UUID.fromString(restaurantUuid))
                .status("RESTAURANT RATING UPDATED SUCCESSFULLY");

        return new ResponseEntity<RestaurantUpdatedResponse>(restaurantUpdatedResponse,HttpStatus.OK);
    }
}
