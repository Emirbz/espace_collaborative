package io.accretio.Services;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.accretio.Models.Reaction;
import io.accretio.Repository.ReactionRepository;

@ApplicationScoped
public class ReactionService {

    @Inject
    private ReactionRepository reactionRepository;

    public void addReaction(Reaction reaction) {
        Reaction r = reactionRepository.checkUserReaction(reaction);

        if (r == null) {
            reactionRepository.persist(reaction);

        } else {
            reactionRepository.updateReaction(r.getId(), reaction);
        }

    }

    public List<Reaction> findByMessage(int id) {

        return reactionRepository.find("message_id", id).list();
    }

    public void deleteReaction(Reaction reaction) {
        reactionRepository.delete(reaction);

    }
    /*
     * public Reaction getUserReaction(Message message ,Reaction reaction) { return
     * reactionRepository.checkUserReaction() }
     */

}
