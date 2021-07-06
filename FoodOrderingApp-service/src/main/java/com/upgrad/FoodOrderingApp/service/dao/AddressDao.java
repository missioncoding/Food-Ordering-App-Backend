package com.upgrad.FoodOrderingApp.service.dao;


import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import org.springframework.stereotype.Repository;

/**
 * @author zeelani
 * Repository class to handle the db opertions of Address entity
 */

@Repository
public class AddressDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Method to save address
     * @param addressEntity
     * @return
     */
    public AddressEntity save(AddressEntity addressEntity){
        entityManager.persist(addressEntity);
        return addressEntity;
    }

    /**
     * Method to get address by UUID
     * @param uuid
     * @return
     */
    public AddressEntity fetchByUuid(String uuid){
        try{
            AddressEntity addressEntity = entityManager.createNamedQuery("address.fetchByUuid",AddressEntity.class).setParameter("uuid",uuid).getSingleResult();
            return addressEntity;
        }catch (NoResultException nre){
            return null;
        }
    }

    /**
     * Method to delete the address
     * @param addressEntity
     * @return
     */
    public AddressEntity delete(AddressEntity addressEntity) {
        entityManager.remove(addressEntity);
        return addressEntity;
    }

    /**
     * Method to update the address
     * @param addressEntity
     * @return
     */
    public AddressEntity updateActiveStatus(AddressEntity addressEntity) {
        entityManager.merge(addressEntity);
        return addressEntity;
    }
}
