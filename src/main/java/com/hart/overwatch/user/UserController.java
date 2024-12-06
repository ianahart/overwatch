package com.hart.overwatch.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.user.dto.UserDto;
import com.hart.overwatch.user.request.DeleteUserRequest;
import com.hart.overwatch.user.request.UpdateUserPasswordRequest;
import com.hart.overwatch.user.request.UpdateUserRequest;
import com.hart.overwatch.user.response.DeleteUserResponse;
import com.hart.overwatch.user.response.GetAllUsersResponse;
import com.hart.overwatch.user.response.GetReviewersResponse;
import com.hart.overwatch.user.response.UpdateUserPasswordResponse;
import com.hart.overwatch.user.response.UpdateUserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/users")
public class UserController {

    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/search-all")
    public ResponseEntity<GetAllUsersResponse> searchAllUsers(@RequestParam("search") String search,
            @RequestParam("page") int page, @RequestParam("pageSize") int pageSize,
            @RequestParam("direction") String direction) {
        return ResponseEntity.status(HttpStatus.OK).body(new GetAllUsersResponse("success",
                userService.searchAllUsers(search, page, pageSize, direction)));
    }

    @GetMapping(path = "/search")
    public ResponseEntity<GetReviewersResponse> searchReviewers(
            @RequestParam("search") String search, @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize, @RequestParam("direction") String direction) {
        return ResponseEntity.status(HttpStatus.OK).body(new GetReviewersResponse("success",
                userService.searchReviewers(search, page, pageSize, direction)));
    }

    @GetMapping("/sync")
    public ResponseEntity<UserDto> syncUser(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new NotFoundException("Invalid header token");
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(this.userService.getUserByToken(authHeader.substring(7)));
    }

    @PatchMapping(path = "/{userId}/password")
    public ResponseEntity<UpdateUserPasswordResponse> updateUserPassword(
            @PathVariable("userId") Long userId, @RequestBody UpdateUserPasswordRequest request) {
        this.userService.updateUserPassword(request.getCurrentPassword(), request.getNewPassword(),
                userId);

        return ResponseEntity.status(HttpStatus.OK).body(new UpdateUserPasswordResponse("success"));
    }

    @PostMapping(path = "/{userId}/delete")
    public ResponseEntity<DeleteUserResponse> deleteUser(@PathVariable("userId") Long usersId,
            @RequestBody DeleteUserRequest request) {
        this.userService.deleteUser(usersId, request.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body(new DeleteUserResponse("success"));
    }

    @PatchMapping(path = "/{userId}")
    public ResponseEntity<UpdateUserResponse> updateUser(
            @Valid @RequestBody UpdateUserRequest request, @PathVariable("userId") Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new UpdateUserResponse("success", this.userService.updateUser(request, userId)));
    }
}


