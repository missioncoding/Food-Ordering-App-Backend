package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author zeelani
 * class representing the state entity
 */


@NamedQueries({
    @NamedQuery(name = "state.fetchByUuid", query = "SELECT s from StateEntity s where s.stateUuid = :uuid"),
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
    private String stateUuid;

    @Column(name = "state_name")
    @Size(max = 30)
    private String stateName;

     public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStateUuid() {
        return stateUuid;
    }

    public void setStateUuid(String stateUuid) {
        this.stateUuid = stateUuid;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
