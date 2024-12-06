package com.hart.overwatch.ban;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.ban.request.CreateBanRequest;
import com.hart.overwatch.ban.request.UpdateBanRequest;
import com.hart.overwatch.ban.response.CreateBanResponse;
import com.hart.overwatch.ban.response.DeleteBanResponse;
import com.hart.overwatch.ban.response.GetAllBansResponse;
import com.hart.overwatch.ban.response.GetBanResponse;
import com.hart.overwatch.ban.response.UpdateBanResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/admin/banned-users")
public class BanController {

    private final BanService banService;

    @Autowired
    public BanController(BanService banService) {
        this.banService = banService;
    }

    @PostMapping(path = "")
    public ResponseEntity<CreateBanResponse> createBan(
            @Valid @RequestBody CreateBanRequest request) {
        banService.createBan(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateBanResponse("success"));
    }

    @GetMapping(path = "")
    public ResponseEntity<GetAllBansResponse> getBans(@RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize, @RequestParam("direction") String direction) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new GetAllBansResponse("success", banService.getBans(page, pageSize, direction)));
    }

    @GetMapping(path = "/{banId}")
    public ResponseEntity<GetBanResponse> getBan(@PathVariable("banId") Long banId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new GetBanResponse("success", banService.getBan(banId)));
    }

    @PatchMapping(path = "/{banId}")
    public ResponseEntity<UpdateBanResponse> updateBan(@Valid @RequestBody UpdateBanRequest request,
            @PathVariable("banId") Long banId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new UpdateBanResponse("success", banService.updateBan(banId, request)));
    }

    @DeleteMapping(path = "/{banId}")
    public ResponseEntity<DeleteBanResponse> deleteBan(@PathVariable("banId") Long banId) {
        banService.deleteBan(banId);
        return ResponseEntity.status(HttpStatus.OK).body(new DeleteBanResponse("success"));
    }
}
