package org.kravbank.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import kotlin.RequiresOptIn;

import javax.enterprise.context.RequestScoped;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Requirement extends PanacheEntity {
    public String title;

    public String description;

    public String needId;

   // @OneToMany
   // public List<RequirementVariant> variant;

    //public String tags;

}
