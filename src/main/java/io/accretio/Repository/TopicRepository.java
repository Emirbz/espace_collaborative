package io.accretio.Repository;


import io.accretio.Models.Tag;
import io.accretio.Models.Topic;
import io.accretio.Models.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class TopicRepository implements PanacheRepository<Topic> {

    public List<Topic> getAllTopics() {
        return listAll(Sort.descending("timestamp"));

    }

    public List<Topic> getMyTopics(User user) {
        return find("user_id=?1",Sort.by("timestamp", Sort.Direction.Descending),user.getId()).list();
    }
}