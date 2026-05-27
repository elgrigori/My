package com.volunteer.actionservice.application.domain;

import jakarta.persistence.Column;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("DONATION")
public class DonationAction extends Action {
    @Column(length = 2000)
    public String requiredItems;

    @ElementCollection
    @CollectionTable(name = "donation_action_products", joinColumns = @JoinColumn(name = "action_id"))
    public List<ActionProduct> products = new ArrayList<>();

    @Override
    public ActionType type() {
        return ActionType.DONATION;
    }
}
