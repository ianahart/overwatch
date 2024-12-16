package com.hart.overwatch.suggestion;

import java.util.HashMap;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.amazon.AmazonService;
import com.hart.overwatch.suggestion.request.CreateSuggestionRequest;
import com.hart.overwatch.advice.BadRequestException;

@Service
public class SuggestionService {

    private final String BUCKET_NAME = "arrow-date";

    private final SuggestionRepository suggestionRepository;

    private final UserService userService;

    private final AmazonService amazonService;

    @Autowired
    public SuggestionService(SuggestionRepository suggestionRepository, UserService userService,
            AmazonService amazonService) {
        this.suggestionRepository = suggestionRepository;
        this.userService = userService;
        this.amazonService = amazonService;
    }


    private Suggestion getSuggestionById(Long suggestionId) {
        return suggestionRepository.findById(suggestionId).orElseThrow(() -> new NotFoundException(
                String.format("Could not find a suggestion with the id %d")));
    }


    public void createSuggestion(CreateSuggestionRequest request) {
        User user = userService.getUserById(request.getUserId());

        String title = Jsoup.clean(request.getTitle(), Safelist.none());
        String description = Jsoup.clean(request.getDescription(), Safelist.none());
        String contact = Jsoup.clean(request.getContact(), Safelist.none());

        Suggestion newSuggestion = null;

        if (request.getAttachment() == null) {
            newSuggestion = new Suggestion(title, description, contact, request.getFeedbackType(),
                    request.getPriorityLevel(), FeedbackStatus.PENDING, user);
        } else {
            HashMap<String, String> result = this.amazonService.putS3Object(BUCKET_NAME,
                    request.getAttachment().getOriginalFilename(), request.getAttachment());
            String fileName = result.get("filename");
            String fileUrl = result.get("objectUrl");
            newSuggestion = new Suggestion(title, description, contact, fileName, fileUrl,
                    request.getFeedbackType(), request.getPriorityLevel(), FeedbackStatus.PENDING,
                    user);
        }
        suggestionRepository.save(newSuggestion);
    }

}
