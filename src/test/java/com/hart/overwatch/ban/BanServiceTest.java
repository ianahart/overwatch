package com.hart.overwatch.ban;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.ban.dto.BanDto;
import com.hart.overwatch.ban.request.CreateBanRequest;
import com.hart.overwatch.csv.CsvFileService;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.stripe.model.PaymentIntent;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class BanServiceTest {

    @InjectMocks
    private BanService banService;

    @Mock
    private BanRepository banRepository;

    @Mock
    private UserService userService;

    @Mock
    private PaginationService paginationService;

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
    public void BanService_CreateBan_ThrowBadRequestException() {
        CreateBanRequest request = new CreateBanRequest();
        request.setTime(ban.getTime());
        request.setUserId(user.getId());
        request.setAdminNotes("admin notes");

        when(banRepository.banExistsByUserId(request.getUserId())).thenReturn(true);

        Assertions.assertThatThrownBy(() -> {
            banService.createBan(request);
        }).isInstanceOf(BadRequestException.class).hasMessage("You have already banned this user");
    }


    @Test
    public void BanService_CreateBan_ReturnNothing() {
        CreateBanRequest request = new CreateBanRequest();
        request.setTime(ban.getTime());
        request.setUserId(user.getId());
        request.setAdminNotes("admin notes");

        when(banRepository.banExistsByUserId(request.getUserId())).thenReturn(false);
        when(userService.getUserById(request.getUserId())).thenReturn(user);

        Ban newBan = new Ban();
        newBan.setTime(request.getTime());
        newBan.setUser(user);
        newBan.setAdminNotes(request.getAdminNotes());
        newBan.setBanDate(LocalDateTime.now().plusSeconds(request.getTime()));

        when(banRepository.save(any(Ban.class))).thenReturn(newBan);

        banService.createBan(request);

        verify(userService, times(1)).getUserById(request.getUserId());
        verify(banRepository, times(1)).save(any(Ban.class));
    }

    @Test
    public void BanService_GetBans_ReturnPaginationDtoOfBanDto() {
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

        when(paginationService.getPageable(page, pageSize, direction)).thenReturn(pageable);
        when(banRepository.getBans(pageable)).thenReturn(pageactualPaginationDto);

        PaginationDto<BanDto> actualPaginationDto = banService.getBans(page, pageSize, direction);

        Assertions.assertThat(actualPaginationDto).isNotNull();
        Assertions.assertThat(actualPaginationDto.getPage())
                .isEqualTo(expectedPaginationDto.getPage());
        Assertions.assertThat(actualPaginationDto.getPageSize())
                .isEqualTo(expectedPaginationDto.getPageSize());
        Assertions.assertThat(actualPaginationDto.getTotalPages())
                .isEqualTo(expectedPaginationDto.getTotalPages());
        Assertions.assertThat(actualPaginationDto.getTotalElements())
                .isEqualTo(expectedPaginationDto.getTotalElements());
        Assertions.assertThat(actualPaginationDto.getDirection())
                .isEqualTo(expectedPaginationDto.getDirection());
        Assertions.assertThat(actualPaginationDto.getItems()).hasSize(1);
        BanDto actualBanDto = actualPaginationDto.getItems().get(0);
        Assertions.assertThat(actualBanDto.getId()).isEqualTo(banDto.getId());
        Assertions.assertThat(actualBanDto.getTime()).isEqualTo(banDto.getTime());
        Assertions.assertThat(actualBanDto.getUserId()).isEqualTo(banDto.getUserId());
        Assertions.assertThat(actualBanDto.getBanDate()).isEqualTo(banDto.getBanDate());
        Assertions.assertThat(actualBanDto.getEmail()).isEqualTo(banDto.getEmail());
        Assertions.assertThat(actualBanDto.getFullName()).isEqualTo(banDto.getFullName());
        Assertions.assertThat(actualBanDto.getAdminNotes()).isEqualTo(banDto.getAdminNotes());
    }


}

