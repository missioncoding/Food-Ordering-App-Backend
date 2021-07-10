package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author zeelani
 * class representing the state entity
 */


@NamedQueries({
    @NamedQuery(name = "state.fetchByUuid", query = "SELECT s from StateEntity s where s.uuid = :uuid"),
    @NamedQuery(name = "state.fetchAll",query = "SELECT s from StateEntity s"),
})

@Entity
@Table(name = "state",uniqueConstraints = {@UniqueConstraint(columnNames = {"uuid"})})
public class StateEntity implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid")
    @Size(max = 200)
    @NotNull
    private String uuid;

    @Column(name = "state_name")
    @Size(max = 30)
    private String stateName;

    // creating a parameterized constructor for the testing purpose
    public StateEntity(String uuid, String stateName) {
        this.uuid = uuid;
        this.stateName = stateName;
        return;
    }

    public StateEntity() {

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

    public void setUuid(String stateUuid) {
        this.uuid = uuid;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
