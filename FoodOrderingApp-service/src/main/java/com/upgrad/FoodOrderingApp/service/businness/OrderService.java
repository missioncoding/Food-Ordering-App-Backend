package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.dao.OrderDao;
import com.upgrad.FoodOrderingApp.service.dao.OrderItemDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrdersEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zeelani
 * Service Class for handling the orders business logic
 */

@Service
public class OrderService {

    @Autowired
    OrderDao orderDao;

    @Autowired
    OrderItemDao orderItemDao;

    @Autowired
    CustomerDao customerDao;

     /**
     * Method to save the order
     * @param ordersEntity
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public OrdersEntity saveOrder(OrdersEntity ordersEntity) {
        OrdersEntity savedOrderEntity = orderDao.saveOrder(ordersEntity);
        return savedOrderEntity;
    }

    /* This method is to saveOrderItem.Takes the orderItemEntity  and saves it to DB and returns saved the OrderItemEntity.
    If error throws exception with error code and error message.
    */

    /**
     * method to save the order item
     * @param orderItemEntity
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public OrderItemEntity saveOrderItem(OrderItemEntity orderItemEntity) {
        OrderItemEntity savedOrderItemEntity = orderItemDao.saveOrderItem(orderItemEntity);
        return savedOrderItemEntity;
    }

    /**
     * Method to get the orders by customers
     * @param customerUuid
     * @return
     */
    public List<OrdersEntity> getOrdersByCustomers(String customerUuid) {
        CustomerEntity customerEntity = customerDao.fetchByUuid(customerUuid);
        List<OrdersEntity> ordersEntities = orderDao.fetchCustomerOrders(customerEntity);
        return ordersEntities;
    }

    /**
     * Method to get Order Items By Order.
     * @param ordersEntity
     * @return
     */
    public List<OrderItemEntity> getOrderItemsByOrder(OrdersEntity ordersEntity) {
        List<OrderItemEntity> orderItemEntities = orderItemDao.fetchByOrder(ordersEntity);
        return orderItemEntities;
    }
}

