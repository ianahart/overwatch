package com.hart.overwatch.topic;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hart.overwatch.config.DatabaseSetupService;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.tag.Tag;
import com.hart.overwatch.tag.dto.TagDto;
import com.hart.overwatch.token.TokenRepository;
import com.hart.overwatch.topic.dto.TopicDto;
import com.hart.overwatch.topic.request.CreateTopicRequest;
import com.hart.overwatch.topicmanagement.TopicManagementService;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;


@ActiveProfiles("test")
@WebMvcTest(controllers = TopicController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class TopicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TopicService topicService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private TopicManagementService topicManagementService;

    @MockBean
    private DatabaseSetupService databaseSetupService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private TokenRepository tokenRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Topic topic;

    private User user;

    private List<Tag> tags = new ArrayList<>();


    private User createUser() {
        Boolean loggedIn = true;
        User userEntity = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        userEntity.setId(1L);
        return userEntity;
    }

    private Topic createTopic(User user) {
        Topic topicEntity = new Topic("title", "description", user);
        topicEntity.setId(1L);
        return topicEntity;
    }

    private List<Tag> createTags() {
        List<Tag> tagEntities = new ArrayList<>();
        tagEntities.add(new Tag(1L, "spring boot"));
        tagEntities.add(new Tag(2L, "java"));
        return tagEntities;
    }


    private TopicDto createTopicDto(Topic topic) {
        TopicDto topicDto = new TopicDto();
        topicDto.setId(topic.getId());
        topicDto.setTitle(topic.getTitle());
        topicDto.setDescription(topic.getDescription());
        topicDto.setTotalCommentCount(1);
        topicDto.setTags(List.of(new TagDto(2L, "java")));

        return topicDto;
    }

    @BeforeEach
    public void setUp() {
        databaseSetupService.createTsvectorTrigger();
        user = createUser();
        topic = createTopic(user);
        tags = createTags();

        topic.setTags(tags);
        tags.forEach(tag -> tag.setTopics(List.of(topic)));
    }

    @Test
    public void TopicController_SearchTopics_ReturnGetTopicsReponse() throws Exception {
        String query = "title";

        TopicDto topicDto = createTopicDto(topic);

        when(topicService.searchTopics(query)).thenReturn(List.of(topicDto));

        ResultActions response =
                mockMvc.perform(get("/api/v1/topics/search").param("query", query));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id",
                        CoreMatchers.is(topicDto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].title",
                        CoreMatchers.is(topicDto.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].description",
                        CoreMatchers.is(topicDto.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].totalCommentCount",
                        CoreMatchers.is(topicDto.getTotalCommentCount())));

    }

    @Test
    public void TopicController_GetTopic_ReturnGetTopicResponse() throws Exception {
        Long topicId = topic.getId();

        TopicDto topicDto = createTopicDto(topic);
        when(topicService.getTopic(topicId)).thenReturn(topicDto);

        ResultActions response = mockMvc.perform(get(String.format("/api/v1/topics/%d", topicId)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id",
                        CoreMatchers.is(topicDto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title",
                        CoreMatchers.is(topicDto.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.description",
                        CoreMatchers.is(topicDto.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalCommentCount",
                        CoreMatchers.is(topicDto.getTotalCommentCount())));

    }

    @Test
    public void TopicController_CreateTopic_ReturnCreateTopicResponse() throws Exception {
        CreateTopicRequest request = new CreateTopicRequest();
        request.setTitle("new title");
        request.setDescription("new description");
        request.setUserId(user.getId());
        request.setTags(List.of("java", "spring boot"));

        doNothing().when(topicManagementService).handleCreateTopic(request);

        ResultActions response =
                mockMvc.perform(post("/api/v1/topics").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }

    @Test
    public void TopicController_GetAllTopics_ReturnGetAllTopicsResponse() throws Exception {
        int page = 0;
        int pageSize = 3;
        String direction = "next";

        Pageable pageable = Pageable.ofSize(pageSize).withPage(page);

        Page<Topic> pageResult = new PageImpl<>(Collections.singletonList(topic), pageable, 1);
        List<TopicDto> topicDtos = List.of(createTopicDto(topic));

        PaginationDto<TopicDto> expectedPaginationDto =
                new PaginationDto<>(topicDtos, pageResult.getNumber(), pageSize,
                        pageResult.getTotalPages(), direction, pageResult.getTotalElements());

        when(topicService.getTopics(page, pageSize, direction)).thenReturn(expectedPaginationDto);

        ResultActions response = mockMvc.perform(get("/api/v1/topics").param("page", "0")
                .param("pageSize", "3").param("direction", "next"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.items[0].id",
                        CoreMatchers.is(topicDtos.get(0).getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.items[0].title",
                        CoreMatchers.is(topicDtos.get(0).getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.items[0].description",
                        CoreMatchers.is(topicDtos.get(0).getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.items[0].tags",
                        Matchers.hasSize(topicDtos.size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.items[0].totalCommentCount",
                        CoreMatchers.is(topicDtos.get(0).getTotalCommentCount())));
    }

    @Test
    public void TopicController_GetAllTopicsWithTags_ReturnGetAllTopicsResponse() throws Exception {
        String query = "title";
        int page = 0;
        int pageSize = 3;
        String direction = "next";

        Pageable pageable = Pageable.ofSize(pageSize).withPage(page);

        Page<Topic> pageResult = new PageImpl<>(Collections.singletonList(topic), pageable, 1);
        List<TopicDto> topicDtos = List.of(createTopicDto(topic));

        PaginationDto<TopicDto> expectedPaginationDto =
                new PaginationDto<>(topicDtos, pageResult.getNumber(), pageSize,
                        pageResult.getTotalPages(), direction, pageResult.getTotalElements());

        when(topicService.getTopicsWithTags(page, pageSize, direction, query))
                .thenReturn(expectedPaginationDto);

        ResultActions response = mockMvc.perform(get("/api/v1/topics/tags").param("page", "0")
                .param("pageSize", "3").param("direction", "next").param("query", query));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.items[0].id",
                        CoreMatchers.is(topicDtos.get(0).getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.items[0].title",
                        CoreMatchers.is(topicDtos.get(0).getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.items[0].description",
                        CoreMatchers.is(topicDtos.get(0).getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.items[0].tags",
                        Matchers.hasSize(topicDtos.size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.items[0].totalCommentCount",
                        CoreMatchers.is(topicDtos.get(0).getTotalCommentCount())));
    }

}


