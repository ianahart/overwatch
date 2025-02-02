package com.hart.overwatch.github;

import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.github.dto.GitHubPaginationDto;
import com.hart.overwatch.github.dto.GitHubRepositoryDto;
import com.hart.overwatch.token.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.hamcrest.CoreMatchers;
import java.util.List;

@ActiveProfiles("test")
@WebMvcTest(controllers = GitHubController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class GitHubControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GitHubService gitHubService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private TokenRepository tokenRepository;

    private GitHubPaginationDto gitHubPaginationDto;

    private GitHubRepositoryDto createGitHubRepositoryDto() {
        return new GitHubRepositoryDto(1L, "fullName", "http://avatar.url", "http://html.url",
                "Java", 100);
    }


    @BeforeEach
    public void setUp() {
        List<GitHubRepositoryDto> repositories = List.of(createGitHubRepositoryDto());
        String nextPageUrl = "https://api.github.com/user/repos?page=2";
        gitHubPaginationDto = new GitHubPaginationDto(nextPageUrl, repositories);
    }

    @Test
    public void GitHubController_GetAccessToken_Return_FetchGitHubAccessTokenResponse()
            throws Exception {
        Long githubId = 1L;
        when(gitHubService.getAccessToken("dummy_code")).thenReturn(githubId);

        ResultActions response = mockMvc.perform(get("/api/v1/github/auth")
                .contentType(MediaType.APPLICATION_JSON).param("code", "dummy_code"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.githubId",
                        CoreMatchers.is(githubId.intValue())));
    }

    @Test
    public void GitHubController_GetUserRepos_ReturnFetchGitHubUserReposResponse() throws Exception {
        when(gitHubService.getUserRepos(1L, 1)).thenReturn(gitHubPaginationDto);

        ResultActions response = mockMvc.perform(get("/api/v1/github/user/repos").contentType(MediaType.APPLICATION_JSON).header("GitHub-Token", "dummy_token").param("page", "1").param("githubId", "1"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.repositories[0].id", CoreMatchers.is(1)));
    }
}


