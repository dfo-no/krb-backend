package org.kravbank.java.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity

public class Need extends PanacheEntity {

    public String title;

    public String description;

    @OneToMany
    public List<Requirement> requirements; // fra Requirements

   // public String type;  //need
    //public parent




}
