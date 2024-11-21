package com.hart.overwatch.apptestimonial;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.apptestimonial.request.CreateAppTestimonialRequest;

@Service
public class AppTestimonialService {

    private final AppTestimonialRepository appTestimonialRepository;

    private final UserService userService;

    @Autowired
    public AppTestimonialService(AppTestimonialRepository appTestimonialRepository,
            UserService userService) {
        this.appTestimonialRepository = appTestimonialRepository;
        this.userService = userService;
    }

    private AppTestimonial getAppTestimonialById(Long appTestimonialId) {
        return appTestimonialRepository.findById(appTestimonialId)
                .orElseThrow(() -> new NotFoundException(String
                        .format("Could not find app testimonial with id %d", appTestimonialId)));
    }


    private boolean alreadySubmittedTestimonial(Long userId) {
        return appTestimonialRepository.existsByUserId(userId);
    }

    public void createAppTestimonial(CreateAppTestimonialRequest request) {
        Long userId = request.getUserId();

        if (alreadySubmittedTestimonial(userId)) {
            throw new BadRequestException("You have already submitted a testimonial for OverWatch");
        }

        String developerType = Jsoup.clean(request.getDeveloperType(), Safelist.none());
        String content = Jsoup.clean(request.getContent(), Safelist.none());
        User user = userService.getUserById(userId);

        AppTestimonial appTestimonial = new AppTestimonial();

        appTestimonial.setUser(user);
        appTestimonial.setDeveloperType(developerType);
        appTestimonial.setContent(content);

        appTestimonialRepository.save(appTestimonial);
    }
}
