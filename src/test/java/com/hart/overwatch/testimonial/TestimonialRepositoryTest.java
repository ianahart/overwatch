package com.hart.overwatch.testimonial;

import java.util.List;
import java.util.stream.IntStream;
import java.util.Arrays;

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
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.testimonial.dto.TestimonialDto;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;
import com.hart.overwatch.setting.Setting;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:reset_testimonial_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class TestimonialRepositoryTest {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    TestimonialRepository testimonialRepository;


    @PersistenceContext
    private EntityManager entityManager;

    private User user;

    private List<String> testimonialNames;

    private List<String> testimonialTexts;

    @BeforeEach
    public void setUp() {
        testimonialNames = Arrays.asList("john doe", "jane doe", "paul smith");
        testimonialTexts = Arrays.asList("test 1", "test 2", "test 3");
        user = new User("bill@mail.com", "bill", "smith", "bill smith", Role.REVIEWER, true,
                new Profile(), "password123", new Setting());
        userRepository.save(user);

        IntStream.rangeClosed(0, 2).forEach(i -> {
            Testimonial testimonial =
                    new Testimonial(testimonialNames.get(i), testimonialTexts.get(i), false, user);
            testimonialRepository.save(testimonial);
        });
    }


    @AfterEach
    public void tearDown() {
        testimonialRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void TestimonialRepository_FindTopThree_ReturnTestimonialList() {
        List<Testimonial> testimonialList =
                testimonialRepository.findTop3ByUserIdOrderByCreatedAtDesc(user.getId());

        Assertions.assertThat(testimonialList).isNotNull();
        Assertions.assertThat(testimonialList.size()).isEqualTo(3);
    }

    @Test
    public void TestimonialRepository_TestimonialExists_ReturnTrue() {
        boolean testimonialExists =
                testimonialRepository.testimonialExists(testimonialNames.get(0), user.getId());

        Assertions.assertThat(testimonialExists).isTrue();

    }

    @Test
    public void TestimonialRepository_TestimonialExists_ReturnFalse() {
        boolean testimonialExists =
                testimonialRepository.testimonialExists("john foe", user.getId());

        Assertions.assertThat(testimonialExists).isFalse();
    }

    @Test
    public void TestimonialRepository_GetTestimonials_ReturnPaginatedTestimonialDto() {
        Pageable pageable = PageRequest.of(0, 3);

        Page<TestimonialDto> result = testimonialRepository.getTestimonials(pageable, user.getId());

        Assertions.assertThat(result.getContent().size()).isEqualTo(3);
    }

    @Test
    public void TestimonialRepository_FindTestimonialById_ReturnSingleTestimonial() {
        Long testimonialId = 1L;

        Testimonial testimonial = testimonialRepository.findById(testimonialId).get();

        Assertions.assertThat(testimonial).isNotNull();

    }
}

