package com.hart.overwatch.todocardmanagement;

import java.util.ArrayList;
import java.util.List;
import com.hart.overwatch.todocard.TodoCard;
import com.hart.overwatch.todocard.request.UpdateTodoCardRequest;

public class PropertyChangeLogger {

    public static List<String> getUpdatedProperties(TodoCard original,
            UpdateTodoCardRequest request) {
        List<String> updatedProperties = new ArrayList<>();

        if (request.getTitle() != null && !request.getTitle().equals(original.getTitle())) {
            updatedProperties.add("Title");
        }

        if (request.getDetails() != null && !request.getDetails().equals(original.getDetails())) {
            updatedProperties.add("Details");
        }

        if (request.getLabel() != null && !request.getLabel().equals(original.getLabel())) {
            updatedProperties.add("Label");
        }

        if (request.getColor() != null && !request.getColor().equals(original.getColor())) {
            updatedProperties.add("Color");
        }

        if (request.getIndex() != null && !request.getIndex().equals(original.getIndex())) {
            updatedProperties.add("Index");
        }

        if (request.getStartDate() != null
                && !request.getStartDate().equals(original.getStartDate())) {
            updatedProperties.add("Start Date");
        }

        if (request.getEndDate() != null && !request.getEndDate().equals(original.getEndDate())) {
            updatedProperties.add("End Date");
        }

        if (request.getPhoto() != null && !request.getPhoto().equals(original.getPhoto())) {
            updatedProperties.add("Photo");
        }

        if (request.getUploadPhotoUrl() != null
                && !request.getUploadPhotoUrl().equals(original.getUploadPhotoUrl())) {
            updatedProperties.add("Uploaded photo");
        }

        return updatedProperties;
    }

    public static String joinProperties(List<String> properties) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < properties.size(); i++) {
            String delimiter = i == properties.size() - 1 ? "" : ", ";
            sb.append(properties.get(i)).append(delimiter);
        }
        return sb.toString();
    }
}
