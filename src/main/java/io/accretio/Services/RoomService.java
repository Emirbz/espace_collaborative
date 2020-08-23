package io.accretio.Services;

import io.accretio.Config.LoggingFilter;
import io.accretio.Models.Room;
import io.accretio.Models.RoomRequest;
import io.accretio.Models.User;
import io.accretio.Repository.RoomRepository;
import io.accretio.Utils.FileUploader;
import io.minio.errors.*;
import org.jboss.logging.Logger;

import javax.annotation.Nullable;
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

    @Inject
    RoomRequestService roomRequestService;

    private static final Logger LOG = Logger.getLogger(LoggingFilter.class);

    public List<Room> getRoom(User loggedUser) {
        List<Room> roomList = roomRepository.listAll();


        roomList.forEach(room -> {
            RoomRequest roomRequest = roomRequestService.checkUserGotRequest(loggedUser, room);
            if (roomRequest != null) {
                room.setRequestStatus(roomRequest.status);
            }
        });
        return roomList;
    }

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


    public void deleteRoom(Room room) {
        roomRepository.delete(room);

    }

    public void updateRoom(int id, Room room) {

        roomRepository.updateRoom(id, room);

    }

    public Room addUsers(Room room, User user) {
        if (room.isPrivate) {
            RoomRequest roomRequest = roomRequestService.checkUserGotRequest(user, room);
            if (roomRequest == null) {
                roomRequestService.addRequest(user, room);
                room.setRequestStatus(RoomRequest.requestType.PENDING);
            } else if (roomRequest.getStatus().equals(RoomRequest.requestType.ACCEPTED)) {
                room.getUsers().add(user);
                roomRepository.persist(room);
            }

            return room;

        }
        room.getUsers().add(user);
        roomRepository.persist(room);
        return room;
    }

    public Room getSigneRoom(long id) {
        return roomRepository.findById(id);
    }

    public List<Room> getMyRooms(User user, @Nullable String name) {
        List<Room> rooms;
        assert name != null;
        if (name.length() == 0) {
            rooms = getRoom(user);

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

    public List<Room> getMyCreatedRooms(User user, @Nullable String name) {
        List<Room> rooms;
        assert name != null;
        if (name.length() == 0) {
            rooms = getRoom(user);

        } else {
            rooms = roomRepository.searchRoomByName(name);
        }
        return rooms.stream().filter(room -> room.getUser().getId().equals(user.getId())).collect(Collectors.toList());


    }
}