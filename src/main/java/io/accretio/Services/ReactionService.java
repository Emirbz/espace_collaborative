package io.accretio.Services;

import io.accretio.Config.LoggingFilter;
import io.accretio.Models.Message;
import io.accretio.Models.Reaction;
import io.accretio.Models.Reaction;
import io.accretio.Repository.ReactionRepository;
import io.accretio.Repository.ReactionRepository;
import io.accretio.Utils.FileUploader;
import io.minio.errors.*;
import io.quarkus.panache.common.Sort;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@ApplicationScoped
public class ReactionService {


    @Inject
    private ReactionRepository reactionRepository;





    public void addReaction(Reaction reaction){
      Reaction r  = reactionRepository.checkUserReaction(reaction);

       if (r == null)
       {
           reactionRepository.persist(reaction);

       }
       else
       {
           reactionRepository.updateReaction(r.getId(),reaction);
       }





    }

       public List<Reaction> findByMessage(int id)
    {

        return reactionRepository.find("message_id",id).list();
    }


    public  void deleteReaction(Reaction reaction)
    {
        reactionRepository.delete(reaction);

    }
  /*  public Reaction getUserReaction(Message message ,Reaction reaction)
    {
        return reactionRepository.checkUserReaction()
    }*/



}
