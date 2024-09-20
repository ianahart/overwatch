package com.hart.overwatch.dropdownoption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.dropdownoption.response.DeleteDropDownOptionResponse;

@RestController
@RequestMapping(path = "/api/v1/dropdown-options")
public class DropDownOptionController {

    private final DropDownOptionService dropDownOptionService;

    @Autowired
    public DropDownOptionController(DropDownOptionService dropDownOptionService) {
        this.dropDownOptionService = dropDownOptionService;
    }

    @DeleteMapping(path = "/{dropDownOptionId}")
    public ResponseEntity<DeleteDropDownOptionResponse> DeleteDropDownOption(
            @PathVariable("dropDownOptionId") Long dropDownOptionId) {
        dropDownOptionService.deleteDropDownOption(dropDownOptionId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new DeleteDropDownOptionResponse("success"));
    }
}
