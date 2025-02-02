package com.hart.overwatch.workspace;

import java.util.List;
import java.sql.Timestamp;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.token.TokenRepository;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.workspace.dto.CreateWorkSpaceDto;
import com.hart.overwatch.workspace.dto.WorkSpaceDto;
import com.hart.overwatch.workspace.request.CreateWorkSpaceRequest;
import com.hart.overwatch.workspace.request.UpdateWorkSpaceRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.data.domain.PageImpl;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import java.util.Collections;
import org.hamcrest.CoreMatchers;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

@ActiveProfiles("test")
@WebMvcTest(controllers = WorkSpaceController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class WorkSpaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WorkSpaceService workSpaceService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private TokenRepository tokenRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    private WorkSpace workSpace;

    @BeforeEach
    public void init() {
        Boolean loggedIn = true;
        user = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());

        workSpace = new WorkSpace("main", "#000000", user);
        workSpace.setId(1L);
        user.setId(1L);
        workSpace.setUser(user);

        List<WorkSpace> workSpaces = new ArrayList<>();

        workSpaces.add(workSpace);
        user.setWorkSpaces(workSpaces);
    }

    @Test
    public void WorkSpaceController_CreateWorkSpace_ReturnCreateWorkSpaceResponse()
            throws Exception {
        CreateWorkSpaceRequest createWorkSpaceRequest = new CreateWorkSpaceRequest();
        createWorkSpaceRequest.setUserId(1L);
        createWorkSpaceRequest.setTitle("main");
        createWorkSpaceRequest.setBackgroundColor("#0000FF");

        when(workSpaceService.createWorkSpace(any(CreateWorkSpaceRequest.class)))
                .thenReturn(new CreateWorkSpaceDto("main", "#0000FF"));

        ResultActions response =
                mockMvc.perform(post("/api/v1/workspaces").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createWorkSpaceRequest)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title", CoreMatchers.is("main")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.backgroundColor",
                        CoreMatchers.is("#0000FF")));
    }

    @Test
    public void WorkSpaceController_GetWorkSpaces_ReturnGetAllWorkSpaceResponse() throws Exception {
        Long userId = user.getId();
        int page = 0;
        int pageSize = 3;
        String direction = "next";
        Pageable pageable = Pageable.ofSize(pageSize);
        WorkSpaceDto reviewDto = new WorkSpaceDto();
        Page<WorkSpaceDto> pageResult =
                new PageImpl<>(Collections.singletonList(reviewDto), pageable, 1);
        PaginationDto<WorkSpaceDto> paginationDto =
                new PaginationDto<>(pageResult.getContent(), pageResult.getNumber(), pageSize,
                        pageResult.getTotalPages(), direction, pageResult.getTotalElements());


        when(workSpaceService.getWorkSpaces(userId, page, pageSize, direction))
                .thenReturn(paginationDto);

        ResultActions response =
                mockMvc.perform(get("/api/v1/workspaces").contentType(MediaType.APPLICATION_JSON)
                        .param("userId", String.valueOf(userId)).param("page", String.valueOf(page))
                        .param("pageSize", String.valueOf(pageSize)).param("direction", direction));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.items[0]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.items[0].id",
                        CoreMatchers.is(reviewDto.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.page",
                        CoreMatchers.is(pageResult.getNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize",
                        CoreMatchers.is(pageSize)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalPages",
                        CoreMatchers.is(pageResult.getTotalPages())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.direction",
                        CoreMatchers.is(direction)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalElements",
                        CoreMatchers.is((int) pageResult.getTotalElements())));
    }

    @Test
    public void WorkSpaceController_UpdateWorkSpace_ReturnUpdateWorkSpaceResponse()
            throws Exception {
        UpdateWorkSpaceRequest request =
                new UpdateWorkSpaceRequest(user.getId(), "main", "#0000FF");
        WorkSpaceDto workSpaceDto = new WorkSpaceDto();
        workSpaceDto.setId(workSpace.getId());
        workSpaceDto.setTitle("main");
        workSpaceDto.setBackgroundColor("#0000FF");
        workSpaceDto.setUserId(user.getId());
        workSpaceDto.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        when(workSpaceService.updateWorkSpace(any(UpdateWorkSpaceRequest.class), anyLong()))
                .thenReturn(workSpaceDto);

        ResultActions response =
                mockMvc.perform(patch("/api/v1/workspaces/" + String.valueOf(workSpace.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id",
                        CoreMatchers.equalToObject(workSpace.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.userId",
                        CoreMatchers.equalToObject(workSpace.getUser().getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title",
                        CoreMatchers.is(Jsoup.clean(request.getTitle(), Safelist.none()))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.backgroundColor", CoreMatchers
                        .is(Jsoup.clean(request.getBackgroundColor(), Safelist.none()))));
    }

    @Test
    public void WorkSpaceController_DeleteWorkSpace_ReturnDeleteWorkSpaceResponse()
            throws Exception {

        doNothing().when(workSpaceService).deleteWorkSpace(workSpace.getId());

        ResultActions response = mockMvc
                .perform(delete("/api/v1/workspaces/1").contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }

    @Test
    public void WorkSpaceController_GetLatestWorkSpace_ReturnGetLatestWorkSpaceResponse()
            throws Exception {
        WorkSpaceDto workSpaceDto = new WorkSpaceDto();
        workSpaceDto.setId(workSpace.getId());
        workSpaceDto.setTitle("main");
        workSpaceDto.setBackgroundColor("#000000");
        workSpaceDto.setUserId(user.getId());
        workSpaceDto.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        when(workSpaceService.getLatestWorkSpace(user.getId())).thenReturn(workSpaceDto);

        ResultActions response = mockMvc.perform(get("/api/v1/workspaces/latest")
                .contentType(MediaType.APPLICATION_JSON).param("userId", "1"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id",
                        CoreMatchers.equalToObject(workSpace.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.userId",
                        CoreMatchers.equalToObject(workSpace.getUser().getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title",
                        CoreMatchers.is(Jsoup.clean(workSpace.getTitle(), Safelist.none()))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.backgroundColor", CoreMatchers
                        .is(Jsoup.clean(workSpace.getBackgroundColor(), Safelist.none()))));


    }


}
