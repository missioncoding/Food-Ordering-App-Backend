package com.upgrad.FoodOrderingApp.service.entity;


import com.upgrad.FoodOrderingApp.service.common.ItemType;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//This Class represents the ItemEntity table in the DB


@NamedQueries({
    @NamedQuery(name = "item.fetchByUuid", query = "SELECT i FROM ItemEntity i WHERE i.uuid = :uuid"),
})

@Entity
@Table(name = "item", uniqueConstraints = {@UniqueConstraint(columnNames = {"uuid"})})
public class ItemEntity implements Serializable {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(name = "uuid")
    @Size(max = 200)
    @NotNull
    private String uuid;

    @Column(name = "item_name")
    @Size(max = 30)
    @NotNull
    private String itemName;

    @Column(name = "price")
    @NotNull
    private Integer price;


    @Column(name = "type")
    @Size(max = 10)
    @NotNull
    private ItemType type;

    // adding parameterized constructor for testing purpose
    public ItemEntity(Integer id, @Size(max = 200) @NotNull String uuid, @Size(max = 30) @NotNull String itemName, @NotNull Integer price, @Size(max = 10) @NotNull ItemType type) {
        this.id = id;
        this.uuid = uuid;
        this.itemName = itemName;
        this.price = price;
        this.type = type;
    }

    public ItemEntity() {}

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

    public String getitemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }
}
