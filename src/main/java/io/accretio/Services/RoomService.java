package io.accretio.Services;

import io.accretio.Config.LoggingFilter;
import io.accretio.Models.Room;
import io.accretio.Models.User;
import io.accretio.Repository.RoomRepository;
import io.accretio.Utils.FileUploader;
import io.minio.errors.*;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
        if (room.getImage()!= null) {
            try {
                room.setImage(new FileUploader().addImage(room.getImage()));
            } catch (InvalidPortException | InvalidEndpointException | IOException | InvalidKeyException | NoSuchAlgorithmException | InsufficientDataException | InvalidExpiresRangeException | InvalidResponseException | InternalException | XmlParserException | InvalidBucketNameException | ErrorResponseException | RegionConflictException e) {
                e.printStackTrace();
            }
        }
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