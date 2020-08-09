package io.accretio.Services;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import io.accretio.Config.LoggingFilter;
import io.accretio.Models.Choix;
import io.accretio.Models.Message;
import io.accretio.Models.User;
import io.accretio.Repository.ChoixRepository;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ChoixService {

    @Inject
    ChoixRepository choixRepository;

    @Inject
    SondageService sondageService;

    private static final Logger LOG = Logger.getLogger(LoggingFilter.class);


    public void addChoix(Choix choix) {

        choixRepository.persist(choix);

    }

    @Transactional
    public void addChoixEventBus(Integer choixId ,User user)
    {
        Choix choix = getSingleChoix(choixId);
        Message sondage = sondageService.getSigneSondage(choix.getMessage().getId());
        Set<Choix> choixSet = sondage.getChoix();
        System.out.println("one" + Arrays.toString(choixSet.toArray()));
        AtomicReference<User> userVoted = new AtomicReference<>();

        choixSet.forEach(c -> {
            c.getUsers().forEach(u -> {
                if (u.getId().equals(user.getId())) {
                    userVoted.set(u);
                }
            });
            if (userVoted.get() != null) {
                c.getUsers().remove(userVoted.get());
            }

        });

        choix.getUsers().add(user);
        LOG.info("Persiste choix from eventBus");

    }

    public List<Choix> getChoixBySondage(int id) {

        return choixRepository.find("sondage_id", id).list();
    }

    public Choix getSingleChoix(long id) {

        return choixRepository.findById(id);
    }

}
