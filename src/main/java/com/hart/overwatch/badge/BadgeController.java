package com.hart.overwatch.badge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.badge.request.CreateBadgeRequest;
import com.hart.overwatch.badge.request.UpdateBadgeRequest;
import com.hart.overwatch.badge.response.CreateBadgeResponse;
import com.hart.overwatch.badge.response.DeleteBadgeResponse;
import com.hart.overwatch.badge.response.GetAllBadgesResponse;
import com.hart.overwatch.badge.response.GetBadgeResponse;
import com.hart.overwatch.badge.response.UpdateBadgeResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "api/v1/admin/badges")
public class BadgeController {

    private final BadgeService badgeService;

    @Autowired
    public BadgeController(BadgeService badgeService) {
        this.badgeService = badgeService;
    }

    @PostMapping(path = "")
    public ResponseEntity<CreateBadgeResponse> createBadge(
            @Valid @ModelAttribute CreateBadgeRequest request) {
        badgeService.createBadge(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateBadgeResponse("success"));
    }

    @GetMapping(path = "")
    public ResponseEntity<GetAllBadgesResponse> getBadges(@RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize, @RequestParam("direction") String direction) {

        return ResponseEntity.status(HttpStatus.OK).body(new GetAllBadgesResponse("success",
                badgeService.getBadges(page, pageSize, direction)));
    }

    @GetMapping(path = "/{badgeId}")
    public ResponseEntity<GetBadgeResponse> getBadge(@PathVariable("badgeId") Long badgeId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new GetBadgeResponse("success", badgeService.getBadge(badgeId)));
    }

    @PatchMapping(path = "/{badgeId}")
    public ResponseEntity<UpdateBadgeResponse> updateBadge(
            @Valid @ModelAttribute UpdateBadgeRequest request,
            @PathVariable("badgeId") Long badgeId) {
        badgeService.updateBadge(request, badgeId);
        return ResponseEntity.status(HttpStatus.OK).body(new UpdateBadgeResponse("success"));
    }

    @DeleteMapping(path = "/{badgeId}")
    public ResponseEntity<DeleteBadgeResponse> deleteBadge(@PathVariable("badgeId") Long badgeId) {
        badgeService.deleteBadge(badgeId);
        return ResponseEntity.status(HttpStatus.OK).body(new DeleteBadgeResponse("success"));
    }
}
