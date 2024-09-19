package com.hart.overwatch.customfield;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.customfield.request.CreateCustomFieldRequest;
import com.hart.overwatch.customfield.response.CreateCustomFieldResponse;
import com.hart.overwatch.customfieldmanagement.CustomFieldManagementService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/custom-fields")
public class CustomFieldController {

    private final CustomFieldManagementService customFieldManagementService;

    private final CustomFieldService customFieldService;

    @Autowired
    public CustomFieldController(CustomFieldManagementService customFieldManagementService,
            CustomFieldService customFieldService) {
        this.customFieldManagementService = customFieldManagementService;
        this.customFieldService = customFieldService;
    }


    @PostMapping("")
    public ResponseEntity<CreateCustomFieldResponse> createCustomField(
            @Valid @RequestBody CreateCustomFieldRequest request) {
        customFieldManagementService.handleCreateCustomField(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateCustomFieldResponse("success"));
    }

}
