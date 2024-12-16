package com.hart.overwatch.suggestion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.suggestion.request.CreateSuggestionRequest;
import com.hart.overwatch.suggestion.response.CreateSuggestionResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1")
public class SuggestionController {

    private final SuggestionService suggestionService;

    @Autowired
    public SuggestionController(SuggestionService suggestionService) {
        this.suggestionService = suggestionService;
    }

    @PostMapping("/suggestions")
    public ResponseEntity<CreateSuggestionResponse> createSuggestion(
            @Valid @ModelAttribute CreateSuggestionRequest request) {
        suggestionService.createSuggestion(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateSuggestionResponse("success"));
    }
}
