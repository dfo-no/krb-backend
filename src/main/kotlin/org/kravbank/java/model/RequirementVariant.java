package org.kravbank.java.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class RequirementVariant extends PanacheEntity {
    public String description;

    public String requirementText;

    public String instruction;

    public Boolean useProduct;

    public Boolean useSpesification; //typo

    public Boolean useQualification;

    @OneToMany
    public List<Project> products;

    @OneToMany
    public List <Config> configs;


    //public questions
    // public String type;


    //CONFIG


}
