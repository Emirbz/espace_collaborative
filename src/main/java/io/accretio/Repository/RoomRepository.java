package io.accretio.Repository;


import io.accretio.Models.Room;
import io.accretio.Models.Topic;
import io.accretio.Models.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;


import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class  RoomRepository implements PanacheRepository<Room> {



    public void updateRoom(int id ,Room room)
    {
        update("name = ?1 , subject= ?2  where id = ?3", room.name,room.subject, (long) id);

    }

    public List<Room> searchRoomByName(String name) {
        return find("name Like concat('%', ?1, '%')",name).list();

    }


}