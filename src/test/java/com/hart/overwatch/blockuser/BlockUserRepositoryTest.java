package com.hart.overwatch.blockuser;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import com.hart.overwatch.blockuser.dto.BlockUserDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.profile.ProfileRepository;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:reset_block_user_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class BlockUserRepositoryTest {

    @Autowired
    private BlockUserRepository blockUserRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private User blockerUser;

    private User blockedUser;

    private BlockUser blockUser;


    private User createBlockerUser() {
        boolean loggedIn = true;
        Profile profile = new Profile();
        profile.setAvatarUrl("http://avatar.url/1");
        profileRepository.save(profile);

        blockerUser = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                profile, "Test12345%", new Setting());
        userRepository.save(blockerUser);

        return blockerUser;
    }

    private User createBlockedUser() {
        boolean loggedIn = true;
        Profile profile = new Profile();
        profile.setAvatarUrl("http://avatar.url/2");
        profileRepository.save(profile);

        blockedUser = new User("jane@mail.com", "Jane", "Doe", "Jane Doe", Role.REVIEWER, loggedIn,
                profile, "Test12345%", new Setting());
        userRepository.save(blockedUser);

        return blockedUser;
    }

    @BeforeEach
    public void setUp() {
        blockerUser = createBlockerUser();
        blockedUser = createBlockedUser();
        blockUser = new BlockUser(blockerUser, blockedUser);

        blockUser.setBlockedUser(blockedUser);
        blockUser.setBlockerUser(blockerUser);

        blockUserRepository.save(blockUser);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        blockUserRepository.deleteAll();
        profileRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void BlockUserRepository_GetAllBlockedUsers_ReturnPageOfBlockUserDto() {
        Pageable pageable = PageRequest.of(0, 1);
        Long blockerUserId = blockerUser.getId();

        Page<BlockUserDto> result = blockUserRepository.getAllBlockedUsers(pageable, blockerUserId);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getTotalElements()).isEqualTo(1);
        Assertions.assertThat(result.getContent()).hasSize(1);

        BlockUserDto blockUserDto = result.getContent().get(0);
        Assertions.assertThat(blockUserDto.getBlockedUserId()).isEqualTo(blockedUser.getId());
        Assertions.assertThat(blockUserDto.getFullName()).isEqualTo(blockedUser.getFullName());
        Assertions.assertThat(blockUserDto.getAvatarUrl())
                .isEqualTo(blockedUser.getProfile().getAvatarUrl());
    }

    @Test
    public void BlockUserRepository_FindByBlockedUserIdAndBlockerUserId_ReturnBooleanTrue() {
        Long blockedUserId = blockedUser.getId();
        Long blockerUserId = blockerUser.getId();

        boolean exists = blockUserRepository.findByBlockedUserIdAndBlockerUserId(blockedUserId,
                blockerUserId);

        Assertions.assertThat(exists).isTrue();
    }
}


