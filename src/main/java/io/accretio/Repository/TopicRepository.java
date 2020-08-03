package io.accretio.Repository;


import io.accretio.Models.Reply;
import io.accretio.Models.Tag;
import io.accretio.Models.Topic;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;

@ApplicationScoped
public class TopicRepository implements PanacheRepository<Topic> {

    public List<Topic> getAllTopics() {
        return listAll(Sort.descending("timestamp"));

    }
}