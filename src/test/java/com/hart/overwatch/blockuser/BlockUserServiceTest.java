package com.hart.overwatch.blockuser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
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
        PaginationDto<BlockUserDto> expectedPaginationDto =
                new PaginationDto<>(pageResult.getContent(), pageResult.getNumber(), pageSize,
                        pageResult.getTotalPages(), direction, pageResult.getTotalElements());

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

}


