package io.accretio.Services;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.accretio.Models.Choix;
import io.accretio.Repository.ChoixRepository;

@ApplicationScoped
public class ChoixService {

    @Inject
    ChoixRepository choixRepository;

    public void addChoix(Choix choix) {

        choixRepository.persist(choix);

    }

    public List<Choix> getChoixBySondage(int id) {

        return choixRepository.find("sondage_id", id).list();
    }

    public Choix getSingleChoix(long id) {

        return choixRepository.findById(id);
    }

}
