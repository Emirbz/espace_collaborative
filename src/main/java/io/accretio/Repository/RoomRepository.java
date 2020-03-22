package io.accretio.Repository;


import io.accretio.Models.Room;
import io.quarkus.hibernate.orm.panache.PanacheRepository;


import javax.enterprise.context.ApplicationScoped;
@ApplicationScoped
public class  RoomRepository implements PanacheRepository<Room> {



    public void updateRoom(int id ,Room room)
    {
        update("name = ?1 , subject= ?2  where id = ?3", room.name,room.subject, (long) id);

    }

}