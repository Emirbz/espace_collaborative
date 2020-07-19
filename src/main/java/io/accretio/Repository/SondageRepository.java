package io.accretio.Repository;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import io.accretio.Models.Message;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;

@ApplicationScoped
public class SondageRepository implements PanacheRepository<Message> {

    public List<Message> findSondagesByRoom(int roomId) {
        return find("room_id=?1 and type=?2", Sort.by("timestamp", Sort.Direction.Descending), roomId,
                Message.type.SONDAGE).list();

    }

}
