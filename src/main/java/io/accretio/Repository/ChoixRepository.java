package io.accretio.Repository;

import io.accretio.Models.Choix;
import io.accretio.Models.Sondage;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ChoixRepository implements PanacheRepository<Choix> {


}