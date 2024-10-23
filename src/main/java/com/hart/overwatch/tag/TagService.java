package com.hart.overwatch.tag;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hart.overwatch.topic.Topic;
import com.hart.overwatch.topic.TopicRepository;
import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    private final TagRepository tagRepository;

    private final TopicRepository topicRepository;

    @Autowired
    public TagService(TagRepository tagRepository, TopicRepository topicRepository) {
        this.tagRepository = tagRepository;
        this.topicRepository = topicRepository;
    }


    public void createTags(List<String> tags, Topic topic) {

        for (String tag : tags) {
            String cleanedTag = Jsoup.clean(tag, Safelist.none());
            Optional<Tag> existingTag = tagRepository.findByName(cleanedTag);

            Tag tagEntity;
            if (existingTag.isPresent()) {
                tagEntity = existingTag.get();
            } else {
                tagEntity = new Tag(cleanedTag);
                tagRepository.save(tagEntity);
            }

            if (!topic.getTags().contains(tagEntity)) {
                topic.getTags().add(tagEntity);
                topicRepository.save(topic);
            }
        }
    }

}
