package com.hart.overwatch.customfieldmanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hart.overwatch.customfield.CustomField;
import com.hart.overwatch.customfield.CustomFieldService;
import com.hart.overwatch.customfield.request.CreateCustomFieldRequest;
import com.hart.overwatch.dropdownoption.DropDownOptionService;
import com.hart.overwatch.activity.ActivityService;
import com.hart.overwatch.advice.BadRequestException;

@Service
public class CustomFieldManagementService {

    private final CustomFieldService customFieldService;

    private final DropDownOptionService dropDownOptionService;

    private final ActivityService activityService;


    @Autowired
    public CustomFieldManagementService(CustomFieldService customFieldService,
            DropDownOptionService dropDownOptionService, ActivityService activityService) {
        this.customFieldService = customFieldService;
        this.dropDownOptionService = dropDownOptionService;
        this.activityService = activityService;
    }

    @Transactional
    public void handleCreateCustomField(CreateCustomFieldRequest request) {
        CustomField customField = customFieldService.createCustomField(request);

        if (customField == null) {
            throw new BadRequestException("Something went wrong. Please try again.");
        }

        if (request.getDropDownOptions().size() > 0) {
            dropDownOptionService.createDropDownOptions(request.getDropDownOptions(), customField);
        }

        String text = String.format("You added a %s custom field called %s",
                customField.getFieldType(), customField.getFieldName());
        activityService.createActivity(text, customField.getTodoCard().getId(),
                customField.getUser().getId());

    }
}
