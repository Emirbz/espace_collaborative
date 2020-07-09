package io.accretio.Repository;

import io.accretio.Models.Message;
import io.accretio.Models.Reaction;
import io.accretio.Models.Sondage;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class SondageRepository implements PanacheRepository<Message> {


    public List<Message> findSondagesByRoom(int roomId)
    {
        return find("room_id=?1 and type=?2", Sort.by("timestamp", Sort.Direction.Descending),roomId,Message.type.SONDAGE).list();




    }








}
