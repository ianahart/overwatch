package com.hart.overwatch.label;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.Optional;
import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import com.hart.overwatch.activelabel.ActiveLabel;
import com.hart.overwatch.activelabel.ActiveLabelRepository;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.label.request.CreateLabelRequest;
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
}


