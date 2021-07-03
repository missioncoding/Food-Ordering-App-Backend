package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * @author zeelani
 * Repository class handling customer related DB operations
 */

@Repository
public class CustomerDao {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * updates the customer entity in database
     * @param customerEntity
     * @return
     */
    public CustomerEntity updateCustomer(CustomerEntity customerEntity){
        entityManager.merge(customerEntity);
        return customerEntity;
    }

    /**
     * Method to fetch the customer details using the UUID
     * @param uuid
     * @return
     */
    public CustomerEntity fetchByUuid (final String uuid){
        try {
            CustomerEntity customer = entityManager.createNamedQuery("customer.fetchByUuid",CustomerEntity.class).setParameter("uuid",uuid).getSingleResult();
            return customer;
        }catch (NoResultException nre){
            return null;
        }
    }

    /**
     * Fetch customer details based on contact number
     * @param contact_number
     * @return
     */
    public CustomerEntity fetchByContactNumber (final String contact_number){
        try{
            CustomerEntity customer = entityManager.createNamedQuery("customer.fetchByContactNumber",CustomerEntity.class).setParameter("contact_number",contact_number).getSingleResult();
            return customer;
        }catch (NoResultException nre){
            return null;
        }
    }

    /**
     * Creates a customer entry in database
     * @param customerEntity
     * @return
     */
    public CustomerEntity createCustomer(CustomerEntity customerEntity){
        entityManager.persist(customerEntity);
        return customerEntity;
    }

}
