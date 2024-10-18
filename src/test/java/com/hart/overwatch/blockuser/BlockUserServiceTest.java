package com.hart.overwatch.blockuser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.blockuser.dto.BlockUserDto;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;
import com.hart.overwatch.user.UserService;


@ExtendWith(MockitoExtension.class)
public class BlockUserServiceTest {
    @InjectMocks
    private BlockUserService blockUserService;

    @Mock
    BlockUserRepository blockUserRepository;

    @Mock
    UserService userService;

    @Mock
    PaginationService paginationService;

    @Mock
    UserRepository userRepository;

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
    public void BlockUserService_DeleteBlockUser_ThrowBadRequestException() {
        Long blockUserId = null;

        Assertions.assertThatThrownBy(() -> {
            blockUserService.deleteBlockUser(blockUserId);
        }).isInstanceOf(BadRequestException.class).hasMessage("Missing block user id");
    }

    @Test
    public void BlockUserService_DeleteBlockUser_ThrowForbiddenException() {
        Long blockUserId = blockUser.getId();

        User forbiddenUser = new User();
        forbiddenUser.setId(999L);

        when(userService.getCurrentlyLoggedInUser()).thenReturn(forbiddenUser);
        when(blockUserRepository.findById(blockUserId)).thenReturn(Optional.of(blockUser));

        Assertions.assertThatThrownBy(() -> {
            blockUserService.deleteBlockUser(blockUserId);
        }).isInstanceOf(ForbiddenException.class)
                .hasMessage("Cannot unblock a user that you have not blocked");
    }

    @Test
    public void BlockUserService_DeleteBlockUser_ReturnNothing() {
        Long blockUserId = blockUser.getId();

        when(userService.getCurrentlyLoggedInUser()).thenReturn(blockerUser);
        when(blockUserRepository.findById(blockUserId)).thenReturn(Optional.of(blockUser));

        doNothing().when(blockUserRepository).delete(blockUser);

        blockUserService.deleteBlockUser(blockUserId);

        verify(blockUserRepository, times(1)).delete(blockUser);
    }

    @Test
    public void BlockUserService_GetAllBlockedUsers_ReturnPaginationDtoOfBlockUserDto() {
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

        when(paginationService.getPageable(page, pageSize, direction)).thenReturn(pageable);

        when(blockUserRepository.getAllBlockedUsers(pageable, blockerUser.getId()))
                .thenReturn(pageResult);

        PaginationDto<BlockUserDto> result =
                blockUserService.getAllBlockedUsers(blockerUser.getId(), page, pageSize, direction);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getItems()).hasSize(1);
        BlockUserDto actualBlockUserDto = result.getItems().getFirst();
        Assertions.assertThat(actualBlockUserDto.getId()).isEqualTo(blockUserDto.getId());
        Assertions.assertThat(actualBlockUserDto.getBlockedUserId())
                .isEqualTo(blockUserDto.getBlockedUserId());
        Assertions.assertThat(actualBlockUserDto.getFullName())
                .isEqualTo(blockUserDto.getFullName());
        Assertions.assertThat(actualBlockUserDto.getAvatarUrl())
                .isEqualTo(actualBlockUserDto.getAvatarUrl());
        Assertions.assertThat(actualBlockUserDto.getCreatedAt())
                .isEqualTo(blockUserDto.getCreatedAt());
    }

    @Test
    public void BlockUserService_GetCurUserBlockedByUser_ReturnBlockUser() {
        when(userRepository.findUserWithBlockerUsers(blockerUser.getId())).thenReturn(blockerUser);

        BlockUser result = blockUserService.getCurUserBlockedByUser(blockedUser.getId(), blockerUser.getId());

        Assertions.assertThat(result).isNotNull();
    }


}


