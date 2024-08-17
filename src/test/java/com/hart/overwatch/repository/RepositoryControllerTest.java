package com.hart.overwatch.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.connection.RequestStatus;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.repository.dto.RepositoryDto;
import com.hart.overwatch.repository.request.CreateUserRepositoryRequest;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.token.TokenRepository;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import org.hamcrest.CoreMatchers;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ActiveProfiles("test")
@WebMvcTest(controllers = RepositoryController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class RepositoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RepositoryService repositoryService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private TokenRepository tokenRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User owner;

    private User reviewer;

    private Repository repository;

    private RepositoryDto reviewerRepositoryDto;

    private RepositoryDto ownerRepositoryDto;

    private PaginationDto<RepositoryDto> reviewerPaginationDto;

    private PaginationDto<RepositoryDto> ownerPaginationDto;

    private Page<RepositoryDto> pageResult;


    private User createOwner() {
        boolean loggedIn = true;
        Profile profile = new Profile();
        profile.setAvatarUrl("http://avatar.url/1");
        profile.setId(1L);
        owner = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn, profile,
                "Test12345%", new Setting());
        owner.setId(1L);

        return owner;
    }

    private User createReviewer() {
        boolean loggedIn = true;
        Profile profile = new Profile();
        profile.setAvatarUrl("http://avatar.url/2");
        profile.setId(2L);

        reviewer = new User("jane@mail.com", "Jane", "Doe", "Jane Doe", Role.REVIEWER, loggedIn,
                profile, "Test12345%", new Setting());
        reviewer.setId(2L);

        return reviewer;
    }

    private Repository createRepository(User owner, User reviewer) {
        Repository repository = new Repository();

        repository.setFeedback("some feedback");
        repository.setComment("here is some comment");
        repository.setAvatarUrl("https://github.com/avatarurl");
        repository.setRepoUrl("https://github.com/user/repo");
        repository.setRepoName("repoName");
        repository.setLanguage("Java");
        repository.setStatus(RepositoryStatus.INCOMPLETE);
        repository.setReviewer(reviewer);
        repository.setOwner(owner);

        repository.setId(1L);

        return repository;
    }

    private RepositoryDto createRepositoryDto(User owner, User reviewer, Repository repository,
            Role role) {
        RepositoryDto repositoryDto = new RepositoryDto();

        repositoryDto.setId(repository.getId());
        repositoryDto.setOwnerId(owner.getId());
        repositoryDto.setReviewerId(reviewer.getId());
        repositoryDto
                .setFirstName(role == Role.USER ? reviewer.getFirstName() : owner.getFirstName());
        repositoryDto.setLastName(role == Role.USER ? reviewer.getLastName() : owner.getLastName());
        repositoryDto.setProfileUrl(role == Role.USER ? reviewer.getProfile().getAvatarUrl()
                : owner.getProfile().getAvatarUrl());
        repositoryDto.setRepoName(repository.getRepoName());
        repositoryDto.setLanguage(repository.getLanguage());
        repositoryDto.setRepoUrl(repository.getRepoUrl());
        repositoryDto.setAvatarUrl(repository.getAvatarUrl());
        repositoryDto.setCreatedAt(LocalDateTime.now());
        repositoryDto.setStatus(repository.getStatus());

        return repositoryDto;
    }

    private Pageable createPageable(int pageSize) {
        return PageRequest.of(0, pageSize);
    }

    private PaginationDto<RepositoryDto> createPaginationDto(RepositoryDto repositoryDto) {
        int pageSize = 3;
        String direction = "next";
        Pageable pageable = createPageable(pageSize);

        pageResult = new PageImpl<>(Collections.singletonList(repositoryDto), pageable, 1);
        PaginationDto<RepositoryDto> expectedPaginationDto =
                new PaginationDto<>(pageResult.getContent(), pageResult.getNumber(), pageSize,
                        pageResult.getTotalPages(), direction, pageResult.getTotalElements());

        return expectedPaginationDto;
    }

    @BeforeEach
    public void setUp() {
        owner = createOwner();
        reviewer = createReviewer();
        repository = createRepository(owner, reviewer);
        reviewerRepositoryDto =
                createRepositoryDto(owner, reviewer, repository, reviewer.getRole());
        ownerRepositoryDto = createRepositoryDto(owner, reviewer, repository, owner.getRole());
        reviewerPaginationDto = createPaginationDto(reviewerRepositoryDto);
        ownerPaginationDto = createPaginationDto(ownerRepositoryDto);
    }

    @Test
    public void RepositoryController_CreateUserRepository_ReturnCreateUserRepositoryResponse()
            throws Exception {
        CreateUserRepositoryRequest request = new CreateUserRepositoryRequest();
        request.setReviewerId(reviewer.getId());
        request.setOwnerId(owner.getId());
        request.setRepoName(repository.getRepoName());
        request.setRepoUrl(repository.getRepoUrl());
        request.setComment(repository.getComment());
        request.setLanguage(repository.getLanguage());

        doNothing().when(repositoryService).handleCreateUserRepository(request);

        ResultActions response = mockMvc
                .perform(post("/api/v1/repositories/user").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }

    @Test
    public void RepositoryController_GetDistinctRepositoryLanguages_ReturnGetDistinctRepositoryLanguagesResponse() throws Exception {
        when(repositoryService.getDistinctRepositoryLanguages()).thenReturn(List.of("Java", "Python", "HTML"));

        ResultActions response = mockMvc.perform(get("/api/v1/repositories/languages").contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data", CoreMatchers.hasItems("Java","Python", "HTML")));
    }

    @Test
    public void RepositoryController_GetAllRepositories_ReturnGetAllRepositoriesResponse() throws Exception{

        when(repositoryService.getAllRepositories(0, 3, "next", "desc", RepositoryStatus.INCOMPLETE, "all")).thenReturn(ownerPaginationDto);

        ResultActions response = mockMvc.perform(get("/api/v1/repositories").contentType(MediaType.APPLICATION_JSON).param("page", "0")
        .param("pageSize", "3").param("direction", "next").param("sort", "desc").param("status", "INCOMPLETE").param("language", "all"));

        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }

    @Test
    public void RepositoryController_DeleteRepository_ReturnDeleteRepositoryResponse()
            throws Exception {
        doNothing().when(repositoryService).deleteRepository(repository.getId());

        ResultActions response = mockMvc
                .perform(delete("/api/v1/repositories/1").contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));

        verify(repositoryService, times(1)).deleteRepository(repository.getId());
    }

    @Test
    public void RepositoryController_GetRepositoryComment_ReturnGetRepositoryCommentResponse() throws Exception {
        when(repositoryService.getRepositoryComment(repository.getId())).thenReturn(repository.getComment());

        ResultActions response = mockMvc.perform(get("/api/v1/repositories/1/comment").contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data", CoreMatchers.is(repository.getComment())));
    }
}


