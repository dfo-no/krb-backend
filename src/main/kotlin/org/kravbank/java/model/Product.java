package org.kravbank.java.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class Product extends PanacheEntity {
    public String title;

    public String description;

    public String deletedDate;

    //public String type;
    //public children
    //public parent
}
