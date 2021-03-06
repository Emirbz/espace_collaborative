package io.accretio.Models;

import com.fasterxml.jackson.annotation.*;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;


@Table
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Message extends PanacheEntityBase {
    public enum type {
        TEXT,
        IMAGE,
        AUDIO,
        FILE,
        SONDAGE,
        VIDEO
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    public long id;


    @Transient
    public File metaData;

    public File getMetaData() {
        return metaData;
    }

    public void setMetaData(File metaData) {
        this.metaData = metaData;
    }

    @Lob
    private String body;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Lob
    private String file;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }




    @Enumerated(EnumType.ORDINAL)
    private type type;


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Message.type getType() {
        return type;
    }

    public void setType(Message.type type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setUser(User userId) {
        this.user = userId;
    }


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public Room getRoom() {
        return room;
    }

    public void setRoomId(Room roomId) {
        this.room = roomId;
    }


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
//TODO timestamp ghalet
    private long timestamp = new Date().getTime() / 1000 ;




    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Room room;



    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private User user;

    public Set<Reaction> getReactions() {
        return reactions;
    }

    public void setReactions(Set<Reaction> reactions) {
        this.reactions = reactions;
    }

    @OneToMany(mappedBy = "message", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<Reaction> reactions;
    @OneToMany(mappedBy = "message", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<Choix> choix;

    public Set<Choix> getChoix() {
        return choix;
    }

    public void setChoix(Set<Choix> choix) {
        this.choix = choix;
    }


    public Message() {
    }

    public Message(long id) {
        this.id = id;
    }
}
