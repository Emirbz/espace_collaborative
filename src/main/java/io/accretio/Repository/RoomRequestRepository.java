package io.accretio.Repository;

import io.accretio.Models.Badge;
import io.accretio.Models.Room;
import io.accretio.Models.RoomRequest;
import io.accretio.Models.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class RoomRequestRepository implements PanacheRepository<RoomRequest> {


    public RoomRequest checkUserRequest(User user, Room room) {
       return find("user_id=?1 AND room_id=?2",user.getId(),room.getId()).firstResult();
    }

    public List<RoomRequest> findPendingRequests() {
        return list("status=?1",RoomRequest.requestType.PENDING);
    }

    public void acceptRequest(RoomRequest roomRequest) {
        update("status=?1 where id=?2",RoomRequest.requestType.ACCEPTED,roomRequest.getId());
    }
    public void rejectRequest(RoomRequest roomRequest) {
        update("status=?1 where id=?2",RoomRequest.requestType.REJECTED,roomRequest.getId());
    }


}