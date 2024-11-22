package com.hart.overwatch.apptestimonial;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.comment.Comment;
import com.hart.overwatch.comment.CommentService;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.reportcomment.request.CreateReportCommentRequest;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.topic.Topic;
import com.hart.overwatch.topic.TopicService;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class AppTestimonialServiceTest {

    @InjectMocks
    private AppTestimonialService appTestimonialService;

    @Mock
    private UserService userService;

    @Mock
    private AppTestimonialRepository appTestimonialRepository;

    private User user;

    private AppTestimonial appTestimonial;

    private User createUser() {
        Boolean loggedIn = true;
        Profile profile = new Profile();
        profile.setAvatarUrl("https://imgur.com/profile-pic");
        profile.setId(1L);

        User userEntity = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                profile, "Test12345%", new Setting());
        userEntity.setId(1L);
        return userEntity;
    }

    private AppTestimonial createAppTestimonial(User user) {
        AppTestimonial appTestimonialEntity = new AppTestimonial();
        appTestimonialEntity.setId(1L);
        appTestimonialEntity.setDeveloperType("Frontend Developer");
        appTestimonialEntity.setContent("testimonial content");
        appTestimonialEntity.setUser(user);

        return appTestimonialEntity;
    }


    @BeforeEach
    public void setUp() {
        user = createUser();
        appTestimonial = createAppTestimonial(user);
    }

    @Test
    public void AppTestimonialService_GetAppTestimonial_ThrowNotFoundException() {
        when(userService.getCurrentlyLoggedInUser()).thenReturn(user);
        when(appTestimonialRepository.getAppTestimonialByUserId(user.getId())).thenReturn(null);

        Assertions.assertThatThrownBy(() -> {
            appTestimonialService.getAppTestimonial();
        }).isInstanceOf(NotFoundException.class).hasMessage("You don't seem to have written a testimonial yet");
    }
}


