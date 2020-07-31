package io.accretio.Services;

import io.accretio.Models.Reply;
import io.accretio.Models.Topic;
import io.accretio.Repository.ReplyRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;


@ApplicationScoped
public class ReplyService {

    @Inject
    ReplyRepository replyRepository;


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


    public List<Reply> getRepliesByTopic(Topic topic) {
        return replyRepository.list("topic", topic);
    }
}