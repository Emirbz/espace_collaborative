package io.accretio.Services;

import io.accretio.Models.Topic;
import io.accretio.Models.Tag;
import io.accretio.Repository.TagRepository;
import io.accretio.Repository.TopicRepository;

import javax.annotation.Nullable;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@ApplicationScoped
public class TopicService {

    @Inject
    TopicRepository topicRepository;

    @Inject
    TagRepository tagRepository;


    public List<Topic> getTopicsByTags(@Nullable List<Tag> tags)
    {
        assert tags != null;
        if (tags.size()==0) {

            return setCountRepliesList(topicRepository.getAllTopics());
         }
         else {
             Set<Topic> filtredTopics = new HashSet<>();
             tags.forEach(tag -> {
                 Tag newTag= tagRepository.findById(tag.getId());
                 filtredTopics.addAll(newTag.getTopics().stream().sorted(Comparator.comparing(Topic::getTimestamp)).collect(Collectors.toCollection(LinkedHashSet::new)));
             });
             return setCountRepliesList(new ArrayList<>(filtredTopics));
         }


    }
    public List<Topic> setCountRepliesList(List<Topic> topics)
    {
        for (Topic topic : topics) {
            setCountReplies(topic);
        }
        return topics;
    }



    public Topic setCountReplies(Topic topic)
    {
        topic.setCountReplies(topic.getReplies().size());
        return topic;

    }

    public void addTopic(Topic topic) {
        Set<Tag> tags = topic.getTags();
        Set<Tag> newSet = new HashSet<>();
        for (Tag tag : tags) {
            if (tag.getId() == 0) {
                if (!tagRepository.checkTagExists(tag.getName().toLowerCase())) {
                    tagRepository.persist(tag);
                }
                Tag newTag = tagRepository.getTagByName(tag.getName().toLowerCase());
                newSet.add(newTag);
            } else {
                newSet.add(tagRepository.findById(tag.getId()));
            }
        }
        topic.setTags(newSet);
        topicRepository.persist(topic);
    }

    public void deleteTopic(Topic topic) {
        topicRepository.delete(topic);

    }

    public Topic getTopicById(long id) {
        Topic topic = setCountReplies(topicRepository.findById(id));
        long seen = topic.getSeen();
        topicRepository.update("seen = ?1 where id = ?2", (seen+1),id);
        topic.setSeen(seen+1);
        return topic;
    }


    public void setInactive(Topic topic) {
        topicRepository.update("status = ?1 where id = ?2", Topic.Status.Inactive.name(), topic.getId());
    }


}