package io.accretio.Models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.*;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Table
@Entity
public class Tag extends PanacheEntityBase {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    private String name;


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

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER, mappedBy = "tags")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<Topic> topics = new HashSet<>();

    @Transient
    private int countTopics;


    public Set<Topic> getTopics() {
        return topics;
    }

    public void setTopics(Set<Topic> topics) {
        this.countTopics = topics.size();
        this.topics = topics;
    }

    public int getCountTopics() {
        return countTopics;
    }

    public void setCountTopics(int countTopics) {
        this.countTopics = countTopics;
    }


}
