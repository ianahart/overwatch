package com.hart.overwatch.savecomment;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
import com.hart.overwatch.comment.Comment;
import com.hart.overwatch.comment.CommentRepository;
import com.hart.overwatch.config.DatabaseSetupService;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.savecomment.dto.SaveCommentDto;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.topic.Topic;
import com.hart.overwatch.topic.TopicRepository;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;

@DataJpaTest
@ActiveProfiles("test")
@Import(DatabaseSetupService.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:reset_save_comment_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class SaveCommentRepositoryTest {

    @Autowired
    private SaveCommentRepository saveCommentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    TopicRepository topicRepository;


    @PersistenceContext
    private EntityManager entityManager;

    private User user;

    private Comment comment;

    private SaveComment saveComment;



    private User createUser() {
        Boolean loggedIn = true;
        User userEntity = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        userRepository.save(userEntity);
        return userEntity;
    }

    private Topic createTopic(User user) {
        Topic topicEntity = new Topic("title", "description", user);
        topicRepository.save(topicEntity);
        return topicEntity;
    }

    private Comment createComment(User user, Topic topic) {
        Comment commentEntity = new Comment();
        commentEntity.setUser(user);
        commentEntity.setTopic(topic);
        commentEntity.setContent("content");
        commentEntity.setIsEdited(false);
        commentEntity.setCreatedAt(LocalDateTime.now());
        commentEntity.setReactions(new ArrayList<>());
        commentEntity.setCommentVotes(new ArrayList<>());
        commentRepository.save(commentEntity);
        return commentEntity;
    }


    private SaveComment createSaveComment(User user, Comment comment) {
        SaveComment saveCommentEntity = new SaveComment();
        saveCommentEntity.setComment(comment);
        saveCommentEntity.setUser(user);
        saveCommentRepository.save(saveCommentEntity);
        return saveCommentEntity;
    }

    @BeforeEach
    public void setUp() {
        user = createUser();
        Topic topic = createTopic(user);
        comment = createComment(user, topic);
        saveComment = createSaveComment(user, comment);

    }

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        saveCommentRepository.deleteAll();
        commentRepository.deleteAll();
        topicRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void SaveCommentRepository_FindSaveCommentByUserIdAndCommentId_ReturnBooleanTrue() {
        Long userId = user.getId();
        Long commentId = comment.getId();

        Boolean exists =
                saveCommentRepository.findSaveCommentByUserIdAndCommentId(userId, commentId);

        Assertions.assertThat(exists).isTrue();
    }

    @Test
    public void SaveCommentRepository_FindSaveCommentsByUserId_ReturnPageOfSaveCommentDto() {
        Pageable pageable = PageRequest.of(0, 10);
        Long userId = user.getId();

        Page<SaveCommentDto> result =
                saveCommentRepository.findSaveCommentsByUserId(pageable, userId);
        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.getContent()).hasSize(1);
        SaveCommentDto saveCommentDto = result.getContent().get(0);
        Assertions.assertThat(saveCommentDto.getUserId()).isEqualTo(userId);

    }
}


