package com.hart.overwatch.blockuser;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.mockito.Mockito.*;
import com.hart.overwatch.blockuser.dto.BlockUserDto;
import com.hart.overwatch.blockuser.request.CreateBlockUserRequest;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.token.TokenRepository;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.hamcrest.CoreMatchers;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@ActiveProfiles("test")
@WebMvcTest(controllers = BlockUserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class BlockUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlockUserService blockUserService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private TokenRepository tokenRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User blockerUser;

    private User blockedUser;

    private BlockUser blockUser;


    private User createBlockerUser() {
        boolean loggedIn = true;
        Profile profile = new Profile();
        profile.setAvatarUrl("http://avatar.url/1");
        profile.setId(1L);

        blockerUser = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                profile, "Test12345%", new Setting());
        blockerUser.setId(1L);

        return blockerUser;
    }

    private User createBlockedUser() {
        boolean loggedIn = true;
        Profile profile = new Profile();
        profile.setAvatarUrl("http://avatar.url/2");
        profile.setId(2L);

        blockedUser = new User("jane@mail.com", "Jane", "Doe", "Jane Doe", Role.REVIEWER, loggedIn,
                profile, "Test12345%", new Setting());
        blockedUser.setId(2L);

        return blockedUser;
    }

    @BeforeEach
    public void setUp() {
        blockerUser = createBlockerUser();
        blockedUser = createBlockedUser();
        blockUser = new BlockUser(blockerUser, blockedUser);

        blockUser.setBlockedUser(blockedUser);
        blockUser.setBlockerUser(blockerUser);

        blockUser.setId(1L);

        blockerUser.setBlockerUsers(List.of(blockUser));
    }

    @Test
    public void BlockUserController_GetAllBlockUsers_ReturnGetAllBlockUserResponse()
            throws Exception {
        Long blockerUserId = blockerUser.getId();
        int page = 0;
        int pageSize = 3;
        String direction = "next";
        Pageable pageable = Pageable.ofSize(pageSize);
        BlockUserDto blockUserDto = new BlockUserDto();
        LocalDateTime createdAt = LocalDateTime.now();
        blockUserDto.setId(blockUser.getId());
        blockUserDto.setFullName(blockedUser.getFullName());
        blockUserDto.setAvatarUrl(blockedUser.getProfile().getAvatarUrl());
        blockUserDto.setBlockedUserId(blockedUser.getId());
        blockUserDto.setCreatedAt(createdAt);

        Page<BlockUserDto> pageResult =
                new PageImpl<>(Collections.singletonList(blockUserDto), pageable, 1);
        PaginationDto<BlockUserDto> paginationDto =
                new PaginationDto<>(pageResult.getContent(), pageResult.getNumber(), pageSize,
                        pageResult.getTotalPages(), direction, pageResult.getTotalElements());


        when(blockUserService.getAllBlockedUsers(blockerUserId, page, pageSize, direction))
                .thenReturn(paginationDto);

        ResultActions response =
                mockMvc.perform(get("/api/v1/block-users").contentType(MediaType.APPLICATION_JSON)
                        .param("blockerUserId", String.valueOf(blockerUserId))
                        .param("page", String.valueOf(page))
                        .param("pageSize", String.valueOf(pageSize)).param("direction", direction));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.items[0]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.items[0].id",
                        CoreMatchers.is(blockUserDto.getId().intValue())))
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
    public void BlockUserController_CreateBlockUser_ReturnCreateBlockUserResponse()
            throws Exception {
        CreateBlockUserRequest request = new CreateBlockUserRequest();
        request.setBlockedUserId(blockedUser.getId());
        request.setBlockerUserId(blockerUser.getId());

        doNothing().when(blockUserService).createBlockUser(request);

        ResultActions response =
                mockMvc.perform(post("/api/v1/block-users").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));

        verify(blockUserService, times(1)).createBlockUser(any(CreateBlockUserRequest.class));
    }

    @Test
    public void BlockUserController_DeleteBlockUser_ReturnDeleteBlockUserResponse()
            throws Exception {
        Long blockUserId = blockUser.getId();

        doNothing().when(blockUserService).deleteBlockUser(blockUserId);

        ResultActions response =
                mockMvc.perform(delete(String.format("/api/v1/block-users/%d", blockUserId)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));

        verify(blockUserService, times(1)).deleteBlockUser(blockUserId);
    }

}


