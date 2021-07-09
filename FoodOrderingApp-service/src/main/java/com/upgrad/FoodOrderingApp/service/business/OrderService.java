package com.upgrad.FoodOrderingApp.service.business;

import com.upgrad.FoodOrderingApp.service.dao.CouponDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.dao.OrderDao;
import com.upgrad.FoodOrderingApp.service.dao.OrderItemDao;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrdersEntity;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
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
    CouponDao couponDao;

    @Autowired
    CustomerDao customerDao;

    @Autowired
    OrderItemDao orderItemDao;

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

     /**
     * Method to get the coupon entity using coupon name
     * @param couponName
     * @return
     * @throws CouponNotFoundException
     */
    public CouponEntity getCouponByCouponName(String couponName) throws CouponNotFoundException {
        if(couponName == null||couponName == ""){
            throw new CouponNotFoundException("CPF-002","Coupon name field should not be empty");
        }
        CouponEntity couponEntity = couponDao.fetchByName(couponName);
        if(couponEntity == null){
            throw new CouponNotFoundException("CPF-001","No coupon by this name");
        }
        return couponEntity;
    }

    /**
     * Method to get the coupon entity using the coupon id
     * @param couponUuid
     * @return
     * @throws CouponNotFoundException
     */
    public CouponEntity getCouponByCouponId(String couponUuid) throws CouponNotFoundException {
        CouponEntity couponEntity = couponDao.fetchById(couponUuid);
        if(couponEntity == null){
            throw new CouponNotFoundException("CPF-002","No coupon by this id");
        }
        return couponEntity;
    }
}

