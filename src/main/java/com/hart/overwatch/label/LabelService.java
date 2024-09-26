package com.hart.overwatch.label;

import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.hart.overwatch.label.dto.LabelDto;
import com.hart.overwatch.label.request.CreateLabelRequest;
import com.hart.overwatch.label.request.UpdateLabelRequest;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.workspace.WorkSpace;
import com.hart.overwatch.workspace.WorkSpaceService;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.activelabel.ActiveLabelRepository;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.NotFoundException;


@Service
public class LabelService {

    private final int LABEL_QUANITTY_PER_WORKSPACE = 8;

    private final WorkSpaceService workSpaceService;

    private final UserService userService;

    private final LabelRepository labelRepository;

    private final ActiveLabelRepository activeLabelRepository;

    @Autowired
    public LabelService(WorkSpaceService workSpaceService, UserService userService,
            LabelRepository labelRepository, ActiveLabelRepository activeLabelRepository) {
        this.workSpaceService = workSpaceService;
        this.userService = userService;
        this.labelRepository = labelRepository;
        this.activeLabelRepository = activeLabelRepository;
    }

    public Label getLabelById(Long labelId) {
        return labelRepository.findById(labelId).orElseThrow(() -> new NotFoundException(
                String.format("Label with id %d was not found", labelId)));
    }

    private boolean labelExistsInWorkSpace(String color, String title, Long workSpaceId) {
        return labelRepository.labelExistsInWorkSpace(color, title, workSpaceId);
    }

    private long countLabelsInWorkSpace(Long workSpaceId) {
        return labelRepository.countLabelsInWorkSpace(workSpaceId);
    }

    public void createLabel(CreateLabelRequest request) {
        Long workSpaceId = request.getWorkSpaceId();

        WorkSpace workSpace = workSpaceService.getWorkSpaceById(workSpaceId);
        User user = userService.getUserById(request.getUserId());

        if (!user.getId().equals(workSpace.getUser().getId())) {
            throw new ForbiddenException("Cannot add a label to another user's workspace");
        }

        String color = Jsoup.clean(request.getColor(), Safelist.none());
        String title = Jsoup.clean(request.getTitle(), Safelist.none()).toLowerCase();

        if (labelExistsInWorkSpace(color, title, workSpaceId)) {
            throw new BadRequestException(
                    "You have already added a label with that title or color in this workspace");
        }

        if (countLabelsInWorkSpace(workSpaceId) > LABEL_QUANITTY_PER_WORKSPACE) {
            throw new BadRequestException(String.format(
                    "You have added the maximum amount of labels of (%d) for this workspace",
                    LABEL_QUANITTY_PER_WORKSPACE));
        }

        Boolean isChecked = false;
        Label label = new Label(isChecked, title, color, user, workSpace);

        labelRepository.save(label);
    }


    public List<LabelDto> getLabels(Long workSpaceId) {

        Pageable pageable = PageRequest.of(0, LABEL_QUANITTY_PER_WORKSPACE);

        Page<LabelDto> result = labelRepository.getLabelsByWorkSpaceId(workSpaceId, pageable);

        return result.getContent();
    }

    public void deleteLabel(Long labelId) {
        Label label = getLabelById(labelId);
        Long currentUserId = userService.getCurrentlyLoggedInUser().getId();

        if (!label.getUser().getId().equals(currentUserId)) {
            throw new ForbiddenException("Cannot delete a label that is not yours");
        }

        label.getActiveLabels().forEach(al -> {
            activeLabelRepository.delete(al);
        });

        labelRepository.delete(label);
    }

    public LabelDto updateLabel(Long labelId, UpdateLabelRequest request) {
        Label label = getLabelById(labelId);
        label.setIsChecked(request.getIsChecked());

        labelRepository.save(label);

        return new LabelDto(label.getId(), label.getWorkSpace().getId(), label.getUser().getId(),
                label.getCreatedAt(), label.getIsChecked(), label.getTitle(), label.getColor());

    }
}
