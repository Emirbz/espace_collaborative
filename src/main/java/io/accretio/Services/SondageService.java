package io.accretio.Services;

import java.util.List;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.accretio.Models.Choix;
import io.accretio.Models.Message;
import io.accretio.Repository.SondageRepository;

@ApplicationScoped
public class SondageService {

    @Inject
    SondageRepository sondageRepository;

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
