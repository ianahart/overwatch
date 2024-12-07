package com.hart.overwatch.ban;

import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import com.hart.overwatch.ban.dto.BanDto;
import com.hart.overwatch.config.DatabaseSetupService;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;

@DataJpaTest
@ActiveProfiles("test")
@Import(DatabaseSetupService.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:reset_ban_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class BanRepositoryTest {

    @Autowired
    private BanRepository banRepository;

    @Autowired
    UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private User user;

    private Ban ban;

    private User createUser() {
        Boolean loggedIn = true;
        Profile profileEntity = new Profile();
        User userEntity = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                profileEntity, "Test12345%", new Setting());
        userRepository.save(userEntity);
        return userEntity;
    }

    private Ban createBan(User user) {
        Ban banEntity = new Ban();
        banEntity.setTime(86400L);
        banEntity.setUser(user);
        banEntity.setAdminNotes("admin notes");
        banEntity.setBanDate(LocalDateTime.now().plusSeconds(86400));

        banRepository.save(banEntity);

        return banEntity;
    }

    @BeforeEach
    public void setUp() {
        user = createUser();
        ban = createBan(user);

    }


    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        banRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void BanRepository_GetBans_ReturnPageOfDto() {
        int page = 0, pageSize = 3;
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<BanDto> result = banRepository.getBans(pageable);

        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.getContent()).hasSize(1);
        BanDto banDto = result.getContent().get(0);
        Assertions.assertThat(banDto.getId()).isEqualTo(ban.getId());
        Assertions.assertThat(banDto.getTime()).isEqualTo(ban.getTime());
        Assertions.assertThat(banDto.getUserId()).isEqualTo(ban.getUser().getId());
        Assertions.assertThat(banDto.getBanDate()).isEqualTo(ban.getBanDate());
        Assertions.assertThat(banDto.getEmail()).isEqualTo(ban.getUser().getEmail());
        Assertions.assertThat(banDto.getFullName()).isEqualTo(ban.getUser().getFullName());
        Assertions.assertThat(banDto.getAdminNotes()).isEqualTo(ban.getAdminNotes());
    }
}


