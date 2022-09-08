package org.kravbank.java.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import javax.persistence.Entity;

@Entity
public class Code extends PanacheEntity {
    public String title;

    public String description;

   // public String type; //code

    //public parent

}
