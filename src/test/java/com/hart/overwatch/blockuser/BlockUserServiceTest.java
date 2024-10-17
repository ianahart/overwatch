package com.hart.overwatch.blockuser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.pagination.PaginationService;
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

}


