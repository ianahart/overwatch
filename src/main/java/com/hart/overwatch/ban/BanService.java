package com.hart.overwatch.ban;

import java.time.LocalDateTime;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.ban.dto.BanDto;
import com.hart.overwatch.ban.request.CreateBanRequest;
import com.hart.overwatch.ban.request.UpdateBanRequest;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.advice.BadRequestException;

@Service
public class BanService {

    private final BanRepository banRepository;

    private final UserService userService;

    private final PaginationService paginationService;

    @Autowired
    public BanService(BanRepository banRepository, UserService userService,
            PaginationService paginationService) {
        this.banRepository = banRepository;
        this.userService = userService;
        this.paginationService = paginationService;
    }

    private Ban getBanByBanId(Long banId) {
        return banRepository.findById(banId).orElseThrow(() -> new NotFoundException(
                String.format("Could not find a ban with the id %d", banId)));
    }

    private boolean alreadyBanned(Long userId) {
        return banRepository.banExistsByUserId(userId);
    }

    private LocalDateTime createBanDate(Long seconds) {
        return LocalDateTime.now().plusSeconds(seconds);
    }

    public void createBan(CreateBanRequest request) {
        Long userId = request.getUserId();

        if (alreadyBanned(userId)) {
            throw new BadRequestException("You have already banned this user");
        }

        String adminNotes = Jsoup.clean(request.getAdminNotes(), Safelist.none());
        Long time = request.getTime();
        User user = userService.getUserById(userId);
        LocalDateTime banDate = createBanDate(time);

        Ban ban = new Ban(adminNotes, time, banDate, user);

        banRepository.save(ban);
    }

    public PaginationDto<BanDto> getBans(int page, int pageSize, String direction) {

        Pageable pageable = this.paginationService.getPageable(page, pageSize, direction);

        Page<BanDto> result = this.banRepository.getBans(pageable);

        return new PaginationDto<BanDto>(result.getContent(), result.getNumber(), pageSize,
                result.getTotalPages(), direction, result.getTotalElements());
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
        dto.setCreatedAt(ban.getCreatedAt());

        return dto;
    }

    public BanDto getBan(Long banId) {
        return convertToDto(getBanByBanId(banId));
    }

    public BanDto updateBan(Long banId, UpdateBanRequest request) {
        Ban ban = getBanByBanId(banId);
        String adminNotes = Jsoup.clean(request.getAdminNotes(), Safelist.none());
        Long time = request.getTime();
        LocalDateTime banDate = createBanDate(time);

        ban.setTime(time);
        ban.setBanDate(banDate);
        ban.setAdminNotes(adminNotes);

        banRepository.save(ban);

        return convertToDto(ban);
    }

    @Transactional
    public void deleteBan(Long banId) {
        Ban ban = getBanByBanId(banId);

        System.out.println(ban.getAdminNotes());
        if (ban != null) {
            banRepository.deleteById(banId);
        }
    }
}
