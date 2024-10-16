package com.hart.overwatch.blockuser;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.blockuser.request.CreateBlockUserRequest;
import com.hart.overwatch.advice.BadRequestException;

@Service
public class BlockUserService {

    private final BlockUserRepository blockUserRepository;

    private final UserService userService;

    private final UserRepository userRepository;

    @Autowired
    public BlockUserService(BlockUserRepository blockUserRepository, UserService userService,
            UserRepository userRepository) {
        this.blockUserRepository = blockUserRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    private boolean blockUserAlreadyExists(Long blockedUserId, Long blockerUserId) {
        return blockUserRepository.findByBlockedUserIdAndBlockerUserId(blockedUserId,
                blockerUserId);
    }

    public List<Long> getBlockedUsersForCurUser(User user) {
        if (user == null) {
            return Collections.emptyList();
        }
        return user.getBlockerUsers().stream()
                .map(blockedUser -> blockedUser.getBlockedUser().getId())
                .collect(Collectors.toList());
    }

    public BlockUser getCurUserBlockedByUser(Long userId, Long blockerUserId) {
        User user = userRepository.findUserWithBlockerUsers(blockerUserId);
        if (user == null) {
            return null;
        }


        return user.getBlockerUsers().stream()
                .filter(blocker -> blocker.getBlockedUser().getId().equals(userId)).findFirst()
                .orElse(null);
    }

    public void createBlockUser(CreateBlockUserRequest request) {

        if (blockUserAlreadyExists(request.getBlockedUserId(), request.getBlockerUserId())) {
            throw new BadRequestException("You have already blocked this user");
        }

        User blockedUser = userService.getUserById(request.getBlockedUserId());
        User blockerUser = userService.getUserById(request.getBlockerUserId());

        if (blockedUser == null || blockerUser == null) {
            throw new NotFoundException("Could not find either blocked user or blocker user");
        }

        BlockUser newBlockUser = new BlockUser();

        newBlockUser.setBlockedUser(blockedUser);
        newBlockUser.setBlockerUser(blockerUser);

        blockUserRepository.save(newBlockUser);
    }

}
