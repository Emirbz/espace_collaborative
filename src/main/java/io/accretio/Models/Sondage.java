package io.accretio.Models;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;



@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Sondage extends PanacheEntityBase {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    public long id;


    private String body;


    private Boolean active = true;

    private long timestamp = new Date().getTime() / 1000;


    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private User user;


    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Room room;


 /*   public Set<Choix> getChoix() {
        return choix;
    }

    public void setChoix(Set<Choix> choix) {
        this.choix = choix;
    }

    @OneToMany(mappedBy = "sondage", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<Choix> choix;*/


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
