package com.hart.overwatch.badge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.badge.request.CreateBadgeRequest;
import com.hart.overwatch.badge.response.CreateBadgeResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "api/v1")
public class BadgeController {

    private final BadgeService badgeService;

    @Autowired
    public BadgeController(BadgeService badgeService) {
        this.badgeService = badgeService;
    }

    @PostMapping(path = "/admin/badges")
    public ResponseEntity<CreateBadgeResponse> createBadge(
            @Valid @ModelAttribute CreateBadgeRequest request) {
        badgeService.createBadge(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateBadgeResponse("success"));
    }
}
