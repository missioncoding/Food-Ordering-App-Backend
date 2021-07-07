package com.upgrad.FoodOrderingApp.api.controller;

<<<<<<< HEAD

=======
>>>>>>> 593795181ed6dd7e05627612f643ea78447c27c0
import com.upgrad.FoodOrderingApp.api.model.ItemList;
import com.upgrad.FoodOrderingApp.api.model.ItemListResponse;
import com.upgrad.FoodOrderingApp.service.business.ItemService;
import com.upgrad.FoodOrderingApp.service.business.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
<<<<<<< HEAD
 * @author murarka
 * Item controller handling all item related apis
 */

@RestController
@RequestMapping("/item")
public class ItemController {

    @Autowired
    ItemService itemService;

    @Autowired
    RestaurantService restaurantService;

    /**
     * get method to retrieve the top 5 popular items for the restaurant using id
     * @param restaurantUuid
     * @return
     * @throws RestaurantNotFoundException
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET,path = "/restaurant/{restaurant_id}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ItemListResponse> getTopFivePopularItems (@PathVariable(value = "restaurant_id")final String restaurantUuid) throws RestaurantNotFoundException {

        //calling restaurantService method to get the restaurant entity.
        RestaurantEntity restaurantEntity = restaurantService.getRestaurantByUUID(restaurantUuid);

        //using the item service to get the popular items of the restaurant
        List<ItemEntity> itemEntities = itemService.getItemsByPopularity(restaurantEntity);

        //Creating the api response
        ItemListResponse itemListResponse = new ItemListResponse();
        itemEntities.forEach(itemEntity -> {
            ItemList itemList = new ItemList()
                    .id(UUID.fromString(itemEntity.getUuid()))
                    .itemName(itemEntity.getitemName())
                    .price(itemEntity.getPrice())
                    .itemType(ItemList.ItemTypeEnum.fromValue(itemEntity.getType().getValue()));
            itemListResponse.add(itemList);
        });

        return new ResponseEntity<ItemListResponse>(itemListResponse,HttpStatus.OK);
=======
 * Created by murarka on 07/06/21.
 */

@RestController
public class ItemController {
    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private ItemService itemService;

    @RequestMapping(method = RequestMethod.GET,
                    path = "/item/restaurant/{restaurantId}",
                    produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ItemListResponse> getItemList(@PathVariable("restaurant_id") final String restaurantUuid )
        throws RestaurantNotFoundException {

        RestaurantEntity restaurantEntity = restaurantService.restaurantByUUID(restaurantUuid);
        List<ItemEntity> itemEntityList = itemService.getTopFiveItems(restaurantEntity);

        ItemListResponse itemListResponse = new ItemListResponse();

        for(ItemEntity ie: itemEntityList) {
            ItemList itemList = new ItemList().id(UUID.fromString(ie.getUuid()))
                    .itemName(ie.getitemName())
                    .price(ie.getPrice());
            itemListResponse.add(itemList);


        }

        return new ResponseEntity<ItemListResponse>(itemListResponse, HttpStatus.OK);
>>>>>>> 593795181ed6dd7e05627612f643ea78447c27c0
    }
}
