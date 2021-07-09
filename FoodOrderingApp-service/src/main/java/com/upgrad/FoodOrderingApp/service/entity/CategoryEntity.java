package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @author zeelani
 * Entity class representing the Category table
 */

@NamedQueries({
    @NamedQuery(name = "category.fetchByUuid",query = "SELECT c FROM CategoryEntity c WHERE c.uuid = :uuid"),
    @NamedQuery(name = "category.fetchAll",query = "SELECT c FROM CategoryEntity c ORDER BY c.categoryName ASC "),
})

@Entity
@Table(name = "category",uniqueConstraints = {@UniqueConstraint(columnNames = {"uuid"})})
public class CategoryEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid")
    @Size(max = 200)
    @NotNull
    private String uuid;

    @Column(name = "category_name")
    @Size(max = 255)
    private String categoryName;

    @ManyToMany
    @JoinTable(name = "category_item", joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<ItemEntity> items = new ArrayList<>();

    // adding parameterized constructor for testing purpose
    public CategoryEntity(@Size(max = 200) @NotNull String uuid, @Size(max = 255) String categoryName, List<ItemEntity> items) {
        this.uuid = uuid;
        this.categoryName = categoryName;
        this.items = items;
    }

    public CategoryEntity() {
    }

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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<ItemEntity> getItems() {
        return items;
    }

    public void setItems(List<ItemEntity> items) {
        this.items = items;
    }
}
