package com.hart.overwatch.team;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.config.DatabaseSetupService;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.tag.Tag;
import com.hart.overwatch.tag.dto.TagDto;
import com.hart.overwatch.team.request.CreateTeamRequest;
import com.hart.overwatch.topic.dto.TopicDto;
import com.hart.overwatch.topic.request.CreateTopicRequest;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

    @InjectMocks
    private TeamService teamService;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserService userService;

    @Mock
    private PaginationService paginationService;

    private User user;

    private Team team;

    private User createUser() {
        Boolean loggedIn = true;
        User userEntity = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        userEntity.setId(1L);
        return userEntity;
    }

    private Team createTeam(User user) {
        Team teamEntity = new Team();
        teamEntity.setUser(user);
        teamEntity.setTeamName("team one");
        teamEntity.setTeamDescription("team description");
        teamEntity.setId(1L);

        return teamEntity;
    }


    @BeforeEach
    public void setUp() {
        user = createUser();
        team = createTeam(user);
    }

    @Test
    public void TeamService_CreateTeam_ThrowBadRequestException_MaxTeams() {
        CreateTeamRequest request = new CreateTeamRequest();
        request.setUserId(user.getId());
        request.setTeamName(team.getTeamName());
        request.setTeamDescription(team.getTeamDescription());
        Long MAX_TEAMS = 10L;

        when(teamRepository.getTeamCountByUserId(request.getUserId())).thenReturn(MAX_TEAMS);

        Assertions.assertThatThrownBy(() -> {
            teamService.createTeam(request);
        }).isInstanceOf(BadRequestException.class).hasMessage(String.format("You have already added the maximum amount of teams (%d)", MAX_TEAMS));
    }


}


