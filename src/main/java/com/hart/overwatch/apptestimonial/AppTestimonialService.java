package com.hart.overwatch.apptestimonial;

import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.apptestimonial.dto.AdminAppTestimonialDto;
import com.hart.overwatch.apptestimonial.dto.AppTestimonialDto;
import com.hart.overwatch.apptestimonial.dto.MinAppTestimonialDto;
import com.hart.overwatch.apptestimonial.request.CreateAppTestimonialRequest;
import com.hart.overwatch.apptestimonial.request.UpdateAdminAppTestimonialRequest;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;

@Service
public class AppTestimonialService {

    private final AppTestimonialRepository appTestimonialRepository;

    private final UserService userService;

    private final PaginationService paginationService;

    @Autowired
    public AppTestimonialService(AppTestimonialRepository appTestimonialRepository,
            UserService userService, PaginationService paginationService) {
        this.appTestimonialRepository = appTestimonialRepository;
        this.userService = userService;
        this.paginationService = paginationService;
    }

    private AppTestimonial getAppTestimonialById(Long appTestimonialId) {
        return appTestimonialRepository.findById(appTestimonialId)
                .orElseThrow(() -> new NotFoundException(String
                        .format("Could not find app testimonial with id %d", appTestimonialId)));
    }


    public MinAppTestimonialDto getAppTestimonial() {
        User user = userService.getCurrentlyLoggedInUser();

        AppTestimonial appTestimonial =
                appTestimonialRepository.getAppTestimonialByUserId(user.getId());

        if (appTestimonial == null) {
            throw new NotFoundException("You don't seem to have written a testimonial yet");
        }

        MinAppTestimonialDto appTestimonialDto = new MinAppTestimonialDto();
        appTestimonialDto.setId(appTestimonial.getId());
        appTestimonialDto.setContent(appTestimonial.getContent());
        appTestimonialDto.setDeveloperType(appTestimonial.getDeveloperType());

        return appTestimonialDto;
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
        appTestimonial.setIsSelected(false);

        appTestimonialRepository.save(appTestimonial);
    }

    public void updateAppTestimonial(CreateAppTestimonialRequest request, Long appTestimonialId) {
        Long userId = request.getUserId();
        User user = userService.getUserById(userId);
        AppTestimonial appTestimonial = getAppTestimonialById(appTestimonialId);

        if (!user.getId().equals(appTestimonial.getUser().getId())) {
            throw new ForbiddenException("You cannot update another user's testimonial");
        }

        String developerType = Jsoup.clean(request.getDeveloperType(), Safelist.none());
        String content = Jsoup.clean(request.getContent(), Safelist.none());

        appTestimonial.setDeveloperType(developerType);
        appTestimonial.setContent(content);

        appTestimonialRepository.save(appTestimonial);
    }

    public void deleteAppTestimonial(Long appTestimonialId) {
        User user = userService.getCurrentlyLoggedInUser();
        AppTestimonial appTestimonial = getAppTestimonialById(appTestimonialId);

        if (!user.getId().equals(appTestimonial.getUser().getId())) {
            throw new ForbiddenException("You cannot delete another user's testimonial");
        }

        appTestimonialRepository.delete(appTestimonial);
    }

    public List<AppTestimonialDto> getAppTestimonials(Integer pageSize) {
        if (pageSize == null) {
            throw new BadRequestException("Missing page size requirement");
        }
        Pageable pageable = PageRequest.of(0, pageSize);
        Page<AppTestimonialDto> result = appTestimonialRepository.getAppTestimonials(pageable);

        return result.getContent();
    }


    public PaginationDto<AdminAppTestimonialDto> getAdminAppTestimonials(int page, int pageSize,
            String direction) {

        Pageable pageable = this.paginationService.getPageable(page, pageSize, direction);

        Page<AdminAppTestimonialDto> result =
                this.appTestimonialRepository.getAdminAppTestimonials(pageable);

        return new PaginationDto<AdminAppTestimonialDto>(result.getContent(), result.getNumber(),
                pageSize, result.getTotalPages(), direction, result.getTotalElements());
    }

    public Boolean updateAdminAppTestimonial(UpdateAdminAppTestimonialRequest request,
            Long appTestimonialId) {
        AppTestimonial appTestimonial = getAppTestimonialById(appTestimonialId);

        appTestimonial.setIsSelected(request.getIsSelected());

        appTestimonialRepository.save(appTestimonial);

        return appTestimonial.getIsSelected();
    }
}
