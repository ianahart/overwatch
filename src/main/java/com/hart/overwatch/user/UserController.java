package com.hart.overwatch.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.user.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/api/v1/users")
public class UserController {

    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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

}

