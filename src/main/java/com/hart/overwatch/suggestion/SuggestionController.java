package com.hart.overwatch.suggestion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.suggestion.request.CreateSuggestionRequest;
import com.hart.overwatch.suggestion.request.UpdateSuggestionRequest;
import com.hart.overwatch.suggestion.response.CreateSuggestionResponse;
import com.hart.overwatch.suggestion.response.DeleteSuggestionResponse;
import com.hart.overwatch.suggestion.response.GetAllSuggestionsResponse;
import com.hart.overwatch.suggestion.response.UpdateSuggestionResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1")
public class SuggestionController {

    private final SuggestionService suggestionService;

    @Autowired
    public SuggestionController(SuggestionService suggestionService) {
        this.suggestionService = suggestionService;
    }

    @PostMapping(path = "/suggestions")
    public ResponseEntity<CreateSuggestionResponse> createSuggestion(
            @Valid @ModelAttribute CreateSuggestionRequest request) {
        suggestionService.createSuggestion(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateSuggestionResponse("success"));
    }

    @GetMapping(path = "/admin/suggestions")
    public ResponseEntity<GetAllSuggestionsResponse> getAllSuggestions(
            @RequestParam("feedbackStatus") FeedbackStatus feedbackStatus,
            @RequestParam("page") int page, @RequestParam("pageSize") int pageSize,
            @RequestParam("direction") String direction) {
        return ResponseEntity.status(HttpStatus.OK).body(new GetAllSuggestionsResponse("success",
                suggestionService.getAllSuggestions(feedbackStatus, page, pageSize, direction)));
    }

    @PatchMapping(path = "/admin/suggestions/{suggestionId}")
    public ResponseEntity<UpdateSuggestionResponse> updateSuggestion(
            @PathVariable("suggestionId") Long suggestionId,
            @RequestBody UpdateSuggestionRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(new UpdateSuggestionResponse("success",
                suggestionService.updateSuggestion(request, suggestionId)));
    }

    @DeleteMapping(path = "/admin/suggestions/{suggestionId}")
    public ResponseEntity<DeleteSuggestionResponse> deleteSuggestion(
            @PathVariable("suggestionId") Long suggestionId) {
        suggestionService.deleteSuggestion(suggestionId);
        return ResponseEntity.status(HttpStatus.OK).body(new DeleteSuggestionResponse("success"));
    }
}
