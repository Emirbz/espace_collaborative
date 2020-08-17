package io.accretio.Services;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import io.accretio.Models.User;
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
        int countLikes = replyService.countMyLikes(user);
        JsonObject response = new JsonObject();
        response.put("countTopics", countTopics);
        response.put("countReplies", countReplies);
        response.put("countLikes",countLikes);
        response.put("progress",getBadgeProgress(user));

        return  response;

    }

    public double getUserScore(User user)
    {
        int countSumUsersLike = replyService.countSumUsersLike(user);
        int countUsefulReplies = replyService.countUsefulReplies(user);
        return countSumUsersLike*0.5+3*countUsefulReplies;

    }

    public int getBadge(User user){
        double score = getUserScore(user);
        if  (score < 3)
        {
         return 1;
        }
        if (score >3 && score <=8) {
            return 2;

        }
        if (score > 8 && score <= 18) {
            return 3;

        }

        if (score > 18 && score <= 30) {
            return 4;

        }
        if (score > 30 && score <= 45) {
            return 5;

        }
        if (score > 45 && score <= 65) {
            return 6;

        }
        if (score > 65 && score <= 100) {
            return 7;

        }

        return 8;



    }
    public double getBadgeProgress(User user) {
        double score = getUserScore(user);
        int badgeId = getBadge(user);
        double progress =0;
        switch (badgeId) {
            case 1:
                progress = ((score) / 3) * 100;
                break;
            case 2:
                progress = ((score - 3) / 5) * 100;
                break;
            case 3:
                progress = ((score - 8) / 10) * 100;
                break;
            case 4:
                progress = ((score - 18) / 12) * 100;
                break;
            case 5:
                progress = ((score - 30) / 15) * 100;
                break;
            case 6:
                progress = ((score - 45) / 15) * 100;
                break;
            case 7:
                progress = ((score - 65) / 35) * 100;
                break;
            case 8:
                progress = 100;
                break;
        }
        return progress;




    }


    @Transactional
    public void setBadge(User user) {

        int badgeId = getBadge(user);
        if (user.getBadge().getId() !=badgeId)
        {userRepository.updateBadge(user, badgeId);}
    }


}
