package com.upgrad.FoodOrderingApp.service.business;

import com.upgrad.FoodOrderingApp.service.common.ApplicationUtil;
import com.upgrad.FoodOrderingApp.service.dao.*;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.ItemNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * @author zeelani
 * Class handling the business logic operations of Item entity
 */

@Service
public class ItemService {

    @Autowired
    ItemDao itemDao;

    @Autowired
    OrderDao orderDao;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    OrderItemDao orderItemDao;

    @Autowired
    RestaurantDao restaurantDao;

    @Autowired
    CategoryItemDao categoryItemDao;

    @Autowired
    ApplicationUtil applicationUtil;

    @Autowired
    RestaurantItemDao restaurantItemDao;

    /**
     * get items by the popularity
     * @param restaurantEntity
     * @return list of item entity
     */
    public List<ItemEntity> getItemsByPopularity(RestaurantEntity restaurantEntity) {

        //initializing the item entities list
        List <ItemEntity> itemEntities = new LinkedList<>();

        //get all the order entities from the restaurant
        List <OrdersEntity> ordersEntities = orderDao.fetchByRestaurant(restaurantEntity);

        //Looping through the ordered entities
        ordersEntities.forEach(ordersEntity -> {
            //fetching order item for each order
            List <OrderItemEntity> orderItemEntities = orderItemDao.fetchByOrder(ordersEntity);
            orderItemEntities.forEach(orderItemEntity -> {
                itemEntities.add(orderItemEntity.getItem());
            });
        });

        //create a map count for each order item
        Map<String,Integer> itemCountMap = new HashMap<String,Integer>();
        // looping for each to order item and set the count
        itemEntities.forEach(itemEntity -> {
            Integer count = itemCountMap.get(itemEntity.getUuid());
            itemCountMap.put(itemEntity.getUuid(),(count == null) ? 1 : count+1);
        });

        // calling the utility method to sort the map
        Map<String,Integer> sortedItemCountMap = applicationUtil.sortMapByValues(itemCountMap);

        //Creating the top 5 Itementity list
        List<ItemEntity> sortedItemEntites = new LinkedList<>();
        Integer count = 0;
        for(Map.Entry<String,Integer> item:sortedItemCountMap.entrySet()){
            if(count < 5) {
                //Calls getItemByUUID to get the Item entity
                sortedItemEntites.add(itemDao.fetchByUUID(item.getKey()));
                count = count+1;
            }else{
                break;
            }
        }
        return sortedItemEntites;
    }

    /**
     * Method to get the items by category
     * @param categoryEntity
     * @return
     */
    public List<ItemEntity> getItemsByCategory(CategoryEntity categoryEntity) {
        // fetch all category items from the input category
        List<CategoryItemEntity> categoryItemEntities = categoryItemDao.fetchAllItems(categoryEntity);
        List<ItemEntity> itemEntities = new LinkedList<>();
        categoryItemEntities.forEach(categoryItemEntity -> {
            ItemEntity itemEntity = categoryItemEntity.getItem();
            itemEntities.add(itemEntity);
        });
        return itemEntities;
    }

    /**
     * Method to get the items by category and restaurant
     * @param restaurantUuid
     * @param categoryUuid
     * @return list of item entity
     */
    public List<ItemEntity> getItemsByCategoryAndRestaurant(String restaurantUuid, String categoryUuid) {

        // get the  RestaurantEntity using the restaurant id
        RestaurantEntity restaurantEntity = restaurantDao.fetchByUuid(restaurantUuid);

        // get the  CategoryEntity using the category id
        CategoryEntity categoryEntity = categoryDao.fetchByUuid(categoryUuid);

        // get the  list of RestaurantItemEntity using the above restaurant entity
        List<RestaurantItemEntity> restaurantItemEntities = restaurantItemDao.getItemsByRestaurant(restaurantEntity);

        // get the  list of CategoryItemEntity using the above category entity
        List<CategoryItemEntity> categoryItemEntities = categoryItemDao.fetchAllItems(categoryEntity);

        // Creating list of item entity common to the restaurant and category.
        List<ItemEntity> itemEntities = new LinkedList<>();

        restaurantItemEntities.forEach(restaurantItemEntity -> {
            categoryItemEntities.forEach(categoryItemEntity -> {
                if(restaurantItemEntity.getItem().equals(categoryItemEntity.getItem())){
                    itemEntities.add(restaurantItemEntity.getItem());
                }
            });
        });

        return itemEntities;
    }

    public ItemEntity getItemByUUID(String itemUuid) throws ItemNotFoundException {
        ItemEntity itemEntity = itemDao.fetchByUUID(itemUuid);
        if(itemEntity == null){
            throw new ItemNotFoundException("INF-003","No item by this id exist");
        }
        return itemEntity;
    }
}

