package io.accretio.Repository;


import io.accretio.Models.Tag;
import io.accretio.Models.Topic;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;

@ApplicationScoped
public class TopicRepository implements PanacheRepository<Topic> {

    public void findTopicsByTags(List<Tag> tags) {

    }
}