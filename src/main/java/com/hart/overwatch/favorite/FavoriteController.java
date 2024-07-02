package com.hart.overwatch.favorite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.favorite.request.ToggleFavoriteRequest;
import com.hart.overwatch.favorite.response.ToggleFavoriteResponse;

@RestController
@RequestMapping(path = "/api/v1/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @Autowired
    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping(path = "")
    public ResponseEntity<ToggleFavoriteResponse> toggleFavorite(
            @RequestBody ToggleFavoriteRequest request) {
        this.favoriteService.toggleFavorite(request.getIsFavorited(), request.getUserId(),
                request.getProfileId());
        return ResponseEntity.status(HttpStatus.OK).body(new ToggleFavoriteResponse("success"));
    }
}
