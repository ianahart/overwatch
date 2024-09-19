package com.hart.overwatch.customfield;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.customfield.request.CreateCustomFieldRequest;
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

    public CustomField createCustomField(CreateCustomFieldRequest request) {
        if (request.getUserId() == null || request.getTodoCardId() == null) {
            throw new BadRequestException("Missing either userId or todoCardId in payload");
        }

        if (countCustomFieldsPerTodoCard(request.getUserId(),
                request.getTodoCardId()) >= MAX_CUSTOM_FIELDS) {
            throw new BadRequestException(String.format(
                    "You have alreadya added the maximum amount of custom fields (%d)",
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

        customField.setFieldType(fieldType);
        customField.setFieldName(fieldName);
        customField.setSelectedValue(selectedValue);
        customField.setUser(user);
        customField.setTodoCard(todoCard);

        customFieldRepository.save(customField);

        return customField;
    }

}
