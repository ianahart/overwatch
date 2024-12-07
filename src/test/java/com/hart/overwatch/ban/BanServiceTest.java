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
}

