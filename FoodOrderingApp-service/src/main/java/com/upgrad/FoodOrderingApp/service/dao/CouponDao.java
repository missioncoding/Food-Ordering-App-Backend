package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * @author zeelani
 * Reposity class handling the DB operations of coupon table
 */

@Repository
public class CouponDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * method to fetch the coupon by name
     * @param couponName
     * @return
     */
    public CouponEntity fetchByName(String couponName){
        try{
            CouponEntity couponEntity = entityManager.createNamedQuery("coupon.fetchByName",CouponEntity.class).setParameter("coupon_name",couponName).getSingleResult();
            return couponEntity;
        }catch (NoResultException nre){
            return null;
        }
    }

    /**
     * method to fetch the coupon by ID
     * @param couponUuid
     * @return
     */
    public CouponEntity fetchById(String couponUuid) {
        try {
            CouponEntity couponEntity = entityManager.createNamedQuery("coupon.fetchByUuid",CouponEntity.class).setParameter("uuid",couponUuid).getSingleResult();
            return couponEntity;
        }catch (NoResultException nre){
            return null;
        }
    }
}
