package com.hart.overwatch.workspace;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.workspace.dto.CreateWorkSpaceDto;
import com.hart.overwatch.workspace.dto.WorkSpaceDto;
import com.hart.overwatch.workspace.request.CreateWorkSpaceRequest;
import com.hart.overwatch.workspace.request.UpdateWorkSpaceRequest;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.advice.BadRequestException;

@Service
public class WorkSpaceService {

    private final int MAX_WORKSPACES = 10;

    private final WorkSpaceRepository workSpaceRepository;

    private final UserService userService;

    private final PaginationService paginationService;

    @Autowired
    public WorkSpaceService(WorkSpaceRepository workSpaceRepository, UserService userService,
            PaginationService paginationService) {
        this.workSpaceRepository = workSpaceRepository;
        this.userService = userService;
        this.paginationService = paginationService;
    }

    private WorkSpace getWorkSpaceById(Long workSpaceId) {
        return workSpaceRepository.findById(workSpaceId).orElseThrow(() -> new NotFoundException(
                String.format("A workspace with the id %d was not found", workSpaceId)));
    }

    private boolean alreadyExists(String title, Long userId) {
        return workSpaceRepository.alreadyExistsByTitleAndUserId(title, userId);
    }

    private long workSpaceCountByUser(Long userId) {
        return workSpaceRepository.countWorkSpacesByUserId(userId);
    }

    public CreateWorkSpaceDto createWorkSpace(CreateWorkSpaceRequest request) {

        if (alreadyExists(request.getTitle(), request.getUserId())) {
            throw new BadRequestException("You already have a workspace with that title");
        }

        if (workSpaceCountByUser(request.getUserId()) >= MAX_WORKSPACES) {
            throw new BadRequestException(
                    "You have the maximum amount of open workspaces. Please delete one.");
        }

        String cleanedTitle = Jsoup.clean(request.getTitle(), Safelist.none());
        String cleanedBackgroundColor = Jsoup.clean(request.getBackgroundColor(), Safelist.none());

        User user = userService.getUserById(request.getUserId());

        WorkSpace workSpace = new WorkSpace(cleanedTitle, cleanedBackgroundColor, user);

        workSpaceRepository.save(workSpace);

        return new CreateWorkSpaceDto(workSpace.getTitle(), workSpace.getBackgroundColor());
    }

    public PaginationDto<WorkSpaceDto> getWorkSpaces(Long userId, int page, int pageSize,
            String direction) {

        Pageable pageable = paginationService.getPageable(page, pageSize, direction);

        Page<WorkSpaceDto> result = workSpaceRepository.getWorkSpacesByUserId(pageable, userId);

        return new PaginationDto<WorkSpaceDto>(result.getContent(), result.getNumber(), pageSize,
                result.getTotalPages(), direction, result.getTotalElements());

    }


    public WorkSpaceDto updateWorkSpace(UpdateWorkSpaceRequest request, Long workSpaceId) {

        WorkSpace workSpace = getWorkSpaceById(workSpaceId);

        String cleanedTitle = Jsoup.clean(request.getTitle(), Safelist.none());
        String cleanedBackgroundColor = Jsoup.clean(request.getBackgroundColor(), Safelist.none());

        if (workSpace.getUser().getId() != request.getUserId()) {
            throw new BadRequestException("Cannot edit another user's workspace title");
        }

        workSpace.setTitle(cleanedTitle);
        workSpace.setBackgroundColor(cleanedBackgroundColor);

        workSpaceRepository.save(workSpace);

        return new WorkSpaceDto(workSpace.getId(), workSpace.getUser().getId(),
                workSpace.getCreatedAt(), workSpace.getTitle(), workSpace.getBackgroundColor());
    }


    public void deleteWorkSpace(Long workSpaceId) {
        User currentUser = userService.getCurrentlyLoggedInUser();
        WorkSpace workSpace = getWorkSpaceById(workSpaceId);

        if (currentUser.getId() != workSpace.getUser().getId()) {
            throw new ForbiddenException("Cannot delete a workspace that is not yours");
        }

        workSpaceRepository.delete(workSpace);
    }
}
