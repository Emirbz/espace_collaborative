package io.accretio.Services;

import io.accretio.Config.LoggingFilter;
import io.accretio.Models.Room;
import io.accretio.Models.RoomRequest;
import io.accretio.Models.User;
import io.accretio.Repository.RoomRequestRepository;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class RoomRequestService {

    @Inject
    RoomRequestRepository roomRequestRepository;

    @Inject
    RoomService roomService;


    private static final Logger LOG = Logger.getLogger(LoggingFilter.class);


    public RoomRequest checkUserGotRequest(User user, Room room) {
        return roomRequestRepository.checkUserRequest(user, room);
    }

    public RoomRequest getSingleRoomRequest(int id) {
        return roomRequestRepository.findById((long) id);
    }

    public void addRequest(User user, Room room) {
        RoomRequest roomRequest = new RoomRequest(room, user, RoomRequest.requestType.PENDING);
        roomRequestRepository.persist(roomRequest);
    }

    public List<RoomRequest> getMyRequests(User loggedUser) {
        List<RoomRequest> roomRequestList = roomRequestRepository.findPendingRequests();
        roomRequestList.removeIf(roomRequest -> !roomRequest.getRoom().getUser().getId().equals(loggedUser.getId()));
        return roomRequestList;

    }

    public void acceptRequest(RoomRequest roomRequest) {
        roomRequestRepository.acceptRequest(roomRequest);
        roomRequest.setStatus(RoomRequest.requestType.ACCEPTED);
        roomService.addUsers(roomRequest.getRoom(), roomRequest.getUser());
    }

    public void rejectRequest(RoomRequest roomRequest) {
        roomRequestRepository.rejectRequest(roomRequest);
    }

    public void deleteRoomRequest(RoomRequest roomRequest) {
        roomRequestRepository.delete(roomRequest);
    }


}
