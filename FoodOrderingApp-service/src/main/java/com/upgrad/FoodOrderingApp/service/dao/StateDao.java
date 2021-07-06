package com.upgrad.FoodOrderingApp.service.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.stereotype.Repository;

/**
 * @author zeelani
 * Repository class for handling db operations for state table
 */

@Repository
public class StateDao {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Fetch state by UUID
     * @param uuid
     * @return
     */
    public StateEntity fetchByUuid(String uuid){
        try{
            StateEntity stateEntity = entityManager.createNamedQuery("state.fetchByUuid",StateEntity.class).setParameter("uuid",uuid).getSingleResult();
            return stateEntity;
        }catch (NoResultException nre){
            return null;
        }
    }

    /**
     * Fetch all states
     * @return
     */
    public List<StateEntity> fetchAll(){
        try {
            List<StateEntity> stateEntities = entityManager.createNamedQuery("state.fetchAll",StateEntity.class).getResultList();
            return stateEntities;
        }catch (NoResultException nre){
            return null;
        }
    }
}
