package io.accretio.Services;

import io.accretio.Models.Reply;
import io.accretio.Models.Topic;
import io.accretio.Models.User;
import io.accretio.Repository.ReplyRepository;

import javax.annotation.Nullable;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;


@ApplicationScoped
public class ReplyService {

    @Inject
    ReplyRepository replyRepository;

    @Inject
    UserService userService;


    public List<Reply> getReplies() {
        return replyRepository.listAll();
    }


    public void addReply(Reply reply) {

        replyRepository.persist(reply);

    }


    public void deleteReply(Reply reply) {
        replyRepository.delete(reply);

    }


    public Reply getReplyById(long id) {
        return replyRepository.findById(id);
    }


    public List<Reply> getRepliesByTopic(Integer id) {
        List<Reply> replies = replyRepository.getRepliesByTopic(id);
        replies.forEach(reply -> reply.setTopic(null));
        return replies;
    }

    public Reply likeReply(Integer id, String userName) {
        User user = userService.findUserByUsername(userName);
        Reply reply = getReplyById(id);
        if (reply.getUsers().stream().anyMatch(user1 -> user1.getId().equals(user.getId()))) {
            reply.getUsers().remove(user);

        } else {
            reply.getUsers().add(user);
        }
        Reply.persist(reply);
        userService.setBadge(reply.getUser());
        return reply;
    }

    public List<Reply> getMyReplies(User user,@Nullable String name) {
        assert name != null;
        List<Reply> replies;
        if (name.length()==0) {
            replies =replyRepository.getMyReplies(user);
        }
        else
        {
            replies = replyRepository.searchMyReplies(user,name);
        }
        replies.forEach(reply -> reply.setTopic(new Topic(reply.getTopic().getId(), reply.getTopic().getTitle())));
        return replies;
    }

    public int countMyReplies(User user) {
        return getMyReplies(user, "").size();
    }

    public int countMyLikes(User user) {
        return (int) getReplies().stream().filter(reply -> reply.getUsers().contains(user)).count();
    }


    public int countSumUsersLike(User user) {
        return getMyReplies(user, "").stream().filter(reply -> reply.getUser().getId().equals(user.getId())).mapToInt(reply -> reply.getUsers().size()).sum();
    }
    public int countUsefulReplies(User user) {
        return  (int) getMyReplies(user, "").stream().filter(Reply::isUseful).count();
    }


    public void setUseful(Reply reply) {
        replyRepository.setInactive(reply);
        userService.setBadge(reply.getUser());

    }
}