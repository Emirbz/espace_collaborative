package io.accretio.Models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Table
@Entity
public class Room extends PanacheEntityBase  {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    public long id;

    public String name;

    @Lob
    private String image;

    public String subject;

    public boolean isPrivate;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private User user;
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "room_user",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users = new HashSet<>();

    public RoomRequest.requestType getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RoomRequest.requestType requestStatus) {
        this.requestStatus = requestStatus;
    }

    @Transient
    private RoomRequest.requestType requestStatus;

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Set<User> getUsers() {
        return users;
    }


    public void setUsers(Set<User> users) {
        this.users = users;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return id == room.id &&
                Objects.equals(name, room.name) &&
                Objects.equals(image, room.image) &&
                Objects.equals(subject, room.subject) &&
                Objects.equals(users, room.users);
    }

    public Room(long id) {
        this.id = id;
    }

    public Room() {

    }

}