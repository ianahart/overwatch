package com.hart.overwatch.activelabel;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hart.overwatch.activelabel.dto.ActiveLabelDto;
import com.hart.overwatch.activelabel.request.CreateActiveLabelRequest;
import com.hart.overwatch.label.Label;
import com.hart.overwatch.label.LabelService;
import com.hart.overwatch.todocard.TodoCard;
import com.hart.overwatch.todocard.TodoCardService;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.NotFoundException;

@Service
public class ActiveLabelService {


    private final TodoCardService todoCardService;

    private final LabelService labelService;

    private final ActiveLabelRepository activeLabelRepository;



    @Autowired
    public ActiveLabelService(TodoCardService todoCardService, LabelService labelService,
            ActiveLabelRepository activeLabelRepository) {
        this.todoCardService = todoCardService;
        this.labelService = labelService;
        this.activeLabelRepository = activeLabelRepository;
    }


    private ActiveLabel getActiveLabelById(Long activeLabelId) {
        return activeLabelRepository.findById(activeLabelId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Active label with id %d was not found", activeLabelId)));
    }

    public void createActiveLabel(CreateActiveLabelRequest request) {
        Long todoCardId = request.getTodoCardId();
        Long labelId = request.getLabelId();

        if (todoCardId == null || labelId == null) {
            throw new BadRequestException("Either todo card id is missing or label id is missing");
        }

        Label label = labelService.getLabelById(labelId);
        TodoCard todoCard = todoCardService.getTodoCardById(todoCardId);



        ActiveLabel activeLabel = new ActiveLabel(todoCard, label);


        activeLabelRepository.save(activeLabel);
    }


    public void deleteActiveLabel(Long todoCardId, Long labelId) {
        ActiveLabel activeLabel =
                activeLabelRepository.findByTodoCardIdAndLabelId(todoCardId, labelId);



        activeLabelRepository.delete(activeLabel);
    }

    public List<ActiveLabelDto> getActiveLabels(Long todoCardId) {
        return activeLabelRepository.getActiveLabels(todoCardId);
    }
}