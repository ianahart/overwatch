package com.hart.overwatch.workspace;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.workspace.dto.CreateWorkSpaceDto;
import com.hart.overwatch.workspace.request.CreateWorkSpaceRequest;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.advice.BadRequestException;

@Service
public class WorkSpaceService {

    private final WorkSpaceRepository workSpaceRepository;

    private final UserService userService;

    @Autowired
    public WorkSpaceService(WorkSpaceRepository workSpaceRepository, UserService userService) {
        this.workSpaceRepository = workSpaceRepository;
        this.userService = userService;
    }

    private WorkSpace getWorkSpaceById(Long workSpaceId) {
        return workSpaceRepository.findById(workSpaceId).orElseThrow(() -> new NotFoundException(
                String.format("A workspace with the id %d was not found", workSpaceId)));
    }

    private boolean alreadyExists(String title, Long userId) {
        return workSpaceRepository.alreadyExistsByTitleAndUserId(title, userId);
    }

    public CreateWorkSpaceDto createWorkSpace(CreateWorkSpaceRequest request) {
        if (alreadyExists(request.getTitle(), request.getUserId())) {
            throw new BadRequestException("You already have a workspace with that title");
        }

        String cleanedTitle = Jsoup.clean(request.getTitle(), Safelist.none());
        String cleanedBackgroundColor = Jsoup.clean(request.getBackgroundColor(), Safelist.none());

        User user = userService.getUserById(request.getUserId());

        WorkSpace workSpace = new WorkSpace(cleanedTitle, cleanedBackgroundColor, user);

        workSpaceRepository.save(workSpace);

        return new CreateWorkSpaceDto(workSpace.getTitle(), workSpace.getBackgroundColor());
    }
}
