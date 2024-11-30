package com.hart.overwatch.teamcomment;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.teamcomment.dto.TeamCommentDto;
import com.hart.overwatch.teamcomment.request.CreateTeamCommentRequest;
import com.hart.overwatch.teampost.TeamPost;
import com.hart.overwatch.teampost.TeamPostService;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.advice.BadRequestException;

@Service
public class TeamCommentService {

    private final TeamCommentRepository teamCommentRepository;

    private TeamPostService teamPostService;

    private UserService userService;

    private PaginationService paginationService;

    @Autowired
    public TeamCommentService(TeamCommentRepository teamCommentRepository,
            TeamPostService teamPostService, UserService userService,
            PaginationService paginationService) {
        this.teamCommentRepository = teamCommentRepository;
        this.teamPostService = teamPostService;
        this.userService = userService;
        this.paginationService = paginationService;
    }

    private TeamComment getTeamCommentByTeamCommentId(Long teamCommentId) {
        return teamCommentRepository.findById(teamCommentId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("could not find team comment with id %d", teamCommentId)));
    }

    public void createTeamComment(CreateTeamCommentRequest request, Long teamPostId) {
        String content = Jsoup.clean(request.getContent(), Safelist.none());
        User user = userService.getUserById(request.getUserId());
        TeamPost teamPost = teamPostService.getTeamPostByTeamPostId(teamPostId);
        Boolean isEdited = false;

        TeamComment teamComment = new TeamComment(isEdited, content, user, teamPost);

        teamCommentRepository.save(teamComment);
    }

    public PaginationDto<TeamCommentDto> getTeamComments(Long teamPostId, int page, int pageSize,
            String direction) {
        Pageable pageable = this.paginationService.getPageable(page, pageSize, direction);

        Page<TeamCommentDto> result =
                this.teamCommentRepository.getTeamCommentsByTeamPostId(pageable, teamPostId);

        return new PaginationDto<TeamCommentDto>(result.getContent(), result.getNumber(), pageSize,
                result.getTotalPages(), direction, result.getTotalElements());
    }

}
