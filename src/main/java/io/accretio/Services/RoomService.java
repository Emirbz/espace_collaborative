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
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RoomService {
    @Inject
    RoomRepository roomRepository;

    public List<Room> getRoom() {
        return roomRepository.listAll();
    }

    private static final Logger LOG = Logger.getLogger(LoggingFilter.class);

    public void addRoom(Room room) {
        if (room.getImage() != null) {
            try {
                room.setImage(new FileUploader().addImage(room.getImage()));
            } catch (InvalidPortException | InvalidEndpointException | IOException | InvalidKeyException
                    | NoSuchAlgorithmException | InsufficientDataException | InvalidExpiresRangeException
                    | InvalidResponseException | InternalException | XmlParserException | InvalidBucketNameException
                    | ErrorResponseException | RegionConflictException e) {
                e.printStackTrace();
            }
        }
        roomRepository.persist(room);

    }

    /*
     * public void addUser(User user,long id){
     * if(getSigneRoom(id).getUsers().contains(user)) { return; }
     * getSigneRoom(id).getUsers().add(user); }
     */

    public void deleteRoom(Room room) {
        roomRepository.delete(room);

    }

    public void updateRoom(int id, Room room) {

        roomRepository.updateRoom(id, room);

    }

    public Room addUsers(Room room, User user) {
        room.getUsers().add(user);
        roomRepository.persist(room);
        return room;
    }

    public Room getSigneRoom(long id) {
        return roomRepository.findById(id);
    }

    public List<Room> getMyRooms(User user, String name) {
        List<Room> rooms;
        assert name != null;
        if (name.length() == 0) {
            rooms = getRoom();

        } else {
            rooms = roomRepository.searchRoomByName(name);
        }
        return rooms.stream().filter(room -> room.getUsers().contains(user)).collect(Collectors.toList());

    }

    public Room removeUser(Room room, User user) {
        room.getUsers().remove(user);
        roomRepository.persist(room);
        return room;
    }
}