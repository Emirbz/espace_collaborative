package io.accretio.Models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.util.*;


@Table
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class Room extends PanacheEntityBase {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    public long id;

    public String name;

    @Lob
    private String image;

    public String subject;

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


    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "room_user",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users = new HashSet<>();


    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", subject='" + subject + '\'' +
                ", users=" + users.toString() +
                '}';
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

    @Override
    public int hashCode() {
        return Objects.hash(id, name, image, subject, users);
    }

    /*public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
    @OneToMany(mappedBy = "room", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private List<Message> messages;*/


}