package com.upgrad.FoodOrderingApp.service.entity;


import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author zeelani
 * Entity class representing the Categoryitem table
 */

@NamedQueries({
    @NamedQuery(name = "category_item.fetchAll",query = "SELECT c FROM CategoryItemEntity c WHERE c.category = :category ORDER BY LOWER(c.item.itemName) ASC "),
})

@Entity
@Table(name = "category_item")
public class CategoryItemEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private ItemEntity item;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private CategoryEntity category;

    // adding parameterized constructor for testing purpose
    public CategoryItemEntity(Integer id, @NotNull ItemEntity item, @NotNull CategoryEntity category) {
        this.id = id;
        this.item = item;
        this.category = category;
    }

    public CategoryItemEntity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ItemEntity getItem() {
        return item;
    }

    public void setItem(ItemEntity item) {
        this.item = item;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }
}

