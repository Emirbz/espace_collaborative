package io.accretio.Services;

import io.accretio.Config.LoggingFilter;
import io.accretio.Models.Room;
import io.accretio.Models.User;
import io.accretio.Repository.RoomRepository;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;



@ApplicationScoped
public class RoomService {
    @Inject
    private RoomRepository roomRepository;


    public List<Room> getRoom(){
        return roomRepository.listAll();
    }

    private static final Logger LOG = Logger.getLogger(LoggingFilter.class);



    public void addRoom(Room room){
        LOG.infof(room.toString());
        roomRepository.persist(room);


    }

/*    public void addUser(User user,long id){
        if(getSigneRoom(id).getUsers().contains(user)) {
            return;
        }
        getSigneRoom(id).getUsers().add(user);
    }*/

    public  void deleteRoom(Room room)
    {
        roomRepository.delete(room);

    }

    public void updateRoom(int id , Room room)
    {
        roomRepository.updateRoom(id,room);


    }

    public Room getSigneRoom(long id)
    {
        return roomRepository.findById(id);
    }



}