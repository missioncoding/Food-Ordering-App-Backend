package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrdersEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author zeelani
 * Repository class to handle Order related db operations
 */

@Repository
public class OrderDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Fetch the orders based on the restaurant
     * @param restaurantEntity
     * @return
     */
    public List<OrdersEntity> fetchByRestaurant(RestaurantEntity restaurantEntity){
        try{
            List<OrdersEntity> ordersEntities = entityManager.createNamedQuery("orders.fetchByRestaurant",OrdersEntity.class).setParameter("restaurant",restaurantEntity).getResultList();
            return ordersEntities;
        }catch (NoResultException nre){
            return null;
        }
    }

    /**
     * Fetch the orders by address
     * @param addressEntity
     * @return
     */
    public List<OrdersEntity> fetchByAddress(AddressEntity addressEntity) {
        try{
            List<OrdersEntity> ordersEntities = entityManager.createNamedQuery("orders.fetchByAddress",OrdersEntity.class).setParameter("address",addressEntity).getResultList();
            return ordersEntities;
        }catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Persists the order in DB
     * @param ordersEntity
     * @return
     */
    public OrdersEntity saveOrder(OrdersEntity ordersEntity){
        entityManager.persist(ordersEntity);
        return ordersEntity;
    }

    /**
     * Fetch all orders of a customer
     * @param customerEntity
     * @return
     */
    public List<OrdersEntity> fetchCustomerOrders(CustomerEntity customerEntity) {
        try {
            List<OrdersEntity> ordersEntities = entityManager.createNamedQuery("orders.fetchByCustomer",OrdersEntity.class).setParameter("customer",customerEntity).getResultList();
            return ordersEntities;
        }catch (NoResultException nre){
            return null;
        }
    }
}
