package io.accretio.Services;

import io.accretio.Config.LoggingFilter;
import io.accretio.Models.User;
import io.accretio.Repository.UserRepository;
import org.jboss.logging.Logger;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;



@ApplicationScoped
public class UserService {



    @Inject
    private UserRepository userRepository;


    public List<User> getUser() {
        return userRepository.listAll();
    }

    @Transactional
    public void addUser(User user) {

        userRepository.persist(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);

    }

    public User findUserByUsername(String username)
    {
        System.out.println(username);
        return userRepository.find("username", username).firstResult();
    }

    public void updateUser(String id, User user) {
        userRepository.updateUser(id, user);


    }

    public List<User> findUserByName(String searchToken)
    {
        return  userRepository.findUserByName(searchToken);
    }


    public User getSigneUser(String id) {
        return userRepository.findById(id);
    }
}
