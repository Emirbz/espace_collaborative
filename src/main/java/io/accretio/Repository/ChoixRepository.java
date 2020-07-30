package io.accretio.Repository;

import javax.enterprise.context.ApplicationScoped;

import io.accretio.Models.Choix;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class ChoixRepository implements PanacheRepository<Choix> {


}