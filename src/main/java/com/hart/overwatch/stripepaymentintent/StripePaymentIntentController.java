package com.hart.overwatch.stripepaymentintent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.stripepaymentintent.response.GetAllSearchStripePaymentIntentResponse;
import com.hart.overwatch.stripepaymentintent.response.GetAllStripePaymentIntentResponse;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(path = "/api/v1")
public class StripePaymentIntentController {

    private final StripePaymentIntentService stripePaymentIntentService;

    public StripePaymentIntentController(StripePaymentIntentService stripePaymentIntentService) {
        this.stripePaymentIntentService = stripePaymentIntentService;
    }

    @GetMapping(path = "/users/{userId}/payment-intents")
    public ResponseEntity<GetAllStripePaymentIntentResponse> getUserStripePaymentIntents(
            @PathVariable("userId") Long userId, @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize, @RequestParam("direction") String direction) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GetAllStripePaymentIntentResponse("success", stripePaymentIntentService
                        .getUserStripePaymentIntents(userId, page, pageSize, direction)));
    }

    @GetMapping(path = "/admin/payment-intents")
    public ResponseEntity<GetAllSearchStripePaymentIntentResponse> getSearchStripePaymentIntents(
            @RequestParam("search") String search, @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize, @RequestParam("direction") String direction) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new GetAllSearchStripePaymentIntentResponse("success", stripePaymentIntentService
                        .getAllStripePaymentIntents(search, page, pageSize, direction)));
    }

    @GetMapping(path = "/admin/payment-intents/export-pdf")
    public void exportStripePaymentIntentsToPdf(HttpServletResponse response,
            @RequestParam("search") String search, @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize, @RequestParam("direction") String direction)
            throws IOException {
        stripePaymentIntentService.exportStripePaymentIntentsToPdf(response, search, page, pageSize,
                direction);
    }


    @GetMapping(path = "/admin/payment-intents/export-csv")
    public ResponseEntity<InputStreamResource> exportStripePaymentIntentsToCsv(
            @RequestParam("search") String search, @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize, @RequestParam("direction") String direction)
            throws IOException {
        Path csvFile = stripePaymentIntentService.exportStripePaymentIntentsToCsv(search, page,
                pageSize, direction);

        InputStreamResource resource = new InputStreamResource(Files.newInputStream(csvFile));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=transactions.csv")
                .contentType(MediaType.parseMediaType("text/csv")).body(resource);
    }

}
