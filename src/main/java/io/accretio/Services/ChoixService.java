package io.accretio.Services;

import io.accretio.Models.Choix;
import io.accretio.Models.Room;
import io.accretio.Models.Sondage;
import io.accretio.Repository.ChoixRepository;
import io.accretio.Repository.SondageRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class ChoixService {


    @Inject
    private ChoixRepository choixRepository;


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
