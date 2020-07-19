package io.accretio.Repository;


import io.accretio.Models.Reply;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReplyRepository implements PanacheRepository<Reply> {

}