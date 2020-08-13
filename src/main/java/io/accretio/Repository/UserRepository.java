package io.accretio.Repository;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import io.accretio.Models.Topic;
import io.accretio.Models.User;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;


@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<User,String> {

    public void updateUser(String id , User user)
    {
        update("firstName = ?1 , lastName=?2,email =?3 where id = ?4",user.firstName,user.lastName, user.email, id);

    }

    public List<User> findUserByName(String searchToken)
    {
        return find("firstName Like concat('%', ?1, '%')",searchToken).list();
    }


}
