package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * @author zeelani
 * Repository class for handling the item table db operations
 */

@Repository
public class ItemDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * fetch the item by UUID
     * @param uuid
     * @return
     */
    public ItemEntity fetchByUUID(String uuid) {
        try {
            ItemEntity itemEntity = entityManager.createNamedQuery("item.fetchByUuid",ItemEntity.class).setParameter("uuid",uuid).getSingleResult();
            return itemEntity;
        }catch (NoResultException nre){
            return null;
        }
    }
}

