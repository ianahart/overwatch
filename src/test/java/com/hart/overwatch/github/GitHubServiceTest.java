package com.hart.overwatch.github;

import static org.mockito.Mockito.*;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.connection.Connection;
import com.hart.overwatch.connection.ConnectionService;
import com.hart.overwatch.connection.RequestStatus;
import org.springframework.test.util.ReflectionTestUtils;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.OkHttpClient;
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

        String accessToken = gitHubService.getAccessToken("dummy_code");

        Assertions.assertThat(accessToken).isEqualTo("dummy_token");

        verify(mockClient).newCall(any(Request.class));
        verify(mockCall).execute();
    }
}


