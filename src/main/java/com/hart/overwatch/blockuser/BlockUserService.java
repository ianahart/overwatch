package com.hart.overwatch.blockuser;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.blockuser.dto.BlockUserDto;
import com.hart.overwatch.blockuser.request.CreateBlockUserRequest;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.advice.NotFoundException;

@Service
public class BlockUserService {

    private final BlockUserRepository blockUserRepository;

    private final UserService userService;

    private final UserRepository userRepository;

    private final PaginationService paginationService;

    @Autowired
    public BlockUserService(BlockUserRepository blockUserRepository, UserService userService,
            UserRepository userRepository, PaginationService paginationService) {
        this.blockUserRepository = blockUserRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.paginationService = paginationService;
    }


    private BlockUser getBlockUserById(Long blockUserId) {
        return blockUserRepository.findById(blockUserId).orElseThrow(() -> new NotFoundException(
                String.format("A block user with the id %d was not found", blockUserId)));
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

    public PaginationDto<BlockUserDto> getAllBlockedUsers(Long blockerUserId, int page,
            int pageSize, String direction) {

        Pageable pageable = this.paginationService.getPageable(page, pageSize, direction);

        Page<BlockUserDto> result =
                this.blockUserRepository.getAllBlockedUsers(pageable, blockerUserId);

        return new PaginationDto<BlockUserDto>(result.getContent(), result.getNumber(), pageSize,
                result.getTotalPages(), direction, result.getTotalElements());
    }

    public void deleteBlockUser(Long blockUserId) {
        if (blockUserId == null) {
            throw new BadRequestException("Missing block user id");
        }

        User user = userService.getCurrentlyLoggedInUser();
        BlockUser blockUser = getBlockUserById(blockUserId);

        if (!user.getId().equals(blockUser.getBlockerUser().getId())) {
            throw new ForbiddenException("Cannot unblock a user that you have not blocked");
        }

        blockUserRepository.delete(blockUser);
    }


}
