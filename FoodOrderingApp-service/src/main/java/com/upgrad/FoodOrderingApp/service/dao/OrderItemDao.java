package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrdersEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author zeelani
 * Repository class handling Order item related DB operations
 */

@Repository
public class OrderItemDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Save the order item in DB
     * @param orderItemEntity
     * @return
     */
    public OrderItemEntity saveOrderItem(OrderItemEntity orderItemEntity){
        entityManager.persist(orderItemEntity);
        return orderItemEntity;
    }

    /**
     * Fetch the order items based on order
     * @param ordersEntity
     * @return
     */
    public List<OrderItemEntity> fetchByOrder(OrdersEntity ordersEntity) {
        try{
            List<OrderItemEntity> orderItemEntities = entityManager.createNamedQuery("order_item.fetchByOrder", OrderItemEntity.class).setParameter("ordersEntity",ordersEntity).getResultList();
            return orderItemEntities;
        }catch (NoResultException nre) {
            return null;
        }
    }

}
