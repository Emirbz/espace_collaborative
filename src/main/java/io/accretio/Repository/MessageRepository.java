package io.accretio.Repository;

import javax.enterprise.context.ApplicationScoped;

import io.accretio.Models.Message;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class MessageRepository implements PanacheRepository<Message> {

}
