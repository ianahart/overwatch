package com.hart.overwatch.teampost;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.team.Team;
import com.hart.overwatch.team.TeamService;
import com.hart.overwatch.teampost.dto.TeamPostDto;
import com.hart.overwatch.teampost.request.CreateTeamPostRequest;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.advice.ForbiddenException;

@Service
public class TeamPostService {

    private final TeamPostRepository teamPostRepository;

    private final UserService userService;

    private final TeamService teamService;

    private final PaginationService paginationService;

    @Autowired
    public TeamPostService(TeamPostRepository teamPostRepository, UserService userService,
            TeamService teamService, PaginationService paginationService) {
        this.teamPostRepository = teamPostRepository;
        this.userService = userService;
        this.teamService = teamService;
        this.paginationService = paginationService;
    }

    private TeamPost getTeamPostByTeamPostId(Long teamPostId) {
        return teamPostRepository.findById(teamPostId).orElseThrow(() -> new NotFoundException(
                String.format("Could not find a team post with id %d", teamPostId)));
    }


    private String santizeCode(String code) {
        Safelist safelist = Safelist.basic().addTags("pre", "code").addAttributes("pre", "class")
                .addAttributes("code", "class");

        String wrappedCode = "<pre><code>" + code + "</code></pre>";
        String sanitizedHtml = Jsoup.clean(wrappedCode, safelist);

        Document document = Jsoup.parseBodyFragment(sanitizedHtml);
        document.outputSettings().prettyPrint(true).indentAmount(4);

        return document.body().html();
    }

    public void createTeamPost(CreateTeamPostRequest request, Long teamId) {

        String sanitizedCode = santizeCode(request.getCode());

        String language = Jsoup.clean(request.getLanguage(), Safelist.none());
        User user = userService.getUserById(request.getUserId());
        Team team = teamService.getTeamByTeamId(teamId);
        Boolean isEdited = false;

        TeamPost teamPost = new TeamPost(sanitizedCode, language, isEdited, user, team);

        teamPostRepository.save(teamPost);
    }


    public PaginationDto<TeamPostDto> getTeamPosts(Long teamId, int page, int pageSize,
            String direction) {

        Pageable pageable = this.paginationService.getPageable(page, pageSize, direction);

        Page<TeamPostDto> result = this.teamPostRepository.getTeamPostsByTeamId(pageable, teamId);

        return new PaginationDto<TeamPostDto>(result.getContent(), result.getNumber(), pageSize,
                result.getTotalPages(), direction, result.getTotalElements());
    }

    public void deleteTeamPost(Long teamPostId) {
        User user = userService.getCurrentlyLoggedInUser();
        TeamPost teamPost = getTeamPostByTeamPostId(teamPostId);

        if (!user.getId().equals(teamPost.getUser().getId())) {
            throw new ForbiddenException(
                    "You do not have the permission to delete another user's post");
        }
        teamPostRepository.delete(teamPost);
    }

}
