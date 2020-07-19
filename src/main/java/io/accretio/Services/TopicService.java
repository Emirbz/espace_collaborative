package io.accretio.Services;

import io.accretio.Models.Topic;
import io.accretio.Models.TopicCategory;
import io.accretio.Repository.TopicRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;


@ApplicationScoped
public class TopicService {

    @Inject
    private TopicRepository topicRepository;

    public List<Topic> getTopic() {
        return topicRepository.listAll();
    }

    public void addTopic(Topic topic) {
        topicRepository.persist(topic);
    }

    public void deleteTopic(Topic topic) {
        topicRepository.delete(topic);

    }

    public Topic getTopicById(long id) {
        return topicRepository.findById(id);
    }

    public List<Topic> getTopicsByCategory(TopicCategory topicCategory) {
        return topicRepository.list("topicCategory", topicCategory);
    }

    public void setInactive(Topic topic){
        topicRepository.update("status = ?1 where id = ?2", Topic.Status.Inactive.name(),topic.getId());
    }
}