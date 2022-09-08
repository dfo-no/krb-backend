package org.kravbank.java.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import javax.persistence.Entity;

@Entity
public class Publication extends PanacheEntity {
    public String comment;

    public String date;

    public Long version;

    public String bankId;

    public String deletedDate;


}
