package io.accretio.Services;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import io.accretio.Config.LoggingFilter;
import io.accretio.Models.Message;
import io.accretio.Models.Reaction;
import io.accretio.Repository.ReactionRepository;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ReactionService {

    @Inject
    ReactionRepository reactionRepository;

    private static final Logger LOG = Logger.getLogger(LoggingFilter.class);


    public void addReaction(Reaction reaction) {
        Reaction r = reactionRepository.checkUserReaction(reaction);

        if (r == null) {
            reactionRepository.persist(reaction);

        } else {
            reactionRepository.updateReaction(r.getId(), reaction);
        }

    }


    @Transactional
    public void addReactionEventBus(Reaction reaction , Message message) {
        LOG.info("Reaction persisted from eventBus");
        reaction.setMessage(message);

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
