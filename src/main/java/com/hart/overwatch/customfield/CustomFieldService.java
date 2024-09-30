package com.hart.overwatch.customfield;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.customfield.dto.CustomFieldDto;
import com.hart.overwatch.customfield.request.CreateCustomFieldRequest;
import com.hart.overwatch.customfield.request.UpdateCustomFieldRequest;
import com.hart.overwatch.dropdownoption.DropDownOption;
import com.hart.overwatch.dropdownoption.dto.DropDownOptionDto;
import com.hart.overwatch.todocard.TodoCard;
import com.hart.overwatch.todocard.TodoCardService;

@Service
public class CustomFieldService {

    private final int MAX_CUSTOM_FIELDS = 10;

    private final CustomFieldRepository customFieldRepository;

    private final UserService userService;

    private final TodoCardService todoCardService;

    @Autowired
    public CustomFieldService(CustomFieldRepository customFieldRepository, UserService userService,
            TodoCardService todoCardService) {
        this.customFieldRepository = customFieldRepository;
        this.userService = userService;
        this.todoCardService = todoCardService;
    }

    public CustomField getCustomFieldById(Long customFieldId) {
        return customFieldRepository.findById(customFieldId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Could not find custom field with id %d", customFieldId)));
    }


    private long countCustomFieldsPerTodoCard(Long userId, Long todoCardId) {
        return customFieldRepository.countCustomFieldsPerTodoCard(userId, todoCardId);
    }

    private boolean alreadyExistsByFieldNameNotType(Long todoCard, String fieldName,
            String fieldType) {
        return customFieldRepository.alreadyExistsByFieldNameNotType(todoCard, fieldName,
                fieldType);
    }


    private DropDownOptionDto convertToChildDto(DropDownOption dropDownOption) {
        DropDownOptionDto dropDownOptionDto = new DropDownOptionDto();

        dropDownOptionDto.setId(dropDownOption.getId());
        dropDownOptionDto.setCustomFieldId(dropDownOption.getCustomField().getId());
        dropDownOptionDto.setOptionValue(dropDownOption.getOptionValue());

        return dropDownOptionDto;
    }

    private CustomFieldDto convertToDto(CustomField customField) {
        CustomFieldDto customFieldDto = new CustomFieldDto();
        customFieldDto.setId(customField.getId());
        customFieldDto.setUserId(customField.getUser().getId());
        customFieldDto.setTodoCardId(customField.getTodoCard().getId());
        customFieldDto.setFieldType(customField.getFieldType());
        customFieldDto.setFieldName(customField.getFieldName());
        customFieldDto.setSelectedValue(customField.getSelectedValue());
        customFieldDto.setIsActive(customField.getIsActive());

        if (customFieldDto.getFieldType().toUpperCase().equals("CHECKBOX")
                || customFieldDto.getFieldType().toUpperCase().equals("DROPDOWN")) {

            customFieldDto.setDropDownOptions(customField.getDropDownOptions().stream()
                    .map(this::convertToChildDto).collect(Collectors.toList()));
        }
        return customFieldDto;
    }

    public List<CustomFieldDto> getCustomFields(Long todoCardId, String isActiveParam) {
        if (todoCardId == null) {
            throw new BadRequestException("Missing todo card id parameter");
        }

        Boolean isActive = isActiveParam.equals("true") ? true : false;
        List<CustomField> customFields = new ArrayList<>();

        if (isActive) {
            customFields = customFieldRepository.findByTodoCardIdAndIsActive(todoCardId, isActive);
        } else {
            customFields = customFieldRepository.findByTodoCardId(todoCardId);
        }


        return customFields.stream().collect(Collectors.groupingBy(CustomField::getFieldType))
                .values().stream().flatMap(List::stream).map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public CustomField createCustomField(CreateCustomFieldRequest request) {
        if (request.getUserId() == null || request.getTodoCardId() == null) {
            throw new BadRequestException("Missing either userId or todoCardId in payload");
        }

        if (countCustomFieldsPerTodoCard(request.getUserId(),
                request.getTodoCardId()) >= MAX_CUSTOM_FIELDS) {
            throw new BadRequestException(
                    String.format("You have already added the maximum amount of custom fields (%d)",
                            MAX_CUSTOM_FIELDS));
        }
        String fieldType = Jsoup.clean(request.getFieldType(), Safelist.none());
        String fieldName = Jsoup.clean(request.getFieldName(), Safelist.none());
        String selectedValue = Jsoup.clean(request.getSelectedValue(), Safelist.none());


        if (alreadyExistsByFieldNameNotType(request.getTodoCardId(), fieldName, fieldType)) {
            throw new BadRequestException(
                    String.format("You already added a %s named %s", fieldType, fieldName));
        }

        User user = userService.getUserById(request.getUserId());
        TodoCard todoCard = todoCardService.getTodoCardById(request.getTodoCardId());


        CustomField customField = new CustomField();

        customField.setIsActive(false);
        customField.setFieldType(fieldType);
        customField.setFieldName(fieldName);
        customField.setSelectedValue(selectedValue);
        customField.setUser(user);
        customField.setTodoCard(todoCard);

        customFieldRepository.save(customField);

        return customField;
    }

    public void deleteCustomField(Long customFieldId) {
        CustomField customField = getCustomFieldById(customFieldId);
        User currentUser = userService.getCurrentlyLoggedInUser();

        if (!customField.getUser().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("Cannot delete a custom field that is not yours");
        }
        customFieldRepository.delete(customField);
    }

    public void updateCustomField(Long customFieldId, UpdateCustomFieldRequest request) {
        CustomField customField = getCustomFieldById(customFieldId);
        User currentUser = userService.getCurrentlyLoggedInUser();

        if (!currentUser.getId().equals(customField.getUser().getId())) {
            throw new ForbiddenException("Cannot update a custom field that is not yours");
        }

        customField.setIsActive(request.getIsActive());

        customFieldRepository.save(customField);
    }
}
