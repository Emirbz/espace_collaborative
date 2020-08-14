package io.accretio.Services;

import io.accretio.Models.Tag;
import io.accretio.Models.Topic;
import io.accretio.Models.User;
import io.accretio.Repository.TagRepository;
import io.accretio.Repository.TopicRepository;

import javax.annotation.Nullable;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;


@ApplicationScoped
public class TopicService {

    @Inject
    TopicRepository topicRepository;

    @Inject
    TagRepository tagRepository;


    public List<Topic> getTopicsByTags(@Nullable List<Tag> tags, @Nullable String name) {
        assert tags != null;
        assert name != null;

        if (tags.size() == 0) {
            List<Topic> topics = setCountRepliesList(topicRepository.getAllTopics());
            if (name.length() > 0) {
                topics.removeIf(topic -> !(topic.getTitle().toLowerCase().contains(name.toLowerCase())));
                return topics;
            }
            return topics;
        } else {
            Set<Topic> filtredTopics = new HashSet<>();
            Set<Tag> newTags = new HashSet<>();
            tags.stream().map(tag -> tagRepository.findById(tag.getId())).forEach(newTag -> {
                newTags.add(newTag);
                filtredTopics.addAll(newTag.getTopics().stream().sorted(Comparator.comparing(Topic::getTimestamp)).collect(Collectors.toCollection(LinkedHashSet::new)));
            });
            filtredTopics.removeIf(topic -> !(checkTopicExists(topic, newTags)));

            if (name.length() > 0) {
                filtredTopics.removeIf(topic -> !(topic.getTitle().toLowerCase().contains(name.toLowerCase())));
            }
            return setCountRepliesList(new ArrayList<>(filtredTopics));

        }


    }
    public List<Topic> getAllTopics()
    {
        return topicRepository.listAll();
    }

    public boolean checkTopicExists(Topic topic, Set<Tag> tags) {
        AtomicBoolean exists = new AtomicBoolean(true);
        tags.stream().filter(tag -> tag.getTopics().stream().noneMatch(topic1 -> topic1.getId() == topic.getId())).map(tag -> false).forEach(exists::set);
        return exists.get();

    }

    public List<Topic> setCountRepliesList(List<Topic> topics) {
        for (Topic topic : topics) {
            setCountReplies(topic);
        }
        return topics;
    }


    public Topic setCountReplies(Topic topic) {
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
        topicRepository.update("seen = ?1 where id = ?2", (seen + 1), id);
        topic.setSeen(seen + 1);
        return topic;
    }


    public void setInactive(Topic topic) {
        topicRepository.update("status = ?1 where id = ?2", Topic.Status.Inactive.name(), topic.getId());
    }

    public List<Topic> getMyTopics(User user,@Nullable String name) {
        assert name != null;
        if (name.length() == 0)
        {
            return  topicRepository.getMyTopics(user);
        }
        return topicRepository.searchMyTopics(user,name);

    }


    public int countMyTopics(User user) {
        return getMyTopics(user, "").size();

    }

    public List<Topic> getPoupularTopics() {

        return setCountRepliesList(getAllTopics()).stream().sorted(Comparator.comparing(Topic::getCountReplies).reversed()).collect(Collectors.toList());
    }
}