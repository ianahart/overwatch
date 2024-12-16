package com.hart.overwatch.suggestion.response;

import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.suggestion.dto.SuggestionDto;

public class GetAllSuggestionsResponse {

    private String message;

    private PaginationDto<SuggestionDto> data;

    public GetAllSuggestionsResponse() {

    }

    public GetAllSuggestionsResponse(String message, PaginationDto<SuggestionDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public PaginationDto<SuggestionDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(PaginationDto<SuggestionDto> data) {
        this.data = data;
    }
}
