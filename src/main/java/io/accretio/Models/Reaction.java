package io.accretio.Models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;


@Table
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Reaction extends PanacheEntityBase {
    public enum reactionType {
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
                ", message=" + message.getId() +
                ", user=" + user +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public Reaction.reactionType getType() {
        return type;
    }

    @Enumerated(EnumType.STRING)
    public reactionType type;


    public void setType(Reaction.reactionType type) {
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

public Reaction()
{

}

    public Reaction(Reaction.reactionType type, User user) {
    this.type = type;
        this.user = user;
    }
}
