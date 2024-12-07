package com.hart.overwatch.ban;

import java.time.LocalDateTime;
import java.util.Collections;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hart.overwatch.ban.dto.BanDto;
import com.hart.overwatch.ban.request.CreateBanRequest;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.token.TokenRepository;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;


@DirtiesContext
@ActiveProfiles("test")
@WebMvcTest(controllers = BanController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class BanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BanService banService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private TokenRepository tokenRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    private Ban ban;

    private User createUser() {
        Boolean loggedIn = true;
        Profile profileEntity = new Profile();
        User userEntity = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                profileEntity, "Test12345%", new Setting());
        userEntity.setId(1L);
        return userEntity;
    }

    private Ban createBan(User user) {
        Ban banEntity = new Ban();
        banEntity.setId(1L);
        banEntity.setTime(86400L);
        banEntity.setUser(user);
        banEntity.setAdminNotes("admin notes");
        banEntity.setBanDate(LocalDateTime.now().plusSeconds(86400));


        return banEntity;
    }

    private BanDto convertToDto(Ban ban) {
        BanDto dto = new BanDto();
        dto.setId(ban.getId());
        dto.setTime(ban.getTime());
        dto.setEmail(ban.getUser().getEmail());
        dto.setUserId(ban.getUser().getId());
        dto.setFullName(ban.getUser().getFullName());
        dto.setAdminNotes(ban.getAdminNotes());
        dto.setBanDate(ban.getBanDate());

        return dto;
    }

    @BeforeEach
    public void setUp() {
        user = createUser();
        ban = createBan(user);
    }

    @Test
    public void BanController_CreateBan_ReturnCreateBanResponse() throws Exception {
        CreateBanRequest request = new CreateBanRequest();
        request.setTime(ban.getTime());
        request.setUserId(user.getId());
        request.setAdminNotes(ban.getAdminNotes());

        doNothing().when(banService).createBan(request);

        ResultActions response = mockMvc
                .perform(post("/api/v1/admin/banned-users").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }

    @Test
    public void BanController_GetBans_ReturnGetAllBansResponse() throws Exception {
        int page = 0;
        int pageSize = 3;
        String direction = "next";
        Pageable pageable = Pageable.ofSize(pageSize);
        BanDto banDto = convertToDto(ban);
        Page<BanDto> pageactualPaginationDto =
                new PageImpl<>(Collections.singletonList(banDto), pageable, 1);
        PaginationDto<BanDto> expectedPaginationDto = new PaginationDto<>(
                pageactualPaginationDto.getContent(), pageactualPaginationDto.getNumber(), pageSize,
                pageactualPaginationDto.getTotalPages(), direction,
                pageactualPaginationDto.getTotalElements());

        when(banService.getBans(page, pageSize, direction)).thenReturn(expectedPaginationDto);

        ResultActions response = mockMvc.perform(get("/api/v1/admin/banned-users")
                .param("page", "0").param("pageSize", "3").param("direction", "next"));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.page",
                        CoreMatchers.is(expectedPaginationDto.getPage())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize",
                        CoreMatchers.is(expectedPaginationDto.getPageSize())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalPages",
                        CoreMatchers.is(expectedPaginationDto.getTotalPages())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.page",
                        CoreMatchers.is(expectedPaginationDto.getPage())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalElements",
                        CoreMatchers.is(Math.toIntExact(expectedPaginationDto.getTotalElements()))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.items",
                        Matchers.hasSize(Math.toIntExact(1L))));

    }

    @Test
    public void BanController_GetBan_ReturnGetBanResponse() throws Exception {
        Long banId = ban.getId();
        BanDto banDto = convertToDto(ban);
        when(banService.getBan(banId)).thenReturn(banDto);

        ResultActions response =
                mockMvc.perform(get(String.format("/api/v1/admin/banned-users/%d", banId)));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id",
                        CoreMatchers.is(banDto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.userId",
                        CoreMatchers.is(banDto.getUserId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.time",
                        CoreMatchers.is(banDto.getTime().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email",
                        CoreMatchers.is(banDto.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.fullName",
                        CoreMatchers.is(banDto.getFullName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.adminNotes",
                        CoreMatchers.is(banDto.getAdminNotes())));
    }

}


