package io.accretio.Services;

import io.accretio.Models.Reply;
import io.accretio.Models.User;
import io.accretio.Repository.ReplyRepository;

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
        return replyRepository.getRepliesByTopic(id);
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
        return reply;
    }
}