package io.accretio.Repository;

import io.accretio.Models.Message;
import io.accretio.Models.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class MessageRepository implements PanacheRepository<Message> {



}
