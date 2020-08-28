package io.accretio.Models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;

@Table
@Entity
public class RoomRequest extends PanacheEntityBase {
    public enum requestType {
        PENDING,
        ACCEPTED,
        REJECTED,

    }

    public RoomRequest(Room room, User user, requestType status) {
        this.room = room;
        this.user = user;
        this.status = status;
    }

    public RoomRequest() {

    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Room room;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private User user;

    @Enumerated(EnumType.STRING)
    public requestType status = requestType.PENDING;

    public requestType getStatus() {
        return status;
    }

    public void setStatus(requestType status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
