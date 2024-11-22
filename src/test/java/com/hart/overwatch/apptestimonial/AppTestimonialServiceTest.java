package com.hart.overwatch.apptestimonial;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.apptestimonial.dto.AppTestimonialDto;
import com.hart.overwatch.apptestimonial.dto.MinAppTestimonialDto;
import com.hart.overwatch.apptestimonial.request.CreateAppTestimonialRequest;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    private AppTestimonialDto convertToDto(AppTestimonial appTestimonial) {
        AppTestimonialDto appTestimonialDto = new AppTestimonialDto();
        appTestimonialDto.setId(appTestimonial.getId());
        appTestimonialDto.setContent(appTestimonial.getContent());
        appTestimonialDto.setDeveloperType(appTestimonial.getDeveloperType());
        appTestimonialDto.setFirstName(appTestimonial.getUser().getFirstName());
        appTestimonialDto.setAvatarUrl(appTestimonial.getUser().getProfile().getAvatarUrl());

        return appTestimonialDto;
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

    @Test
    public void AppTestimonialService_GetAppTestimonial_ReturnMinAppTestimonialDto() {
        when(userService.getCurrentlyLoggedInUser()).thenReturn(user);
        when(appTestimonialRepository.getAppTestimonialByUserId(user.getId())).thenReturn(appTestimonial);

        MinAppTestimonialDto minAppTestimonialDto = appTestimonialService.getAppTestimonial();

        Assertions.assertThat(minAppTestimonialDto).isNotNull();
        Assertions.assertThat(minAppTestimonialDto.getId()).isEqualTo(appTestimonial.getId());
        Assertions.assertThat(minAppTestimonialDto.getContent()).isEqualTo(appTestimonial.getContent());
        Assertions.assertThat(minAppTestimonialDto.getDeveloperType()).isEqualTo(appTestimonial.getDeveloperType());
    }

    @Test
    public void AppTestimonialService_CreateAppTestimonial_ThrowBadRequestException() {
        CreateAppTestimonialRequest request = new CreateAppTestimonialRequest();
        request.setUserId(user.getId());
        request.setContent("new content");
        request.setDeveloperType("Frontend Developer");

        when(appTestimonialRepository.existsByUserId(user.getId())).thenReturn(true);

        Assertions.assertThatThrownBy(() -> {
            appTestimonialService.createAppTestimonial(request);
        }).isInstanceOf(BadRequestException.class)
                .hasMessage("You have already submitted a testimonial for OverWatch");
    }

    @Test
    public void AppTestimonialService_CreateAppTestimonial_ReturnNothing() {
        CreateAppTestimonialRequest request = new CreateAppTestimonialRequest();
        User userWithoutAppTestimonial = new User();
        userWithoutAppTestimonial.setId(2L);
        request.setUserId(userWithoutAppTestimonial.getId());
        request.setContent("new content");
        request.setDeveloperType("Frontend Developer");

        when(appTestimonialRepository.existsByUserId(userWithoutAppTestimonial.getId()))
                .thenReturn(false);
        when(userService.getUserById(userWithoutAppTestimonial.getId()))
                .thenReturn(userWithoutAppTestimonial);
        when(appTestimonialRepository.save(any(AppTestimonial.class)))
                .thenReturn(new AppTestimonial());

        appTestimonialService.createAppTestimonial(request);
        Assertions.assertThatNoException();
        verify(appTestimonialRepository, times(1)).save(any(AppTestimonial.class));
    }

    @Test
    public void AppTestimonialService_UpdateAppTestimonial_ThrowForbiddenException() {
        User forbiddenUser = new User();
        forbiddenUser.setId(2L);
        Long appTestimonialId = appTestimonial.getId();
        CreateAppTestimonialRequest request = new CreateAppTestimonialRequest();
        request.setUserId(forbiddenUser.getId());
        request.setContent("updated content");
        request.setDeveloperType("Backend Developer");

        when(userService.getUserById(forbiddenUser.getId())).thenReturn(forbiddenUser);
        when(appTestimonialRepository.findById(appTestimonialId))
                .thenReturn(Optional.of(appTestimonial));

        Assertions.assertThatThrownBy(() -> {
            appTestimonialService.updateAppTestimonial(request, appTestimonialId);

        }).isInstanceOf(ForbiddenException.class)
                .hasMessage("You cannot update another user's testimonial");
    }

    @Test
    public void AppTestimonialService_UpdateAppTestimonial_ReturnNothing() {
        Long appTestimonialId = appTestimonial.getId();
        CreateAppTestimonialRequest request = new CreateAppTestimonialRequest();
        request.setUserId(user.getId());
        request.setContent("updated content");
        request.setDeveloperType("Backend Developer");

        when(userService.getUserById(user.getId())).thenReturn(user);
        when(appTestimonialRepository.findById(appTestimonialId))
                .thenReturn(Optional.of(appTestimonial));
        when(appTestimonialRepository.save(any(AppTestimonial.class))).thenReturn(appTestimonial);

        appTestimonialService.updateAppTestimonial(request, appTestimonialId);

        Assertions.assertThatNoException();
        verify(appTestimonialRepository, times(1)).save(any(AppTestimonial.class));
    }

    @Test
    public void AppTestimonialService_DeleteAppTestimonial_ThrowForbiddenException() {
        Long appTestimonialId = appTestimonial.getId();
        User forbiddenUser = new User();
        forbiddenUser.setId(2L);
        when(userService.getCurrentlyLoggedInUser()).thenReturn(forbiddenUser);
        when(appTestimonialRepository.findById(appTestimonialId))
                .thenReturn(Optional.of(appTestimonial));

        Assertions.assertThatThrownBy(() -> {
            appTestimonialService.deleteAppTestimonial(appTestimonialId);
        }).isInstanceOf(ForbiddenException.class)
                .hasMessage("You cannot delete another user's testimonial");
    }

    @Test
    public void AppTestimonialService_DeleteAppTestimonial_ReturnNothing() {
        when(userService.getCurrentlyLoggedInUser()).thenReturn(user);
        when(appTestimonialRepository.findById(appTestimonial.getId())).thenReturn(Optional.of(appTestimonial));
        doNothing().when(appTestimonialRepository).delete(appTestimonial);

        appTestimonialService.deleteAppTestimonial(appTestimonial.getId());

        Assertions.assertThatNoException();
        verify(appTestimonialRepository, times(1)).delete(appTestimonial);
    }

    @Test
    public void AppTestimonialService_GetAppTestimonials_ThrowBadRequestException() {
        Integer pageSize = null;

        Assertions.assertThatThrownBy(() -> {
            appTestimonialService.getAppTestimonials(pageSize);
        }).isInstanceOf(BadRequestException.class).hasMessage("Missing page size requirement");
    }

    @Test
    public void AppTestimonialService_GetAppTestimonials_ReturnListofAppTestimonialDto() {
        Integer pageSize = 2;
        List<AppTestimonialDto> appTestimonialDtos = List.of(convertToDto(appTestimonial));
        Pageable pageable = PageRequest.of(0, pageSize);
        Page<AppTestimonialDto> pageResult =
                new PageImpl<>(appTestimonialDtos, pageable, appTestimonialDtos.size());

        when(appTestimonialRepository.getAppTestimonials(pageable)).thenReturn(pageResult);

        List<AppTestimonialDto> result = appTestimonialService.getAppTestimonials(pageSize);

        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result).hasSize(1);

        AppTestimonialDto returnedDto = result.get(0);

        Assertions.assertThat(returnedDto.getId()).isEqualTo(appTestimonial.getId());
        Assertions.assertThat(returnedDto.getContent()).isEqualTo(appTestimonial.getContent());
        Assertions.assertThat(returnedDto.getDeveloperType())
                .isEqualTo(appTestimonial.getDeveloperType());
        Assertions.assertThat(returnedDto.getAvatarUrl())
                .isEqualTo(appTestimonial.getUser().getProfile().getAvatarUrl());
        Assertions.assertThat(returnedDto.getFirstName())
                .isEqualTo(appTestimonial.getUser().getFirstName());
    }

}


