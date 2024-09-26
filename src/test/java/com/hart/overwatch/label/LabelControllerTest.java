package com.hart.overwatch.label;

import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import com.hart.overwatch.activelabel.ActiveLabel;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.label.dto.LabelDto;
import com.hart.overwatch.label.request.CreateLabelRequest;
import com.hart.overwatch.label.request.UpdateLabelRequest;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.token.TokenRepository;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.workspace.WorkSpace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.hamcrest.CoreMatchers;


@ActiveProfiles("test")
@WebMvcTest(controllers = LabelController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class LabelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LabelService labelService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private TokenRepository tokenRepository;

    @Autowired
    private ObjectMapper objectMapper;

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
    public void LabelController_CreateLabel_ReturnCreateLabelResponse() throws Exception {
        CreateLabelRequest request =
                new CreateLabelRequest(user.getId(), workSpace.getId(), "important", "#0000FF");

        doNothing().when(labelService).createLabel(request);

        ResultActions response =
                mockMvc.perform(post("/api/v1/labels").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }

    @Test
    public void LabelController_GetLabels_ReturnGetLabelsResponse() throws Exception {
        Label label = labels.get(0);
        List<LabelDto> labelDtos = new ArrayList<>();
        LabelDto labelDto = new LabelDto();
        labelDto.setId(label.getId());
        labelDto.setColor(label.getColor());
        labelDto.setTitle(label.getTitle());
        labelDto.setIsChecked(label.getIsChecked());
        labelDto.setUserId(label.getUser().getId());
        labelDto.setCreatedAt(label.getCreatedAt());
        labelDto.setWorkSpaceId(label.getWorkSpace().getId());
        labelDtos.add(labelDto);

        when(labelService.getLabels(workSpace.getId())).thenReturn(labelDtos);

        ResultActions response = mockMvc.perform(get("/api/v1/labels").param("workSpaceId", "1"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id",
                        CoreMatchers.is(labelDto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].color",
                        CoreMatchers.is(labelDto.getColor())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].title",
                        CoreMatchers.is(labelDto.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].isChecked",
                        CoreMatchers.is(labelDto.getIsChecked())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].userId",
                        CoreMatchers.is(labelDto.getUserId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].workSpaceId",
                        CoreMatchers.is(labelDto.getWorkSpaceId().intValue())));
    }

    @Test
    public void LabelController_DeleteLabel_ReturnDeleteLabelResponse() throws Exception {
        Label label = labels.get(0);
        doNothing().when(labelService).deleteLabel(label.getId());

        ResultActions response =
                mockMvc.perform(delete(String.format("/api/v1/labels/%d", label.getId())));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }

    @Test
    public void LabelController_UpdateLabel_ReturnUpdateLabelResponse() throws Exception {
        Label label = labels.get(0);
        UpdateLabelRequest request = new UpdateLabelRequest(true);
        LabelDto labelDto = new LabelDto();
        labelDto.setId(label.getId());
        labelDto.setIsChecked(request.getIsChecked());

        when(labelService.updateLabel(anyLong(), any(UpdateLabelRequest.class)))
                .thenReturn(labelDto);

        ResultActions response =
                mockMvc.perform(patch(String.format("/api/v1/labels/%d", label.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.isChecked",
                        CoreMatchers.is(labelDto.getIsChecked())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id",
                        CoreMatchers.is(labelDto.getId().intValue())));
    }

}

