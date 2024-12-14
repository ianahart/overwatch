package com.hart.overwatch.github;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.hart.overwatch.github.dto.GitHubPaginationDto;
import com.hart.overwatch.github.dto.GitHubRepositoryDto;
import com.hart.overwatch.github.dto.GitHubTreeDto;
import com.hart.overwatch.github.dto.GitHubTreeNodeDto;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collections;

@Service
public class GitHubService {

    private final OkHttpClient okHttpClient;

    @Value("${GITHUB_CLIENT_ID}")
    private String clientId;

    @Value("${GITHUB_CLIENT_SECRET}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.github.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.provider.github.token-uri}")
    private String tokenUri;

    @Autowired
    public GitHubService(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    public String getAccessToken(String code) {
        String json = String.format(
                "{\"client_id\":\"%s\", \"client_secret\":\"%s\", \"code\":\"%s\", \"redirect_uri\":\"%s\"}",
                clientId, clientSecret, code, redirectUri);

        RequestBody body =
                RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder().url(tokenUri).header("Accept", "application/json")
                .post(body).build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);
                return jsonObject.getString("access_token");
            } else {
                throw new RuntimeException("Failed to get access token: " + response.message());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to get access token", e);
        }
    }



    protected Map<String, String> makeGitHubRequest(String url, String accessToken)

            throws IOException {
        Request request = new Request.Builder().url(url)
                .header("Authorization", "Bearer " + accessToken).build();

        Response response = this.okHttpClient.newCall(request).execute();
//        String rateLimitRemaining = response.header("X-RateLimit-Remaining");
//        String rateLimitReset = response.header("X-RateLimit-Reset");
//        System.out.println("Remaining requests: " + rateLimitRemaining);
//        System.out.println("Reset time: " + rateLimitReset);
//
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        Map<String, String> result = new HashMap<>();

        result.put("body", response.body().string());
        result.put("link", response.header("Link"));


        return result;
    }


    private List<GitHubRepositoryDto> constructRepos(String jsonStr) {
        List<GitHubRepositoryDto> repos = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(jsonStr);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject repo = jsonArray.getJSONObject(i);

            Long id = repo.getLong("id");
            String fullName = repo.isNull("full_name") ? "" : repo.getString("full_name");
            JSONObject owner = repo.getJSONObject("owner");
            String avatarUrl = owner.isNull("avatar_url") ? "" : owner.getString("avatar_url");
            String htmlUrl = repo.getString("html_url");
            String language = repo.isNull("language") ? "" : repo.getString("language");
            Integer stargazersCount = repo.getInt("stargazers_count");

            repos.add(new GitHubRepositoryDto(id, fullName, avatarUrl, htmlUrl, language,
                    stargazersCount));
        }
        return repos;
    }


    private Map<String, String> parseLinkHeader(String linkHeader) {
        Map<String, String> links = new HashMap<>();
        if (linkHeader != null) {
            String[] parts = linkHeader.split(", ");
            Pattern pattern = Pattern.compile("<(.+?)>; rel=\"(.+?)\"");
            for (String part : parts) {
                Matcher matcher = pattern.matcher(part);
                if (matcher.matches()) {
                    links.put(matcher.group(2), matcher.group(1));
                }
            }
        }
        return links;
    }


    public GitHubTreeDto searchRepository(String accessToken, int page, String query,
            String repoPath, int size) throws IOException {
        String url = String.format("https://api.github.com/search/code?page=%d&q=%s", page, query);

        try {
            Map<String, String> result = makeGitHubRequest(url, accessToken);
            String treeData = result.getOrDefault("body", "{}");

            List<String> languages = new ArrayList<>();
            return new GitHubTreeDto(languages, createFileTree(treeData, page, size, "items"));
        } catch (IOException e) {
            throw new RuntimeException("GitHub request failed", e);
        }
    }

    public GitHubPaginationDto getUserRepos(String accessToken, int page) throws IOException {

        String url = String.format("https://api.github.com/user/repos?page=%d", page);
        Map<String, String> result = makeGitHubRequest(url, accessToken);

        Map<String, String> links = parseLinkHeader(result.get("link"));

        String nextPageUrl = links.containsKey("next") ? links.get("next") : "";
        List<GitHubRepositoryDto> data = constructRepos(result.get("body"));

        return new GitHubPaginationDto(nextPageUrl, data);

    }

    private List<GitHubTreeNodeDto> createFileTree(String jsonData, int page, int size,
            String type) {
        JSONObject jsonObject = new JSONObject(jsonData);
        JSONArray treeArray = jsonObject.optJSONArray(type);

        if (treeArray == null || treeArray.isEmpty()) {
            System.out.println(treeArray + " EMPTY TREE");
            return Collections.emptyList();
        }
        System.out.println("treeArray: " + treeArray.length());


        List<GitHubTreeNodeDto> treeNodes = new ArrayList<>();
        for (int i = 0; i < treeArray.length(); i++) {
            JSONObject treeObject = treeArray.getJSONObject(i);

            GitHubTreeNodeDto treeNode = new GitHubTreeNodeDto(treeObject.getString("path"),
                    treeObject.optString("type", "node"), treeObject.getString("sha"),
                    treeObject.optInt("size", 0), treeObject.getString("url"));
            System.out.println("TreeNode created: " + treeNode);
            treeNodes.add(treeNode);
        }
        System.out.println("Total nodes added: " + treeNodes.size());

        if (page < 0) {
            page = 0;
        }
        int startIndex = page * size; // Calculate the starting index

        // If starting index is out of bounds, return an empty list
        if (startIndex >= treeNodes.size()) {
            System.out.println("Start index out of bounds.");
            return List.of();
        }

        // Calculate the safe end index
        int endIndex = Math.min(startIndex + size, treeNodes.size());

        System.out.println("Start index: " + startIndex);
        System.out.println("End index: " + endIndex);
        System.out.println("Total nodes: " + treeNodes.size());

        // Return the paginated sublist
        return treeNodes.subList(startIndex, endIndex);

        // int start = page * size;
        // int end = Math.min(start + size, treeNodes.size());

        // System.out.println("Start: " + start);
        // System.out.println("End: " + end);

        // // Ensure start is within bounds
        // if (start >= treeNodes.size()) {
        // System.out.println("Start index out of bounds. Returning empty list.");
        // return Collections.emptyList();
        // }

        // System.out.println("Returning sublist from index " + start + " to " + end);
        // return treeNodes.subList(start, end);
    }

    public GitHubTreeDto getRepository(String repoName, String accessToken, int page, int size)
            throws IOException {
        String[] ownerAndRepoName = repoName.split("/");
        String url = String.format("https://api.github.com/repos/%s/%s/git/trees/main?recursive=1",
                ownerAndRepoName[0], ownerAndRepoName[1]);
        Map<String, String> result = makeGitHubRequest(url, accessToken);

        String languagesUrl = String.format("https://api.github.com/repos/%s/%s/languages",
                ownerAndRepoName[0], ownerAndRepoName[1]);
        Map<String, String> languagesResult = makeGitHubRequest(languagesUrl, accessToken);

        List<String> languages = constructLanguages(languagesResult.get("body"));


        return new GitHubTreeDto(languages, createFileTree(result.get("body"), page, size, "tree"));
    }

    private String constructContent(String jsonData) {
        try {

            JSONObject jsonObject = new JSONObject(jsonData);
            String content = jsonObject.getString("content");

            return content;

        } catch (JSONException ex) {
            return "";
        }

    }

    private List<String> constructLanguages(String jsonData) {
        JSONObject jsonObject = new JSONObject(jsonData);
        List<String> languages = new ArrayList<>();

        Iterator<String> keys = jsonObject.keys();

        while (keys.hasNext()) {
            languages.add(keys.next());
        }
        return languages;
    }

    public String getRepositoryFile(String accessToken, String path, String owner, String repoName)
            throws IOException {
        String contentsUrl = String.format("https://api.github.com/repos/%s/%s/contents/%s", owner,
                repoName, path);

        Map<String, String> contentsResult = makeGitHubRequest(contentsUrl, accessToken);

        return constructContent(contentsResult.get("body"));


    }
}

