package io.accretio.Repository;


import io.accretio.Models.Reply;
import io.accretio.Models.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class ReplyRepository implements PanacheRepository<Reply> {

    public List<Reply> getRepliesByTopic(Integer id) {
        return find("topic_id=?1" , Sort.by("timestamp", Sort.Direction.Descending),id).list();

    }

    public List<Reply> getMyReplies(User user) {
        return find("user_id=?1", Sort.by("timestamp", Sort.Direction.Descending),user.getId()).list();
    }
    public List<Reply> searchMyReplies(User user, String name) {
        return find("user_id=?1 AND reply Like concat('%', ?2, '%')",Sort.by("timestamp", Sort.Direction.Descending),user.getId(),name).list();
    }

    public void setInactive(Reply reply) {
        update("useful =?1 where id = ?2",!(reply.isUseful()),  reply.getId());
    }
}