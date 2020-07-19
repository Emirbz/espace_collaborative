package io.accretio.Repository;


import io.accretio.Models.TopicCategory;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TopicCategoryRepository implements PanacheRepository<TopicCategory> {

}