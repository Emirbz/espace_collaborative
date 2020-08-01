package io.accretio.Models;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Table
@Entity
public class Topic extends PanacheEntityBase {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    private String title;

    @Lob
    private String description;

    private long timestamp = new Date().getTime() / 1000;

    @Enumerated(EnumType.STRING)
    private Status status = Status.Active;


    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private User user;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "topic_tags",
            joinColumns = @JoinColumn(name = "topic_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "topic", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<Reply> replies;

    @Transient
    private int countReplies;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Set<Reply> getReplies() {
        this.setCountReplies(this.replies.size());
        return replies;
    }

    public void setReplies(Set<Reply> replies) {
        this.replies = replies;
    }

    public enum Status {
        Active, Inactive
    }

    public int getCountReplies() {
        return countReplies;
    }

    public void setCountReplies(int countReplies) {
        this.countReplies = countReplies;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Topic{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", timestamp=" + timestamp +
                ", status=" + status +
                ", user=" + user +
                ", tags=" + tags +
                ", countReplies=" + countReplies +
                '}';
    }
}
