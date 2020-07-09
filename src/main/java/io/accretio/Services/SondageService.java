package io.accretio.Services;

import io.accretio.Models.Choix;
import io.accretio.Models.Message;
import io.accretio.Models.Sondage;
import io.accretio.Repository.SondageRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class SondageService {


    @Inject
    private SondageRepository sondageRepository;


    public void addSondage(Message sondage) {

       Set<Choix> choix = sondage.getChoix();
        choix.forEach(c -> {
            c.setMessage(sondage);
            Choix.persist(c);
        });

        sondageRepository.persist(sondage);


    }

    public List<Message> findByRoom(int id) {

        return sondageRepository.findSondagesByRoom(id);
    }


    public void deleteSondage(Message Sondage) {
        sondageRepository.delete(Sondage);

    }

    public Message getSigneSondage(long id) {
        return sondageRepository.findById(id);
    }

}
