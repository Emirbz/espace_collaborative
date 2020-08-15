package io.accretio.Services;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import io.accretio.Models.Topic;
import io.accretio.Models.User;
import io.accretio.Repository.TopicRepository;
import io.accretio.Repository.UserRepository;
import io.vertx.core.json.JsonObject;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;
    @Inject
    TopicService topicService;
    @Inject
    ReplyService replyService;

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

    public User findUserByUsername(String username) {
        System.out.println(username);
        return userRepository.find("username", username).firstResult();
    }


    @Transactional
    public User findUserById(String id) {
        System.out.println(id);

        return userRepository.findById(id);
    }

    public void updateUser(String id, User user) {
        userRepository.updateUser(id, user);

    }

    public List<User> findUserByName(String searchToken) {
        return userRepository.findUserByName(searchToken);
    }

    public User getSigneUser(String id) {
        return userRepository.findById(id);
    }


    public JsonObject getUserStats(User user) {
        int countTopics = topicService.countMyTopics(user);
        int countReplies = replyService.countMyReplies(user);
        int countReactions = replyService.countMyReactions(user);
        int countUsefulReplies = replyService.countUsefulReplies(user);
        JsonObject response = new JsonObject();
        response.put("countTopics",countTopics);
        response.put("countReplies",countReplies);
        response.put("countReactions",countReactions);
        response.put("countUsefulReplies",countUsefulReplies);
        return  response;

    }
}
