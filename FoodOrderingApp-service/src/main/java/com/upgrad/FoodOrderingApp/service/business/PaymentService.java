package com.upgrad.FoodOrderingApp.service.business;

import com.upgrad.FoodOrderingApp.service.dao.PaymentDao;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import com.upgrad.FoodOrderingApp.service.exception.PaymentMethodNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author zeelani
 * Service class handling the payment business logic
 */

@Service
public class PaymentService {

    @Autowired
    PaymentDao paymentDao;


    /**
     * Method to get the payment entity using the id
     * @param paymentId
     * @return
     * @throws PaymentMethodNotFoundException
     */
    public PaymentEntity getPaymentByUUID(String paymentId) throws PaymentMethodNotFoundException {
        // calling dao method
        PaymentEntity paymentEntity = paymentDao.fetchByUUID(paymentId);
        if(paymentEntity == null){
            throw new PaymentMethodNotFoundException("PNF-002","No payment method found by this id");
        }
        return paymentEntity;
    }

    /**
     * Method to get all the payment methods
     * @return
     */
    public List<PaymentEntity> getAllPaymentMethods() {
        // call dao method to fetch all payment methods
        List<PaymentEntity> paymentEntities = paymentDao.fetchAllPaymentMethods();
        return paymentEntities;
    }
}

