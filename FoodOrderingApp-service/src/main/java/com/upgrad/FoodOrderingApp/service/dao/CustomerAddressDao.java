package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author zeelani
 * Repository class handing Customer Address related DB operations
 */

@Repository
public class CustomerAddressDao {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Fetch Customer details based on the address provided
     * @param addressEntity
     * @return
     */
    public CustomerAddressEntity fetchCustomerByAddress(AddressEntity addressEntity){
        try {
            CustomerAddressEntity customerAddressEntity = entityManager.createNamedQuery("customer_address.fetchByAddress",CustomerAddressEntity.class).setParameter("address_entity",addressEntity).getSingleResult();
            return customerAddressEntity;
        }catch (NoResultException nre){
            return null;
        }
    }

    /**
     * Persist customer address in DB
     * @param customerAddressEntity
     * @return
     */
    public CustomerAddressEntity saveCustomerAddress(CustomerAddressEntity customerAddressEntity){
        entityManager.persist(customerAddressEntity);
        return customerAddressEntity;
    }

    /**
     * Fetch all address related to customer
     * @param customerEntity
     * @return
     */
    public List<CustomerAddressEntity> fetchAllCustomerAddress(CustomerEntity customerEntity){
        try{
            Integer active = 1;
            List <CustomerAddressEntity> customerAddressEntities = entityManager.createNamedQuery("customer_address.fetchByCustomer",CustomerAddressEntity.class).setParameter("customer_entity",customerEntity).setParameter("active",active).getResultList();
            return customerAddressEntities;
        }catch (NoResultException nre){
            return null;
        }
    }
}
