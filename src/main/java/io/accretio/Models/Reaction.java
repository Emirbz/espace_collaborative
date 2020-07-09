package io.accretio.Models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;


@Table
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Reaction extends PanacheEntityBase {
    public enum type {
        BRAVO,
        LIKE,
        LOVE,
        INTERESTING
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    public long id;


    @Override
    public String toString() {
        return "Reaction{" +
                "id=" + id +
                ", type=" + type +
                ", message=" + message +
                ", user=" + user +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public Reaction.type getType() {
        return type;
    }

    @Enumerated(EnumType.STRING)
    public type type;


    public void setType(Reaction.type type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Message message;


    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private User user;

}
