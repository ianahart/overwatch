package com.hart.overwatch.badge;

import java.util.List;
import java.util.Optional;
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
import com.hart.overwatch.badge.dto.BadgeDto;
import com.hart.overwatch.config.DatabaseSetupService;

@DataJpaTest
@ActiveProfiles("test")
@Import(DatabaseSetupService.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:reset_ban_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class BadgeRepositoryTest {

    @Autowired
    private BadgeRepository badgeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Badge badge;


    private Badge createBadge() {
        Badge badgeEntity = new Badge();
        badgeEntity.setTitle("First Reviewer Badge");
        badgeEntity.setImageUrl("https://www.imgur.com/photo-1");
        badgeEntity.setDescription("description");
        badgeEntity.setImageFileName("photo-1");
        badgeEntity.setReviewerBadges(List.of());

        badgeRepository.save(badgeEntity);

        return badgeEntity;
    }

    @BeforeEach
    public void setUp() {
        badge = createBadge();
    }


    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        badgeRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void BadgeRepository_FindBadgeByTitle_ReturnOptionalBadge() {
        String title = badge.getTitle().toLowerCase();

        Optional<Badge> returnedBadge = badgeRepository.findBadgeByTitle(title);

        Assertions.assertThat(returnedBadge).isPresent();
        Assertions.assertThat(returnedBadge.get().getTitle().toLowerCase()).isEqualTo(title);
    }

    @Test
    public void BadgeRepository_ExistsByTitle_ReturnBooleanTrue() {
        String title = badge.getTitle().toLowerCase();

        boolean exists = badgeRepository.existsByTitle(title);

        Assertions.assertThat(exists).isTrue();
    }

    @Test
    public void BadgeRepository_GetBadges_ReturnPageOfBadgeDto() {
        int page = 0, pageSize = 3;
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<BadgeDto> result = badgeRepository.getBadges(pageable);

        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.getContent()).hasSize(1);
        BadgeDto badgeDto = result.getContent().get(0);
        Assertions.assertThat(badgeDto.getId()).isEqualTo(badge.getId());
        Assertions.assertThat(badgeDto.getTitle()).isEqualTo(badge.getTitle());
        Assertions.assertThat(badgeDto.getDescription()).isEqualTo(badge.getDescription());
        Assertions.assertThat(badgeDto.getImageUrl()).isEqualTo(badge.getImageUrl());
    }
}


