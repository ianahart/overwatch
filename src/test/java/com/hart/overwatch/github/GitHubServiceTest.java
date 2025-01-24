package com.hart.overwatch.github;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.io.IOException;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hart.overwatch.github.dto.GitHubPaginationDto;
import com.hart.overwatch.github.dto.GitHubRepositoryDto;
import com.hart.overwatch.github.dto.GitHubTreeDto;
import org.springframework.test.util.ReflectionTestUtils;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class GitHubServiceTest {


    @InjectMocks
    private GitHubService gitHubService;

    @Mock
    private OkHttpClient mockClient;

    @Mock
    private Call mockCall;

    @Mock
    private Response mockResponse;

    @Mock
    private ResponseBody mockResponseBody;



    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(gitHubService, "clientId", "dumm-client-id");
        ReflectionTestUtils.setField(gitHubService, "clientSecret", "dummy-client-secret");
        ReflectionTestUtils.setField(gitHubService, "redirectUri", "dummy-redirect-uri");
        ReflectionTestUtils.setField(gitHubService, "tokenUri",
                "https://github.com/login/oauth/access_token");
    }

    @Test
    public void GitHubService_GetAccessToken_ReturnAccessToken() throws IOException {
        mockCall = mock(Call.class);
        mockResponse = mock(Response.class);

        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.body())
                .thenReturn(ResponseBody.create("{\"access_token\":\"dummy_token\"}",
                        MediaType.get("application/json; charset=utf-8")));

        when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);

        Long accessToken = gitHubService.getAccessToken("dummy_token");

        Assertions.assertThat(accessToken).isEqualTo(1L);

        verify(mockClient).newCall(any(Request.class));
        verify(mockCall).execute();
    }

    @Test
    public void GitHubService_GetUserRepos_ReturnGitHubPaginationDto() throws IOException {
        String accessToken = "dummy_token";
        int page = 1;
        String linkHeader = "<https://api.github.com/user/repos?page=2>; rel=\"next\"";
        String responseBody =
                new JSONArray().put(new JSONObject().put("id", 1L).put("full_name", "repository-1")
                        .put("owner", new JSONObject().put("avatar_url", "http://avatar.url"))
                        .put("html_url", "http://repository.url").put("language", "Java")
                        .put("stargazers_count", 100)).toString();

        when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockResponseBody.string()).thenReturn(responseBody);
        when(mockResponse.header("Link")).thenReturn(linkHeader);

        GitHubPaginationDto paginationDto = gitHubService.getUserRepos(1L, page);

        Assertions.assertThat(paginationDto.getNextPageUrl())
                .isEqualTo("https://api.github.com/user/repos?page=2");
        List<GitHubRepositoryDto> repos = paginationDto.getRepositories();
        Assertions.assertThat(repos.size()).isEqualTo(1);
        GitHubRepositoryDto repo = repos.get(0);
        Assertions.assertThat(repo.getId()).isEqualTo(1L);
        Assertions.assertThat(repo.getFullName()).isEqualTo("repository-1");
        Assertions.assertThat(repo.getAvatarUrl()).isEqualTo("http://avatar.url");
        Assertions.assertThat(repo.getHtmlUrl()).isEqualTo("http://repository.url");
        Assertions.assertThat(repo.getLanguage()).isEqualTo("Java");
        Assertions.assertThat(repo.getStargazersCount()).isEqualTo(100);
    }

    @Test
    public void GitHubService_GetRepository_ReturnGitHubTreeDto() throws IOException {
        String languagesResponseBody = "{ \"Java\": 1000, \"Python\": 500 }";
        String gitHubTreeResponseBody = "{ \"tree\": [ "
                + "{ \"path\": \"file1.txt\", \"type\": \"blob\", \"sha\": \"sha1\", \"url\": \"url1\" }, "
                + "{ \"path\": \"file2.txt\", \"type\": \"blob\", \"sha\": \"sha2\", \"url\": \"url2\" } ] }";

        String repoName = "owner/repository";
        String accessToken = "dummy_token";
        int page = 1;
        int size = 10;

        when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockResponseBody.string()).thenReturn(gitHubTreeResponseBody)
                .thenReturn(languagesResponseBody);

        GitHubTreeDto gitHubTreeDto =
                gitHubService.getRepository(repoName, accessToken, page, size);

        Assertions.assertThat(gitHubTreeDto.getLanguages()).isEqualTo(List.of("Java", "Python"));
    }

    @Test
    public void GitHubService_GetRepositoryFile_ReturnStringContents() throws IOException {
        String accessToken = "dummy_token";
        String path = "file/path";
        String owner = "owner";
        String repoName = "repo";
        String url = String.format("https://api.github.com/repos/%s/%s/contents/%s", owner,
                repoName, path);

        String mockResponseBodyString = "{\"content\":\"file_contents\"}";

        when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponseBody.string()).thenReturn(mockResponseBodyString);

        String result = gitHubService.getRepositoryFile(accessToken, path, owner, repoName);

        Assertions.assertThat(result).isEqualTo("file_contents");
    }
}


