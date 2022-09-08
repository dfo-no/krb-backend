package org.kravbank.java.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

//@AllArgsConstructor
//@NoArgsConstructor
//@Data
@Entity
//@Builder
public class Project extends PanacheEntity {

//    @Column(length = 200)
    public String title;

    public String description;

    public Long version;

    public String publishedDate;

    public String projectId; //uuid

    public String deletedDate;

    @OneToMany
    public List <Publication> publications;

    @OneToMany
    public List <Need> needs;

    @OneToMany
    public List <Requirement> requirements; // -> private String requirementVarian; --> private string config -->

    @OneToMany
    public List <Product> products;

    @OneToMany

    public List <Code> codeList; //CONFIG


    //private String dependency; //vent med bruk
    //private String tags; //vent med bruk

}