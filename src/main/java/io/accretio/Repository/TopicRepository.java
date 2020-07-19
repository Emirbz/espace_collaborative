package io.accretio.Repository;


import io.accretio.Models.Topic;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TopicRepository implements PanacheRepository<Topic> {

}