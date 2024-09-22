package com.hart.overwatch.workspace;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.Optional;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import org.assertj.core.api.Assertions;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.workspace.dto.CreateWorkSpaceDto;
import com.hart.overwatch.workspace.dto.WorkSpaceDto;
import com.hart.overwatch.workspace.request.CreateWorkSpaceRequest;
import com.hart.overwatch.workspace.request.UpdateWorkSpaceRequest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class WorkSpaceServiceTest {

    @InjectMocks
    private WorkSpaceService workSpaceService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WorkSpaceRepository workSpaceRepository;

    @Mock
    private PaginationService paginationService;

    @Mock
    private UserService userService;

    private User user;

    private WorkSpace workSpace;


    @BeforeEach
    void setUp() {
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
    public void WorkSpaceService_WorkSpaceExists_ReturnBoolean() {
        when(workSpaceRepository.findById(workSpace.getId())).thenReturn(Optional.of(workSpace));

        boolean exists = workSpaceService.workSpaceExists(workSpace.getId());

        Assertions.assertThat(exists).isEqualTo(exists);

    }

    @Test
    public void WorkSpaceService_GetWorkSpaceById_ReturnWorkSpace() {
        when(workSpaceRepository.findById(workSpace.getId())).thenReturn(Optional.of(workSpace));
        WorkSpace returnedWorkSpace = workSpaceService.getWorkSpaceById(workSpace.getId());

        Assertions.assertThat(returnedWorkSpace).isNotNull();
        Assertions.assertThat(returnedWorkSpace.getId()).isEqualTo(workSpace.getId());
    }

    @Test
    public void WorkSpaceService_GetWorkSpaceById_ThrowNotFoundException() {
        long nonExistentId = 999L;
        when(workSpaceRepository.findById(nonExistentId)).thenReturn(Optional.ofNullable(null));

        Assertions.assertThatThrownBy(() -> {
            workSpaceService.getWorkSpaceById(nonExistentId);
        }).isInstanceOf(NotFoundException.class).hasMessage(
                String.format("A workspace with the id %d was not found", nonExistentId));

    }

    @Test
    public void WorkSpaceService_CreateWorkSpace_ReturnCreateWorkSpaceDto() {
        CreateWorkSpaceRequest createWorkSpaceRequest = new CreateWorkSpaceRequest();

        createWorkSpaceRequest.setUserId(user.getId());
        createWorkSpaceRequest.setTitle("main");
        createWorkSpaceRequest.setBackgroundColor("#000000");

        when(workSpaceRepository.alreadyExistsByTitleAndUserId("main", user.getId()))
                .thenReturn(false);

        when(workSpaceRepository.countWorkSpacesByUserId(user.getId())).thenReturn(1L);

        when(workSpaceRepository.save(any(WorkSpace.class))).thenReturn(workSpace);

        CreateWorkSpaceDto workSpaceDto = workSpaceService.createWorkSpace(createWorkSpaceRequest);

        verify(workSpaceRepository, times(1)).save(any(WorkSpace.class));
        Assertions.assertThat(workSpaceDto).isNotNull();
    }

    @Test
    public void WorkSpaceService_CreateWorkSpace_ThrowBadRequestExceptionExists() {
        CreateWorkSpaceRequest createWorkSpaceRequest = new CreateWorkSpaceRequest();

        createWorkSpaceRequest.setUserId(user.getId());
        createWorkSpaceRequest.setTitle("main");
        createWorkSpaceRequest.setBackgroundColor("#000000");

        when(workSpaceRepository.alreadyExistsByTitleAndUserId("main", user.getId()))
                .thenReturn(true);

        Assertions.assertThatThrownBy(() -> {
            workSpaceService.createWorkSpace(createWorkSpaceRequest);
        }).isInstanceOf(BadRequestException.class)
                .hasMessage("You already have a workspace with that title");
    }

    @Test
    public void WorkSpaceService_CreateWorkSpace_ThrowBadRequestExceptionMaxWorkSpaces() {
        CreateWorkSpaceRequest createWorkSpaceRequest = new CreateWorkSpaceRequest();

        createWorkSpaceRequest.setUserId(user.getId());
        createWorkSpaceRequest.setTitle("does not exist");
        createWorkSpaceRequest.setBackgroundColor("#000000");

        when(workSpaceRepository.alreadyExistsByTitleAndUserId("does not exist", user.getId()))
                .thenReturn(false);

        when(workSpaceRepository.countWorkSpacesByUserId(user.getId())).thenReturn(20L);

        Assertions.assertThatThrownBy(() -> {
            workSpaceService.createWorkSpace(createWorkSpaceRequest);
        }).isInstanceOf(BadRequestException.class)
                .hasMessage("You have the maximum amount of open workspaces. Please delete one.");
    }

    @Test
    public void WorkSpaceService_GetWorkSpaces_ReturnPaginationDtoOfWorkSpaceDto() {
        int page = 0;
        int pageSize = 3;
        String direction = "next";
        Pageable pageable = Pageable.ofSize(pageSize);
        WorkSpaceDto workSpaceDto = new WorkSpaceDto();
        Page<WorkSpaceDto> pageResult =
                new PageImpl<>(Collections.singletonList(workSpaceDto), pageable, 1);
        PaginationDto<WorkSpaceDto> expectedPaginationDto =
                new PaginationDto<>(pageResult.getContent(), pageResult.getNumber(), pageSize,
                        pageResult.getTotalPages(), direction, pageResult.getTotalElements());

        when(paginationService.getPageable(page, pageSize, direction)).thenReturn(pageable);
        when(workSpaceRepository.getWorkSpacesByUserId(pageable, user.getId()))
                .thenReturn(pageResult);
        PaginationDto<WorkSpaceDto> actualPaginationDto =
                workSpaceService.getWorkSpaces(user.getId(), page, pageSize, direction);

        Assertions.assertThat(actualPaginationDto.getItems())
                .isEqualTo(expectedPaginationDto.getItems());
        Assertions.assertThat(actualPaginationDto.getTotalElements())
                .isEqualTo(expectedPaginationDto.getTotalElements());
    }

    @Test
    public void WorkSpaceService_UpdateWorkSpace_ThrowForbiddenException() {
        Long forbiddenUserId = 2L;
        UpdateWorkSpaceRequest request = new UpdateWorkSpaceRequest();

        request.setTitle("main updated");
        request.setBackgroundColor("#0000FF");
        request.setUserId(forbiddenUserId);

        when(workSpaceRepository.findById(workSpace.getId())).thenReturn(Optional.of(workSpace));

        Assertions.assertThatThrownBy(() -> {
            workSpaceService.updateWorkSpace(request, workSpace.getId());

        }).isInstanceOf(BadRequestException.class)
                .hasMessage("Cannot edit another user's workspace title");
    }


}


