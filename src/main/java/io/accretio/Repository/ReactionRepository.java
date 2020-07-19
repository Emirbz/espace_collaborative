package io.accretio.Repository;

import javax.enterprise.context.ApplicationScoped;

import io.accretio.Models.Reaction;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

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
