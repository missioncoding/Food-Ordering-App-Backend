package com.upgrad.FoodOrderingApp.service.entity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author zeelani
 * Entity class representing coupon table
 */


@NamedQueries({
    @NamedQuery(name = "coupon.fetchByName",query = "SELECT c FROM CouponEntity c WHERE c.couponName = :coupon_name"),
    @NamedQuery(name = "coupon.fetchByUuid",query = "SELECT c FROM  CouponEntity c WHERE c.uuid = :uuid"),
})

@Entity
@Table(name = "coupon",uniqueConstraints = {@UniqueConstraint(columnNames = {"uuid"})})
public class CouponEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "uuid")
    @Size(max = 200)
    @NotNull
    private String uuid;

    @Column(name = "coupon_name")
    @Size(max = 255)
    private String couponName;

    @Column(name = "percent")
    @NotNull
    private Integer percent;

    // adding parameterized constructor for testing purpose
    public CouponEntity(@Size(max = 200) @NotNull String uuid, @Size(max = 255) String couponName, @NotNull Integer percent) {
        this.uuid = uuid;
        this.couponName = couponName;
        this.percent = percent;
    }

    public CouponEntity() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }
}
