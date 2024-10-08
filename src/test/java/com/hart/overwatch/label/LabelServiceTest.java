package com.hart.overwatch.label;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.Optional;
import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import com.hart.overwatch.activelabel.ActiveLabel;
import com.hart.overwatch.activelabel.ActiveLabelRepository;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.label.dto.LabelDto;
import com.hart.overwatch.label.request.CreateLabelRequest;
import com.hart.overwatch.label.request.UpdateLabelRequest;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.workspace.WorkSpace;
import com.hart.overwatch.workspace.WorkSpaceService;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class LabelServiceTest {

    @InjectMocks
    private LabelService labelService;

    @Mock
    private UserService userService;

    @Mock
    WorkSpaceService workSpaceService;

    @Mock
    LabelRepository labelRepository;

    @Mock
    ActiveLabelRepository activeLabelRepository;

    private User user;

    private WorkSpace workSpace;

    private List<Label> labels;

    @BeforeEach
    public void setUp() {
        user = generateUser();
        workSpace = generatedWorkSpace(user);
        labels = generateLabels(user, workSpace);
    }

    private WorkSpace generatedWorkSpace(User user) {
        WorkSpace workSpace = new WorkSpace();
        workSpace.setId(1L);
        workSpace.setTitle("main");
        workSpace.setBackgroundColor("#000000");
        workSpace.setUser(user);

        return workSpace;
    }

    private User generateUser() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setFullName("John Doe");
        user.setEmail("john@mail.com");
        user.setRole(Role.USER);
        user.setLoggedIn(true);
        user.setProfile(new Profile());
        user.setPassword("Test12345%");
        user.setSetting(new Setting());

        return user;
    }

    private List<Label> generateLabels(User user, WorkSpace workSpace) {
        LocalDateTime timestamp = LocalDateTime.now();
        String[] titles = new String[] {"priority", "warning"};
        List<Label> labels = new ArrayList<>();
        int toGenerate = 2;
        for (int i = 1; i <= toGenerate; i++) {
            Label label = new Label();
            label.setId(Long.valueOf(i));
            label.setUser(user);
            label.setWorkSpace(workSpace);
            label.setColor("#000000");
            label.setTitle(titles[i - 1]);
            label.setIsChecked(false);
            label.setCreatedAt(timestamp);
            label.setUpdatedAt(timestamp);
            List<ActiveLabel> activeLabels = new ArrayList<>();
            ActiveLabel activeLabel = new ActiveLabel();
            activeLabel.setId(1L);
            activeLabels.add(activeLabel);
            label.setActiveLabels(activeLabels);
            labels.add(label);
        }
        return labels;
    }

    @Test
    public void LabelService_GetLabelById_ThrowNotFoundException() {
        Long nonExistentLabelId = 999L;
        when(labelRepository.findById(nonExistentLabelId)).thenReturn(Optional.ofNullable(null));

        Assertions.assertThatThrownBy(() -> {
            labelService.getLabelById(nonExistentLabelId);
        }).isInstanceOf(NotFoundException.class)
                .hasMessage(String.format("Label with id %d was not found", nonExistentLabelId));
    }

    @Test
    public void LabelService_GetLabelById_ReturnLabel() {
        Label label = labels.get(0);
        when(labelRepository.findById(label.getId())).thenReturn(Optional.of(label));

        Label returnedLabel = labelService.getLabelById(label.getId());

        Assertions.assertThat(returnedLabel).isNotNull();
        Assertions.assertThat(returnedLabel.getId()).isEqualTo(label.getId());
        Assertions.assertThat(returnedLabel.getIsChecked()).isEqualTo(label.getIsChecked());
        Assertions.assertThat(returnedLabel.getColor()).isEqualTo(label.getColor());
        Assertions.assertThat(returnedLabel.getTitle()).isEqualTo(label.getTitle());
    }

    @Test
    public void LabelService_CreateLabel_ThrowForbiddenException() {
        User forbiddenUser = new User();
        forbiddenUser.setId(2L);
        CreateLabelRequest request = new CreateLabelRequest(forbiddenUser.getId(),
                workSpace.getId(), "priority", "#000000");

        when(workSpaceService.getWorkSpaceById(request.getWorkSpaceId())).thenReturn(workSpace);
        when(userService.getUserById(request.getUserId())).thenReturn(forbiddenUser);

        Assertions.assertThatThrownBy(() -> {
            labelService.createLabel(request);

        }).isInstanceOf(ForbiddenException.class)
                .hasMessage("Cannot add a label to another user's workspace");
    }

    @Test
    public void LabelService_CreateLabel_ThrowBadRequestExceptionExists() {
        CreateLabelRequest request =
                new CreateLabelRequest(user.getId(), workSpace.getId(), "priority", "#000000");

        when(workSpaceService.getWorkSpaceById(request.getWorkSpaceId())).thenReturn(workSpace);
        when(userService.getUserById(request.getUserId())).thenReturn(user);

        when(labelRepository.labelExistsInWorkSpace(request.getColor(), request.getTitle(),
                workSpace.getId())).thenReturn(true);

        Assertions.assertThatThrownBy(() -> {
            labelService.createLabel(request);
        }).isInstanceOf(BadRequestException.class).hasMessage(
                "You have already added a label with that title or color in this workspace");
    }

    @Test
    public void LabelService_CreateLabel_ThrowBadRequestExceptionMaxLabels() {
        int LABEL_QUANTITY_PER_WORKSPACE = 8;
        CreateLabelRequest request =
                new CreateLabelRequest(user.getId(), workSpace.getId(), "important", "#0000FF");

        when(workSpaceService.getWorkSpaceById(request.getWorkSpaceId())).thenReturn(workSpace);
        when(userService.getUserById(request.getUserId())).thenReturn(user);

        when(labelRepository.labelExistsInWorkSpace(request.getColor(), request.getTitle(),
                workSpace.getId())).thenReturn(false);

        when(labelRepository.countLabelsInWorkSpace(workSpace.getId()))
                .thenReturn(Long.valueOf(LABEL_QUANTITY_PER_WORKSPACE + 1));

        Assertions.assertThatThrownBy(() -> {
            labelService.createLabel(request);
        }).isInstanceOf(BadRequestException.class)
                .hasMessage(String.format(
                        "You have added the maximum amount of labels of (%d) for this workspace",
                        LABEL_QUANTITY_PER_WORKSPACE));
    }

    @Test
    public void LabelService_CreateLabel_ReturnNothing() {
        int LABEL_QUANTITY_PER_WORKSPACE = 8;
        CreateLabelRequest request =
                new CreateLabelRequest(user.getId(), workSpace.getId(), "important", "#0000FF");

        when(workSpaceService.getWorkSpaceById(workSpace.getId())).thenReturn(workSpace);
        when(userService.getUserById(user.getId())).thenReturn(user);

        when(labelRepository.labelExistsInWorkSpace(request.getColor(), request.getTitle(),
                workSpace.getId())).thenReturn(false);

        when(labelRepository.countLabelsInWorkSpace(workSpace.getId()))
                .thenReturn(Long.valueOf(LABEL_QUANTITY_PER_WORKSPACE - 1));

        Label newLabel = new Label();
        newLabel.setId(3L);
        newLabel.setTitle(request.getTitle());
        newLabel.setColor(request.getColor());
        newLabel.setIsChecked(false);
        newLabel.setUser(user);
        newLabel.setWorkSpace(workSpace);
        when(labelRepository.save(any(Label.class))).thenReturn(newLabel);

        labelService.createLabel(request);

        verify(labelRepository, times(1)).save(any(Label.class));
    }

    public void LabelService_GetLabels_ReturnListOfLabelDtos() {
        int LABEL_QUANTITY_PER_WORKSPACE = 8;
        Pageable pageable = PageRequest.of(0, LABEL_QUANTITY_PER_WORKSPACE);

        LabelDto labelDto = new LabelDto();
        Page<LabelDto> pageResult =
                new PageImpl<>(Collections.singletonList(labelDto), pageable, 1);

        when(labelRepository.getLabelsByWorkSpaceId(workSpace.getId(), pageable))
                .thenReturn(pageResult);

        List<LabelDto> result = labelService.getLabels(workSpace.getId());

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void LabelService_DeleteLabel_ThrowForbiddenException() {
        User forbiddenUser = new User();
        forbiddenUser.setId(3L);
        Label label = labels.get(0);

        when(userService.getCurrentlyLoggedInUser()).thenReturn(forbiddenUser);
        when(labelRepository.findById(label.getId())).thenReturn(Optional.of(label));

        Assertions.assertThatThrownBy(() -> {
            labelService.deleteLabel(label.getId());
        }).isInstanceOf(ForbiddenException.class)
                .hasMessage("Cannot delete a label that is not yours");
    }

    @Test
    public void LabelService_DeleteLabel_ReturnNothing() {
        Label label = labels.get(0);

        when(labelRepository.findById(label.getId())).thenReturn(Optional.of(label));
        when(userService.getCurrentlyLoggedInUser()).thenReturn(user);

        doNothing().when(activeLabelRepository).delete(label.getActiveLabels().get(0));
        doNothing().when(labelRepository).delete(label);

        labelService.deleteLabel(label.getId());

        verify(activeLabelRepository, times(1)).delete(label.getActiveLabels().get(0));
        verify(labelRepository, times(1)).delete(label);
    }

    @Test
    public void LabelService_UpdateLabel_ReturnLabelDto() {
        Label label = labels.get(0);
        UpdateLabelRequest request = new UpdateLabelRequest(true);

        when(labelRepository.findById(label.getId())).thenReturn(Optional.of(label));
        when(labelRepository.save(any(Label.class))).thenReturn(label);

        LabelDto labelDto = labelService.updateLabel(label.getId(), request);

        Assertions.assertThat(labelDto).isNotNull();
        Assertions.assertThat(labelDto.getId()).isEqualTo(label.getId());

        verify(labelRepository, times(1)).save(any(Label.class));

        Assertions.assertThat(labelDto.getIsChecked()).isTrue();
    }
}

