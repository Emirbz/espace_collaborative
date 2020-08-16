package io.accretio.Services;

import java.util.List;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import io.accretio.Config.LoggingFilter;
import io.accretio.Models.Choix;
import io.accretio.Models.Message;
import io.accretio.Repository.SondageRepository;
import org.jboss.logging.Logger;

@ApplicationScoped
public class SondageService {

    @Inject
    SondageRepository sondageRepository;

    private static final Logger LOG = Logger.getLogger(LoggingFilter.class);


    public void addSondage(Message sondage) {

        Set<Choix> choix = sondage.getChoix();
        choix.forEach(c -> {
            c.setMessage(sondage);
            Choix.persist(c);
        });

        sondageRepository.persist(sondage);

    }

    @Transactional
    public Message addSondageEventBus(Message sondage) {



        Set<Choix> choix = sondage.getChoix();
        choix.forEach(c -> {
            c.setMessage(sondage);
            Choix.persist(c);
        });

        sondageRepository.persist(sondage);
        LOG.info("Sondage Persisted from evnetBus");

        return  sondage;

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
