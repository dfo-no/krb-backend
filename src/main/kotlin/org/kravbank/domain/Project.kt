package org.kravbank.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import io.quarkus.hibernate.orm.panache.PanacheEntity
import org.hibernate.annotations.ResultCheckStyle
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import org.kravbank.utils.EntityState
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity

//todo finn en måte å oppdatere deletedDate i native queryet  -> SET deletedDate = current_timestamp etc
@SQLDelete(
    sql = "UPDATE project SET state = 'DELETED'   WHERE id = ?",
    check = ResultCheckStyle.COUNT
)
@Where(clause = "state <> 'DELETED'")
class Project : PanacheEntity() {

    lateinit var title: String

    lateinit var description: String

    @Column(
        unique = true,
        name = "ref"
    )
    var ref: String = UUID.randomUUID().toString()

    @Enumerated(EnumType.STRING)
    var state: EntityState = EntityState.ACTIVE

    var deletedDate: LocalDateTime? = null

    @OneToMany(
        mappedBy = ("project"),
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    @JsonBackReference(value = "product")
    var products = mutableListOf<Product>()

    @OneToMany(
        mappedBy = ("project"),
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    //@JsonIgnore
    @JsonBackReference(value = "val-publication")
    var publications = mutableListOf<Publication>()

    @OneToMany(
        mappedBy = ("project"),
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    //@JsonIgnore
    @JsonBackReference(value = "val-requirement")
    var requirements = mutableListOf<Requirement>()

    @OneToMany(
        mappedBy = ("project"),
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    //@JsonIgnore
    @JsonBackReference(value = "val-need-project")
    var needs = mutableListOf<Need>()

    @OneToMany(
        mappedBy = ("project"),
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    @JsonBackReference(value = "val-codelist")
    //@JsonIgnore
    var codelist = mutableListOf<Codelist>()
}