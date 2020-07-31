package io.accretio.Repository;


import io.accretio.Models.Message;
import io.accretio.Models.Tag;
import io.accretio.Models.Topic;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class TagRepository implements PanacheRepository<Tag> {

    public boolean checkTagExists(String name) {
        return find("name", name).list().size() > 0;

    }

    public Tag getTagByName(String name) {
        return find("name", name).firstResult();

    }

    public List<Tag> searchTagByName(String name) {
        return list("name Like concat('%', ?1, '%')", name);

    }
}