package com.volunteer.actionservice.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("CONTRIBUTE")
public class ContributeAction extends Action {
    @Column(length = 2000)
    public String requiredItems;

    @ElementCollection
    @CollectionTable(name = "action_products", joinColumns = @JoinColumn(name = "action_id"))
    public List<ActionProduct> products = new ArrayList<>();

    @Override
    public ActionType type() {
        return ActionType.CONTRIBUTE;
    }
}
