package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * @author zeelani
 * Repository class handling customer auth related DB operations
 */

@Repository
public class CustomerAuthDao {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Handle logout operation of customer
     * @param customerAuthEntity
     * @return
     */
    public CustomerAuthEntity customerLogout (CustomerAuthEntity customerAuthEntity){
        entityManager.merge(customerAuthEntity);
        return customerAuthEntity;
    }

    /**
     * Fetch customer auth using access token
     * @param accessToken
     * @return
     */
    public CustomerAuthEntity fetchCustomerAuth(String accessToken){
        try{
            CustomerAuthEntity customerAuthEntity = entityManager.createNamedQuery("customer_auth.fetchByAccessToken",CustomerAuthEntity.class).setParameter("access_Token",accessToken).getSingleResult();
            return customerAuthEntity;
        }catch (NoResultException nre){
            return null;
        }
    }

    /**
     * persist the customer auth in DB
     * @param customerAuthEntity
     * @return
     */
    public CustomerAuthEntity createCustomerAuth (CustomerAuthEntity customerAuthEntity){
        entityManager.persist(customerAuthEntity);
        return customerAuthEntity;
    }
}
