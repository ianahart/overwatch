package com.hart.overwatch.blockuser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.blockuser.request.CreateBlockUserRequest;
import com.hart.overwatch.blockuser.response.CreateBlockUserResponse;
import com.hart.overwatch.blockuser.response.DeleteBlockUserResponse;
import com.hart.overwatch.blockuser.response.GetAllBlockUserResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/block-users")
public class BlockUserController {

    private final BlockUserService blockUserService;

    @Autowired
    public BlockUserController(BlockUserService blockUserService) {
        this.blockUserService = blockUserService;
    }


    @PostMapping("")
    public ResponseEntity<CreateBlockUserResponse> createBlockUser(
            @Valid @RequestBody CreateBlockUserRequest request) {
        blockUserService.createBlockUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateBlockUserResponse("success"));
    }

    @GetMapping("")
    public ResponseEntity<GetAllBlockUserResponse> getAllBlockUsers(
            @RequestParam("blockerUserId") Long blockerUserId, @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize, @RequestParam("direction") String direction) {

        return ResponseEntity.status(HttpStatus.OK).body(new GetAllBlockUserResponse("success",
                blockUserService.getAllBlockedUsers(blockerUserId, page, pageSize, direction)));
    }

    @DeleteMapping("/{blockUserId}")
    public ResponseEntity<DeleteBlockUserResponse> deleteBlockUser(@PathVariable("blockUserId") Long blockUserId) {
        blockUserService.deleteBlockUser(blockUserId);
        return ResponseEntity.status(HttpStatus.OK).body(new DeleteBlockUserResponse("success"));
    }

}
