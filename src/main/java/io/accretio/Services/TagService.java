package io.accretio.Services;

import io.accretio.Models.Tag;
import io.accretio.Models.Topic;
import io.accretio.Repository.TagRepository;
import io.accretio.Repository.TopicRepository;

import javax.annotation.Nullable;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@ApplicationScoped
public class TagService {


    @Inject
    TagRepository tagRepository;

    public List<Tag> getAllTags() {
        List<Tag> tags = tagRepository.listAll();
       tags.forEach(tag -> tag.setCountTopics(tag.getTopics().size()));
       return tags;
    }

    public List<Tag> searchTag(String name) {
        List<Tag> tags = tagRepository.searchTagByName(name);
        tags.forEach(tag -> tag.setCountTopics(tag.getTopics().size()));
        return tags;
    }




}