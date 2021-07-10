package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CategoryItemEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author zeelani
 * Repository class handing category item DB operations
 */

@Repository
public class CategoryItemDao {


    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Method to fetch all category items
     * @param categoryEntity
     * @return
     */
    public List<CategoryItemEntity> fetchAllItems(CategoryEntity categoryEntity) {
        try {
            List<CategoryItemEntity> categoryItems = entityManager.createNamedQuery("category_item.fetchAll",CategoryItemEntity.class).
                                                                  setParameter("category",categoryEntity).getResultList();
            return categoryItems;
        }catch (NoResultException nre){
            return null;
        }
    }
}

