package com.hart.overwatch.badge;

import java.util.HashMap;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.hart.overwatch.amazon.AmazonService;
import com.hart.overwatch.badge.request.CreateBadgeRequest;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.advice.BadRequestException;

@Service
public class BadgeService {

    private final String BUCKET_NAME = "arrow-date";

    private final AmazonService amazonService;

    private final BadgeRepository badgeRepository;

    @Autowired
    public BadgeService(AmazonService amazonService, BadgeRepository badgeRepository) {
        this.amazonService = amazonService;
        this.badgeRepository = badgeRepository;
    }

    private Badge getBadgeById(Long badgeId) {
        return badgeRepository.findById(badgeId).orElseThrow(() -> new NotFoundException(
                String.format("Could not find badge with id %d", badgeId)));
    }


    private boolean badgeAlreadyExists(String title) {
        return badgeRepository.existsByTitle(title.toLowerCase());
    }

    public void createBadge(CreateBadgeRequest request) {
        String title = Jsoup.clean(request.getTitle(), Safelist.none());
        String description = Jsoup.clean(request.getDescription(), Safelist.none());
        MultipartFile image = request.getImage();

        if (badgeAlreadyExists(title)) {
            throw new BadRequestException(
                    String.format("You have already created a badge with the title %s", title));
        }

        HashMap<String, String> result =
                amazonService.putS3Object(BUCKET_NAME, image.getOriginalFilename(), image);
        String imageFileName = result.get("filename");
        String imageUrl = result.get("objectUrl");

        Badge badge = new Badge(imageFileName, imageUrl, title, description);

        badgeRepository.save(badge);
    }


}
