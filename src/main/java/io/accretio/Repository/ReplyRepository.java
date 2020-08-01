package io.accretio.Repository;


import io.accretio.Models.Message;
import io.accretio.Models.Reply;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class ReplyRepository implements PanacheRepository<Reply> {

    public List<Reply> getRepliesByTopic(Integer id) {
        return find("topic_id=?1" , Sort.by("timestamp", Sort.Direction.Descending),id).list();

    }
}