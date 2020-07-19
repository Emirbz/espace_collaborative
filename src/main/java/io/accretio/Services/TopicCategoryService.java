package io.accretio.Services;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.accretio.Models.TopicCategory;
import io.accretio.Repository.TopicCategoryRepository;

@ApplicationScoped
public class TopicCategoryService {

    @Inject
    private TopicCategoryRepository topicCategoryRepository;

    public List<TopicCategory> getCategories() {
        return topicCategoryRepository.listAll();
    }

    public TopicCategory getCategory(long id) {
        return topicCategoryRepository.findById(id);
    }

}