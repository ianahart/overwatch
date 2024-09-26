package com.hart.overwatch.label;


import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import com.hart.overwatch.label.dto.LabelDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;
import com.hart.overwatch.workspace.WorkSpace;
import com.hart.overwatch.workspace.WorkSpaceRepository;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:reset_label_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class LabelRepositoryTest {

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private WorkSpaceRepository workSpaceRepository;

    @Autowired
    UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private User user;

    private WorkSpace workSpace;

    private List<Label> labels;

    @BeforeEach
    public void setUp() {
        user = generateUser();
        userRepository.save(user);

        workSpace = generatedWorkSpace(user);
        workSpaceRepository.save(workSpace);

        labels = generateLabels(user, workSpace);
        labelRepository.saveAll(labels);
    }

    private WorkSpace generatedWorkSpace(User user) {
        WorkSpace workSpace = new WorkSpace();
        workSpace.setTitle("main");
        workSpace.setBackgroundColor("#000000");
        workSpace.setUser(user);

        return workSpace;
    }

    private User generateUser() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setFullName("John Doe");
        user.setEmail("john@mail.com");
        user.setRole(Role.USER);
        user.setLoggedIn(true);
        user.setProfile(new Profile());
        user.setPassword("Test12345%");
        user.setSetting(new Setting());

        return user;
    }

    private List<Label> generateLabels(User user, WorkSpace workSpace) {
        LocalDateTime timestamp = LocalDateTime.now();
        String[] titles = new String[] {"priority", "warning"};
        List<Label> labels = new ArrayList<>();
        int toGenerate = 2;
        for (int i = 1; i <= toGenerate; i++) {
            Label label = new Label();
            label.setUser(user);
            label.setWorkSpace(workSpace);
            label.setColor("#000000");
            label.setTitle(titles[i - 1]);
            label.setIsChecked(false);
            label.setCreatedAt(timestamp);
            label.setUpdatedAt(timestamp);
            labels.add(label);
        }
        return labels;
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        workSpaceRepository.deleteAll();
        labelRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void LabelRepository_GetLabelsByWorkSpaceId_ReturnPageOfLabelDto() {
        Pageable pageable = PageRequest.of(0, 10);
        Long workSpaceId = workSpace.getId();

        Page<LabelDto> page = labelRepository.getLabelsByWorkSpaceId(workSpaceId, pageable);

        Assertions.assertThat(page).isNotNull();
        Assertions.assertThat(page.getContent().size()).isEqualTo(2);
        List<LabelDto> labelDtos = page.getContent();
        Assertions.assertThat(labelDtos.get(0).getTitle()).isEqualTo("priority");
        Assertions.assertThat(labelDtos.get(1).getTitle()).isEqualTo("warning");
    }

    @Test
    public void LabelRepository_CountLabelsInWorkSpace_ReturnLongCount() {
        Long workSpaceId = workSpace.getId();

        long count = labelRepository.countLabelsInWorkSpace(workSpaceId);

        Assertions.assertThat(count).isEqualTo(2);
    }
}


