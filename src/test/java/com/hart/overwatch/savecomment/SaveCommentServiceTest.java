package com.hart.overwatch.savecomment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.comment.Comment;
import com.hart.overwatch.comment.CommentService;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.savecomment.dto.SaveCommentDto;
import com.hart.overwatch.savecomment.request.CreateSaveCommentRequest;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.topic.Topic;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class SaveCommentServiceTest {

    @InjectMocks
    private SaveCommentService saveCommentService;

    @Mock
    SaveCommentRepository saveCommentRepository;

    @Mock
    PaginationService paginationService;

    @Mock
    private UserService userService;

    @Mock
    private CommentService commentService;

    private User user;

    private Comment comment;

    private SaveComment saveComment;

    private User createUser() {
        Boolean loggedIn = true;
        User userEntity = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        userEntity.setId(1L);
        return userEntity;
    }

    private Topic createTopic(User user) {
        Topic topicEntity = new Topic("title", "description", user);
        topicEntity.setId(1L);
        return topicEntity;
    }

    private Comment createComment(User user, Topic topic) {
        Comment commentEntity = new Comment();
        commentEntity.setId(1L);
        commentEntity.setUser(user);
        commentEntity.setTopic(topic);
        commentEntity.setContent("content");
        commentEntity.setIsEdited(false);
        commentEntity.setCreatedAt(LocalDateTime.now());
        commentEntity.setReactions(new ArrayList<>());
        commentEntity.setCommentVotes(new ArrayList<>());
        return commentEntity;
    }


    private SaveComment createSaveComment(User user, Comment comment) {
        SaveComment saveCommentEntity = new SaveComment();
        saveCommentEntity.setId(1L);
        saveCommentEntity.setComment(comment);
        saveCommentEntity.setUser(user);
        return saveCommentEntity;
    }

    @BeforeEach
    public void setUp() {
        user = createUser();
        Topic topic = createTopic(user);
        comment = createComment(user, topic);
        saveComment = createSaveComment(user, comment);
    }

    @Test
    public void SaveCommentService_CreateSaveComment_ThrowBadRequestException() {
        CreateSaveCommentRequest request = new CreateSaveCommentRequest();
        request.setUserId(user.getId());
        request.setCommentId(comment.getId());

        when(saveCommentRepository.findSaveCommentByUserIdAndCommentId(request.getUserId(),
                request.getCommentId())).thenReturn(true);

        Assertions.assertThatThrownBy(() -> {
            saveCommentService.createSaveComment(request);
        }).isInstanceOf(BadRequestException.class)
                .hasMessage("You have already saved this comment");
    }

    @Test
    public void SaveCommentService_CreateSaveComment_ReturnNothing() {
        CreateSaveCommentRequest request = new CreateSaveCommentRequest();
        Comment unsavedComment = new Comment();
        unsavedComment.setId(2L);
        request.setUserId(user.getId());
        request.setCommentId(unsavedComment.getId());

        when(saveCommentRepository.findSaveCommentByUserIdAndCommentId(user.getId(),
                unsavedComment.getId())).thenReturn(false);
        when(userService.getUserById(user.getId())).thenReturn(user);
        when(commentService.getCommentById(unsavedComment.getId())).thenReturn(unsavedComment);
        when(saveCommentRepository.save(any(SaveComment.class))).thenReturn(saveComment);

        saveCommentService.createSaveComment(request);

        Assertions.assertThatNoException();

        verify(saveCommentRepository, times(1)).save(any(SaveComment.class));
    }

    @Test
    public void SaveCommentService_GetSaveComments_ReturnPaginatioDtoOfSaveCommentDto() {
        Long userId = 1L;
        int page = 0;
        int pageSize = 10;
        String direction = "desc";

        Pageable mockPageable = mock(Pageable.class);
        when(paginationService.getSortedPageable(page, pageSize, direction, "desc"))
                .thenReturn(mockPageable);

        List<SaveCommentDto> saveCommentDtos = Collections.singletonList(new SaveCommentDto());
        Page<SaveCommentDto> mockPage = new PageImpl<>(saveCommentDtos, mockPageable, 1);
        when(saveCommentRepository.findSaveCommentsByUserId(mockPageable, userId))
                .thenReturn(mockPage);

        PaginationDto<SaveCommentDto> result =
                saveCommentService.getSaveComments(userId, page, pageSize, direction);

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(saveCommentRepository).findSaveCommentsByUserId(pageableCaptor.capture(),
                eq(userId));
        verify(paginationService).getSortedPageable(page, pageSize, direction, "desc");

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getItems()).isEqualTo(saveCommentDtos);
        Assertions.assertThat(result.getPageSize()).isEqualTo(pageSize);
        Assertions.assertThat(result.getTotalPages()).isEqualTo(1);
        Assertions.assertThat(result.getDirection()).isEqualTo(direction);
        Assertions.assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    public void SaveCommentService_DeleteSaveComment_ThrowForbiddenException() {
        Long saveCommentId = saveComment.getId();
        User forbiddenUser = new User();
        forbiddenUser.setId(2L);

        when(saveCommentRepository.findById(saveCommentId)).thenReturn(Optional.of(saveComment));
        when(userService.getCurrentlyLoggedInUser()).thenReturn(forbiddenUser);

        Assertions.assertThatThrownBy(() -> {
            saveCommentService.deleteSaveComment(saveCommentId);
        }).isInstanceOf(ForbiddenException.class)
                .hasMessage("You cannot unsave a comment that is not yours");

    }

    @Test
    public void SaveCommentService_DeleteSaveComment_ReturnNothing() {
        when(saveCommentRepository.findById(saveComment.getId())).thenReturn(Optional.of(saveComment));
        when(userService.getCurrentlyLoggedInUser()).thenReturn(user);
        doNothing().when(saveCommentRepository).delete(saveComment);

        saveCommentService.deleteSaveComment(saveComment.getId());

        Assertions.assertThatNoException();
        verify(saveCommentRepository, times(1)).delete(saveComment);
    }

}


