package io.accretio.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;


@Table
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class Message extends PanacheEntityBase {
    public enum type {
        TEXT,
        IMAGE,
        AUDIO,
        FILE
    }
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    public long id;

    @Lob
    private String body;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }



    public void setRoom(Room room) {
        this.room = room;
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

    public void setUser(User userId) {
        this.user = userId;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoomId(Room roomId) {
        this.room = roomId;
    }

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn (name="room_id",referencedColumnName="id")
    private Room room;


    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn (name="user_id",referencedColumnName="id")
    private User user;

}
