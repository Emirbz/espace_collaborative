package io.accretio.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Table
@Entity
public class Badge extends PanacheEntityBase  {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    private String name;
    private String icon_s;
    private String icon_b;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon_s() {
        return icon_s;
    }

    public void setIcon_s(String icon_s) {
        this.icon_s = icon_s;
    }

    public String getIcon_b() {
        return icon_b;
    }

    public void setIcon_b(String icon_b) {
        this.icon_b = icon_b;
    }
}
