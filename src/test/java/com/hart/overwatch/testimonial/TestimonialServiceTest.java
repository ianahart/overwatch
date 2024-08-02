package com.hart.overwatch.testimonial;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.IntStream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.testimonial.dto.TestimonialDto;
import com.hart.overwatch.testimonial.request.CreateTestimonialRequest;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TestimonialServiceTest {

    @InjectMocks
    private TestimonialService testimonialService;

    @Mock
    private TestimonialRepository testimonialRepository;

    @Mock
    private PaginationService paginationService;

    @Mock
    private UserService userService;


    private User user;

    private List<Testimonial> testimonials = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        List<String> testimonialNames = Arrays.asList("john doe", "jane doe", "paul smith");
        List<String> testimonialTexts = Arrays.asList("test 1", "test 2", "test 3");
        user = new User("bill@mail.com", "bill", "smith", "bill smith", Role.REVIEWER, true,
                new Profile(), "password123", new Setting());
        user.setId(1L);
        IntStream.rangeClosed(0, 2).forEach(i -> {
            Testimonial testimonial =
                    new Testimonial(testimonialNames.get(i), testimonialTexts.get(i), false, user);
            testimonials.add(testimonial);
        });
    }


    @Test
    public void TestimonialService_CreateTestimonial_ThrowBadRequestException() {
        when(testimonialRepository.testimonialExists(testimonials.get(0).getName(), user.getId())).thenReturn(true);
        CreateTestimonialRequest request = new CreateTestimonialRequest(user.getId(), testimonials.get(0).getName(), "this is a testimonial");

        Assertions.assertThatThrownBy(() -> {
            testimonialService.createTestimonial(request);
        }).isInstanceOf(BadRequestException.class).hasMessage(String.format("You have already added a testimonial by %s", testimonials.get(0).getName()));

    }

    @Test
    public void TestimonialService_CreateTestimonial_ReturnEmpty() {
        when(testimonialRepository.testimonialExists(testimonials.get(1).getName(), user.getId())).thenReturn(false);
        CreateTestimonialRequest request = new CreateTestimonialRequest(user.getId(), testimonials.get(1).getName(), testimonials.get(1).getText());

        when(userService.getUserById(user.getId())).thenReturn(user);
        when(testimonialRepository.save(any(Testimonial.class))).thenReturn(testimonials.get(1));


        testimonialService.createTestimonial(request);

        verify(testimonialRepository, times(1)).save(any(Testimonial.class));
    }

    @Test
    public void TestimonialService_GetTestimonials_ReturnPaginationDtoOfTestimonialDto() {
        int page = 0;
        int pageSize = 3;
        String direction = "next";
        Pageable pageable = PageRequest.of(page, pageSize);
        List<TestimonialDto> testimonialsDto =
                Arrays.asList(new TestimonialDto(1L, user.getId(), testimonials.get(0).getName(),
                        testimonials.get(0).getText(), testimonials.get(0).getCreatedAt()));
        Page<TestimonialDto> pageImpl =
                new PageImpl<>(testimonialsDto, pageable, testimonialsDto.size());

        when(paginationService.getPageable(page, pageSize, direction)).thenReturn(pageable);

        when(testimonialRepository.getTestimonials(pageable, user.getId())).thenReturn(pageImpl);

        PaginationDto<TestimonialDto> result =
                testimonialService.getTestimonials(user.getId(), page, pageSize, direction);

        verify(testimonialRepository, times(1)).getTestimonials(pageable, user.getId());
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getItems().size()).isEqualTo(1);
    }

    @Test
    public void TestimonialService_DeleteTestimonial_ReturnEmpty() {
        testimonials.get(0).setId(1L);
        when(userService.getCurrentlyLoggedInUser()).thenReturn(user);
        when(testimonialRepository.findById(testimonials.get(0).getId()))
                .thenReturn(Optional.of(testimonials.get(0)));

        doNothing().when(testimonialRepository).delete(testimonials.get(0));

        testimonialService.deleteTestimonial(testimonials.get(0).getId());

        verify(testimonialRepository, times(1)).delete(testimonials.get(0));
    }


    @Test
    public void TestimonialService_DeleteTestimonial_ThrowsBadRequestException() {
        testimonials.get(0).setId(1L);
        User unAuthorizedUser = new User();
        unAuthorizedUser.setId(2L);
        when(userService.getCurrentlyLoggedInUser()).thenReturn(unAuthorizedUser);
        when(testimonialRepository.findById(testimonials.get(0).getId()))
                .thenReturn(Optional.of(testimonials.get(0)));

        Assertions.assertThatThrownBy(() -> {
            testimonialService.deleteTestimonial(1L);
        }).isInstanceOf(BadRequestException.class)
                .hasMessage(String.format("Cannot delete another user's testimonial"));

    }


    @Test
    public void TestimonialService_GetLatestTestimonials_ReturnListOfTestimonialDto() {
        Pageable pageable = PageRequest.of(0, 3);
        List<TestimonialDto> testimonialsDto =
                Arrays.asList(new TestimonialDto(1L, user.getId(), testimonials.get(0).getName(),
                        testimonials.get(0).getText(), testimonials.get(0).getCreatedAt()));
        Page<TestimonialDto> pageImpl =
                new PageImpl<>(testimonialsDto, pageable, testimonialsDto.size());


        when(testimonialRepository.getTestimonials(pageable, user.getId())).thenReturn(pageImpl);

        List<TestimonialDto> result = testimonialService.getLatestTestimonials(user.getId());

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(1);
    }
}


