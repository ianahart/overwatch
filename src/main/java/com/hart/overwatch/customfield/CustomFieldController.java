package com.hart.overwatch.customfield;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.customfield.request.CreateCustomFieldRequest;
import com.hart.overwatch.customfield.request.UpdateCustomFieldRequest;
import com.hart.overwatch.customfield.response.CreateCustomFieldResponse;
import com.hart.overwatch.customfield.response.DeleteCustomFieldResponse;
import com.hart.overwatch.customfield.response.GetCustomFieldsResponse;
import com.hart.overwatch.customfield.response.UpdateCustomFieldResponse;
import com.hart.overwatch.customfieldmanagement.CustomFieldManagementService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/")
public class CustomFieldController {

    private final CustomFieldManagementService customFieldManagementService;

    private final CustomFieldService customFieldService;

    @Autowired
    public CustomFieldController(CustomFieldManagementService customFieldManagementService,
            CustomFieldService customFieldService) {
        this.customFieldManagementService = customFieldManagementService;
        this.customFieldService = customFieldService;
    }


    @PostMapping("/custom-fields")
    public ResponseEntity<CreateCustomFieldResponse> createCustomField(
            @Valid @RequestBody CreateCustomFieldRequest request) {
        customFieldManagementService.handleCreateCustomField(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateCustomFieldResponse("success"));
    }

    @GetMapping("todo-cards/{todoCardId}/custom-fields")
    public ResponseEntity<GetCustomFieldsResponse> getCustomFields(
            @PathVariable("todoCardId") Long todoCardId,
            @RequestParam(value = "isActive", defaultValue = "false") String isActive) {
        return ResponseEntity.status(HttpStatus.OK).body(new GetCustomFieldsResponse("success",
                customFieldService.getCustomFields(todoCardId, isActive)));
    }

    @DeleteMapping("/custom-fields/{customFieldId}")
    public ResponseEntity<DeleteCustomFieldResponse> deleteCustomField(
            @PathVariable("customFieldId") Long customFieldId) {
        customFieldService.deleteCustomField(customFieldId);
        return ResponseEntity.status(HttpStatus.OK).body(new DeleteCustomFieldResponse("success"));
    }

    @PatchMapping("/custom-fields/{customFieldId}")
    public ResponseEntity<UpdateCustomFieldResponse> updateCustomField(


            @PathVariable("customFieldId") Long customFieldId,
            @RequestBody UpdateCustomFieldRequest request) {
        customFieldService.updateCustomField(customFieldId, request);
        return ResponseEntity.status(HttpStatus.OK).body(new UpdateCustomFieldResponse("success"));
    }

}
