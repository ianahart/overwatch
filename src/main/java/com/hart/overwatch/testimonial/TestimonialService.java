package com.hart.overwatch.testimonial;

import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.testimonial.request.CreateTestimonialRequest;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.util.MyUtil;
import jakarta.validation.ConstraintViolationException;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hart.overwatch.advice.BadRequestException;



@Service
public class TestimonialService {

    private final TestimonialRepository testimonialRepository;

    private final UserService userService;

    @Autowired
    public TestimonialService(TestimonialRepository testimonialRepository,
            UserService userService) {
        this.testimonialRepository = testimonialRepository;
        this.userService = userService;
    }


    private Testimonial getTestimonialById(Long testimonialId) {
        return this.testimonialRepository.findById(testimonialId)
                .orElseThrow(() -> new NotFoundException(String
                        .format("A testimonial with the id %d was not found", testimonialId)));
    }


    private String cleanName(String name) {
        try {
            String[] fullName = Jsoup.clean(name, Safelist.none()).split(" ");
            fullName[0] = MyUtil.capitalize(fullName[0]);
            fullName[1] = MyUtil.capitalize(fullName[1]);
            return String.join(" ", fullName);

        } catch (IndexOutOfBoundsException ex) {
            return MyUtil.capitalize(name);
        }

    }

    public void createTestimonial(CreateTestimonialRequest request) {
        try {
            boolean exists = this.testimonialRepository
                    .testimonialExists(request.getName().toLowerCase(), request.getUserId());

            if (exists) {
                throw new BadRequestException(String
                        .format("You have already added a testimonial by %s", request.getName()));
            }

            User user = this.userService.getUserById(request.getUserId());

            String cleanedName = cleanName(request.getName());

            Testimonial testimonial = new Testimonial(cleanedName,
                    Jsoup.clean(request.getText(), Safelist.none()), false, user);

            this.testimonialRepository.save(testimonial);

        } catch (ConstraintViolationException ex) {
            throw ex;
        }
    }
}
