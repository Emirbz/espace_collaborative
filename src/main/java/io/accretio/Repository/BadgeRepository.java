package io.accretio.Repository;

import io.accretio.Models.Badge;
import io.accretio.Models.Choix;
import io.accretio.Models.Topic;
import io.accretio.Models.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class BadgeRepository implements PanacheRepository<Badge> {


}