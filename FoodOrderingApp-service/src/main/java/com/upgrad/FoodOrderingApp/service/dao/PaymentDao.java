package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

/**
 * @author zeelani
 * Repository class handling the DB operations of payment table
 */

@Repository
public class PaymentDao {

    @PersistenceContext
    private EntityManager entityManager;


    /**
     * Fetch the payment by UUID
     * @param paymentId
     * @return
     */
    public PaymentEntity fetchByUUID(String paymentId) {
        try{
            PaymentEntity paymentEntity = entityManager.createNamedQuery("payment.fetchByUuid",PaymentEntity.class).setParameter("uuid",paymentId).getSingleResult();
            return paymentEntity;
        }catch (NoResultException nre){
            return null;
        }
    }

    /**
     * Fetch the payment methods
     * @return
     */
    public List<PaymentEntity> fetchAllPaymentMethods() {
        try {
            List<PaymentEntity> paymentEntities =entityManager.createNamedQuery("payment.fetchAllMethods", PaymentEntity.class).getResultList();
            return paymentEntities;
        }catch (NoResultException nre){
            return null;
        }
    }
}

