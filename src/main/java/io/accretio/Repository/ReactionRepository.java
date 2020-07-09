package io.accretio.Repository;

import io.accretio.Models.Message;
import io.accretio.Models.Reaction;
import io.accretio.Models.Room;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReactionRepository implements PanacheRepository<Reaction> {



    public Reaction checkUserReaction(Reaction reaction)
    {
       return find("message_id=?1 and user_id=?2",reaction.getMessage().getId(),reaction.getUser().getId()).firstResult();



    }


    public void updateReaction(long id,Reaction reaction)
    {

        update("type = ?1  where id = ?2", reaction.getType(),id);

    }








}
