package com.hart.overwatch.stripepaymentintent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.io.IOException;
import java.nio.file.Path;
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
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.csv.CsvFileService;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.pdf.PdfFileService;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.stripepaymentintent.dto.FullStripePaymentIntentDto;
import com.hart.overwatch.stripepaymentintent.dto.StripePaymentIntentSearchResultDto;
import com.hart.overwatch.stripepaymentintent.projection.StripePaymentIntentApplicationFeeProjection;
import com.hart.overwatch.email.EmailQueueService;
import com.hart.overwatch.email.request.EmailRequest;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.stripe.model.PaymentIntent;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class StripePaymentIntentServiceTest {

    @InjectMocks
    private StripePaymentIntentService stripePaymentIntentService;

    @Mock
    private StripePaymentIntentRepository stripePaymentIntentRepository;

    @Mock
    private EmailQueueService emailQueueService;

    @Mock
    private CsvFileService csvFileService;

    @Mock
    private PaginationService paginationService;

    @Mock
    private PdfFileService pdfFileService;

    private User user;

    private User reviewer;

    private StripePaymentIntent stripePaymentIntent;

    private User createUser() {
        Boolean loggedIn = true;
        Profile profileEntity = new Profile();
        profileEntity.setAvatarUrl("test-avatar-url-01");
        profileEntity.setId(1L);
        User userEntity = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                profileEntity, "Test12345%", new Setting());
        userEntity.setId(1L);
        return userEntity;
    }

    private User createReviewer() {
        Boolean loggedIn = true;
        Profile profileEntity = new Profile();
        profileEntity.setAvatarUrl("test-avatar-url-02");
        profileEntity.setId(2L);
        User userEntity = new User("jane@mail.com", "Jane", "Doe", "Jane Doe", Role.USER, loggedIn,
                profileEntity, "Test12345%", new Setting());
        userEntity.setId(2L);
        return userEntity;
    }

    private StripePaymentIntent createStripePaymentItent(User user, User reviewer) {
        StripePaymentIntent stripePaymentIntentEntity = new StripePaymentIntent();
        stripePaymentIntentEntity.setId(1L);
        stripePaymentIntentEntity.setUser(user);
        stripePaymentIntentEntity.setReviewer(reviewer);
        stripePaymentIntentEntity.setAmount(10000L);
        stripePaymentIntentEntity.setCurrency("usd");
        stripePaymentIntentEntity.setDescription("description");
        stripePaymentIntentEntity.setClientSecret("client-secret");
        stripePaymentIntentEntity.setStatus(PaymentIntentStatus.PAID);
        stripePaymentIntentEntity.setApplicationFee(10000L);
        stripePaymentIntentEntity.setPaymentIntentId("stripe-paymentintent-id");


        return stripePaymentIntentEntity;
    }

    private FullStripePaymentIntentDto convertToDto(StripePaymentIntent stripePaymentIntent) {
        FullStripePaymentIntentDto dto = new FullStripePaymentIntentDto();
        dto.setId(stripePaymentIntent.getId());
        dto.setAmount(stripePaymentIntent.getAmount());
        dto.setUserId(stripePaymentIntent.getUser().getId());
        dto.setCurrency(stripePaymentIntent.getCurrency());
        dto.setReviewerId(stripePaymentIntent.getReviewer().getId());
        dto.setUserEmail(stripePaymentIntent.getUser().getEmail());
        dto.setDescription(stripePaymentIntent.getDescription());
        dto.setUserFullName(stripePaymentIntent.getUser().getFullName());
        dto.setStatus(stripePaymentIntent.getStatus());
        dto.setApplicationFee(stripePaymentIntent.getApplicationFee());
        dto.setReviewerEmail(stripePaymentIntent.getReviewer().getEmail());
        dto.setReviewerFullName(stripePaymentIntent.getReviewer().getFullName());

        return dto;
    }

    @BeforeEach
    public void setUp() {
        user = createUser();
        reviewer = createReviewer();
        stripePaymentIntent = createStripePaymentItent(user, reviewer);

    }

    @Test
    public void StripePaymentIntentService_GetStripePaymentIntentById_ThrowNotFoundException() {
        Long nonExistentStripePaymentIntentId = 999L;

        when(stripePaymentIntentRepository.findById(nonExistentStripePaymentIntentId))
                .thenReturn(Optional.ofNullable(null));

        Assertions.assertThatThrownBy(() -> {
            stripePaymentIntentService.getStripePaymentIntentById(nonExistentStripePaymentIntentId);
        }).isInstanceOf(NotFoundException.class)
                .hasMessage(String.format("Could not find stripe payment intent with the id %d",
                        nonExistentStripePaymentIntentId));
    }

    @Test
    public void StripePaymentIntentService_GetStripePaymentIntentById_ReturnStripePaymentIntent() {
        Long stripePaymentIntentId = stripePaymentIntent.getId();
        when(stripePaymentIntentRepository.findById(stripePaymentIntentId))
                .thenReturn(Optional.of(stripePaymentIntent));

        StripePaymentIntent returnedStripePaymentIntent =
                stripePaymentIntentService.getStripePaymentIntentById(stripePaymentIntentId);

        Assertions.assertThatNoException();
        Assertions.assertThat(returnedStripePaymentIntent.getId())
                .isEqualTo(stripePaymentIntent.getId());
        Assertions.assertThat(returnedStripePaymentIntent.getAmount())
                .isEqualTo(stripePaymentIntent.getAmount());
        Assertions.assertThat(returnedStripePaymentIntent.getApplicationFee())
                .isEqualTo(stripePaymentIntent.getApplicationFee());
        Assertions.assertThat(returnedStripePaymentIntent.getCurrency())
                .isEqualTo(stripePaymentIntent.getCurrency());
        Assertions.assertThat(returnedStripePaymentIntent.getDescription())
                .isEqualTo(stripePaymentIntent.getDescription());
        Assertions.assertThat(returnedStripePaymentIntent.getClientSecret())
                .isEqualTo(stripePaymentIntent.getClientSecret());
        Assertions.assertThat(returnedStripePaymentIntent.getPaymentIntentId())
                .isEqualTo(stripePaymentIntent.getPaymentIntentId());
        Assertions.assertThat(returnedStripePaymentIntent.getStatus())
                .isEqualTo(stripePaymentIntent.getStatus());

    }

    @Test
    public void StripePaymentItentService_CreateStripePaymentIntent_ReturnNothing() {
        StripePaymentIntent newStripePaymentIntent = new StripePaymentIntent();
        newStripePaymentIntent.setId(2L);
        PaymentIntent paymentIntent = new PaymentIntent();
        paymentIntent.setId("pi_test");
        paymentIntent.setCurrency("usd");
        paymentIntent.setAmount(5000L);
        paymentIntent.setClientSecret("secret_test");
        paymentIntent.setDescription("Code Review Package");

        Long applicationFee = 500L;

        when(stripePaymentIntentRepository.save(any(StripePaymentIntent.class)))
                .thenReturn(newStripePaymentIntent);

        stripePaymentIntentService.createStripePaymentIntent(user, reviewer, paymentIntent,
                applicationFee);

        ArgumentCaptor<StripePaymentIntent> intentCaptor =
                ArgumentCaptor.forClass(StripePaymentIntent.class);
        verify(stripePaymentIntentRepository, times(1)).save(intentCaptor.capture());

        StripePaymentIntent capturedIntent = intentCaptor.getValue();
        Assertions.assertThat(capturedIntent).isNotNull();
        Assertions.assertThat(capturedIntent.getUser()).isEqualTo(user);
        Assertions.assertThat(capturedIntent.getReviewer()).isEqualTo(reviewer);
        Assertions.assertThat(capturedIntent.getCurrency()).isEqualTo("usd");
        Assertions.assertThat(capturedIntent.getPaymentIntentId()).isEqualTo("pi_test");
        Assertions.assertThat(capturedIntent.getAmount()).isEqualTo(5000L);
        Assertions.assertThat(capturedIntent.getApplicationFee()).isEqualTo(applicationFee);
        Assertions.assertThat(capturedIntent.getStatus()).isEqualTo(PaymentIntentStatus.PAID);
        Assertions.assertThat(capturedIntent.getClientSecret()).isEqualTo("secret_test");
        Assertions.assertThat(capturedIntent.getDescription()).isEqualTo("Code Review Package");

        String expectedReviewerText =
                "John Doe has paid you $50.00 usd for reviewing their code!\n package:Code Review Package";
        String expectedUserText =
                "You paid Jane Doe $50.00 usd for reviewing your code!\n package: Code Review Package";

        ArgumentCaptor<EmailRequest> emailCaptor = ArgumentCaptor.forClass(EmailRequest.class);
        verify(emailQueueService, times(2)).queueEmail(emailCaptor.capture());

        List<EmailRequest> queuedEmails = emailCaptor.getAllValues();
        Assertions.assertThat(queuedEmails).hasSize(2);
        Assertions.assertThat(queuedEmails.get(0).getTo()).isEqualTo(reviewer.getEmail());
        Assertions.assertThat(queuedEmails.get(0).getSubject()).isEqualTo("Payment Recieved");
        Assertions.assertThat(queuedEmails.get(0).getBody()).isEqualTo(expectedReviewerText);
        Assertions.assertThat(queuedEmails.get(1).getTo()).isEqualTo(user.getEmail());
        Assertions.assertThat(queuedEmails.get(1).getSubject()).isEqualTo("Payment Notification");
        Assertions.assertThat(queuedEmails.get(1).getBody()).isEqualTo(expectedUserText);
    }

    @Test
    public void StripePaymentIntentService_GetAllStripePaymentIntents_Search() {
        int page = 0;
        int pageSize = 3;
        String direction = "next";
        String search = "john doe";
        Pageable pageable = Pageable.ofSize(pageSize);
        FullStripePaymentIntentDto stripePaymentIntentDto = convertToDto(stripePaymentIntent);
        Page<FullStripePaymentIntentDto> pageResult =
                new PageImpl<>(Collections.singletonList(stripePaymentIntentDto), pageable, 1);
        PaginationDto<FullStripePaymentIntentDto> expectedPaginationDto =
                new PaginationDto<>(pageResult.getContent(), pageResult.getNumber(), pageSize,
                        pageResult.getTotalPages(), direction, pageResult.getTotalElements());

        when(paginationService.getPageable(page, pageSize, direction)).thenReturn(pageable);
        when(stripePaymentIntentRepository.getStripePaymentIntentsBySearch(pageable, search))
                .thenReturn(pageResult);
        when(stripePaymentIntentRepository.findAllBy()).thenReturn(List.of());

        StripePaymentIntentSearchResultDto result = stripePaymentIntentService
                .getAllStripePaymentIntents(search, page, pageSize, direction);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getResult().getPage())
                .isEqualTo(expectedPaginationDto.getPage());
        Assertions.assertThat(result.getResult().getPageSize())
                .isEqualTo(expectedPaginationDto.getPageSize());
        Assertions.assertThat(result.getResult().getTotalPages())
                .isEqualTo(expectedPaginationDto.getTotalPages());
        Assertions.assertThat(result.getResult().getTotalElements())
                .isEqualTo(expectedPaginationDto.getTotalElements());
        Assertions.assertThat(result.getResult().getDirection())
                .isEqualTo(expectedPaginationDto.getDirection());
        Assertions.assertThat(result.getResult().getItems()).hasSize(1);
        FullStripePaymentIntentDto actualStripePaymentIntentDto =
                result.getResult().getItems().get(0);
        Assertions.assertThat(actualStripePaymentIntentDto.getId())
                .isEqualTo(stripePaymentIntentDto.getId());
        Assertions.assertThat(actualStripePaymentIntentDto.getAmount())
                .isEqualTo(stripePaymentIntentDto.getAmount());
        Assertions.assertThat(actualStripePaymentIntentDto.getUserId())
                .isEqualTo(stripePaymentIntentDto.getUserId());
        Assertions.assertThat(actualStripePaymentIntentDto.getReviewerId())
                .isEqualTo(stripePaymentIntentDto.getReviewerId());
        Assertions.assertThat(actualStripePaymentIntentDto.getApplicationFee())
                .isEqualTo(stripePaymentIntentDto.getApplicationFee());
        Assertions.assertThat(actualStripePaymentIntentDto.getCurrency())
                .isEqualTo(stripePaymentIntentDto.getCurrency());
        Assertions.assertThat(actualStripePaymentIntentDto.getUserEmail())
                .isEqualTo(stripePaymentIntentDto.getUserEmail());
        Assertions.assertThat(actualStripePaymentIntentDto.getDescription())
                .isEqualTo(stripePaymentIntentDto.getDescription());
        Assertions.assertThat(actualStripePaymentIntentDto.getUserFullName())
                .isEqualTo(stripePaymentIntentDto.getUserFullName());
        Assertions.assertThat(actualStripePaymentIntentDto.getReviewerEmail())
                .isEqualTo(stripePaymentIntentDto.getReviewerEmail());
        Assertions.assertThat(actualStripePaymentIntentDto.getReviewerFullName())
                .isEqualTo(stripePaymentIntentDto.getReviewerFullName());
        Assertions.assertThat(actualStripePaymentIntentDto.getStatus())
                .isEqualTo(stripePaymentIntentDto.getStatus());
    }

    @Test
    public void StripePaymentIntentService_GetAllStripePaymentIntents_All() {
        int page = 0;
        int pageSize = 3;
        String direction = "next";
        String search = "all";
        Pageable pageable = Pageable.ofSize(pageSize);
        FullStripePaymentIntentDto stripePaymentIntentDto = convertToDto(stripePaymentIntent);
        Page<FullStripePaymentIntentDto> pageResult =
                new PageImpl<>(Collections.singletonList(stripePaymentIntentDto), pageable, 1);
        PaginationDto<FullStripePaymentIntentDto> expectedPaginationDto =
                new PaginationDto<>(pageResult.getContent(), pageResult.getNumber(), pageSize,
                        pageResult.getTotalPages(), direction, pageResult.getTotalElements());

        when(paginationService.getPageable(page, pageSize, direction)).thenReturn(pageable);
        when(stripePaymentIntentRepository.getStripePaymentIntentsBySearch(pageable, ""))
                .thenReturn(pageResult);
        when(stripePaymentIntentRepository.findAllBy()).thenReturn(List.of());

        StripePaymentIntentSearchResultDto result = stripePaymentIntentService
                .getAllStripePaymentIntents(search, page, pageSize, direction);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getResult().getPage())
                .isEqualTo(expectedPaginationDto.getPage());
        Assertions.assertThat(result.getResult().getPageSize())
                .isEqualTo(expectedPaginationDto.getPageSize());
        Assertions.assertThat(result.getResult().getTotalPages())
                .isEqualTo(expectedPaginationDto.getTotalPages());
        Assertions.assertThat(result.getResult().getTotalElements())
                .isEqualTo(expectedPaginationDto.getTotalElements());
        Assertions.assertThat(result.getResult().getDirection())
                .isEqualTo(expectedPaginationDto.getDirection());
        Assertions.assertThat(result.getResult().getItems()).hasSize(1);
        FullStripePaymentIntentDto actualStripePaymentIntentDto =
                result.getResult().getItems().get(0);
        Assertions.assertThat(actualStripePaymentIntentDto.getId())
                .isEqualTo(stripePaymentIntentDto.getId());
        Assertions.assertThat(actualStripePaymentIntentDto.getAmount())
                .isEqualTo(stripePaymentIntentDto.getAmount());
        Assertions.assertThat(actualStripePaymentIntentDto.getUserId())
                .isEqualTo(stripePaymentIntentDto.getUserId());
        Assertions.assertThat(actualStripePaymentIntentDto.getReviewerId())
                .isEqualTo(stripePaymentIntentDto.getReviewerId());
        Assertions.assertThat(actualStripePaymentIntentDto.getApplicationFee())
                .isEqualTo(stripePaymentIntentDto.getApplicationFee());
        Assertions.assertThat(actualStripePaymentIntentDto.getCurrency())
                .isEqualTo(stripePaymentIntentDto.getCurrency());
        Assertions.assertThat(actualStripePaymentIntentDto.getUserEmail())
                .isEqualTo(stripePaymentIntentDto.getUserEmail());
        Assertions.assertThat(actualStripePaymentIntentDto.getDescription())
                .isEqualTo(stripePaymentIntentDto.getDescription());
        Assertions.assertThat(actualStripePaymentIntentDto.getUserFullName())
                .isEqualTo(stripePaymentIntentDto.getUserFullName());
        Assertions.assertThat(actualStripePaymentIntentDto.getReviewerEmail())
                .isEqualTo(stripePaymentIntentDto.getReviewerEmail());
        Assertions.assertThat(actualStripePaymentIntentDto.getReviewerFullName())
                .isEqualTo(stripePaymentIntentDto.getReviewerFullName());
        Assertions.assertThat(actualStripePaymentIntentDto.getStatus())
                .isEqualTo(stripePaymentIntentDto.getStatus());
    }

    @Test
    public void StripePaymentIntentService_ExportStripePaymentIntentsToPdf_ReturnNothing()
            throws IOException {
        int page = 0;
        int pageSize = 3;
        String direction = "next";
        HttpServletResponse response = mock(HttpServletResponse.class);
        String search = "all";
        Pageable pageable = Pageable.ofSize(pageSize);
        FullStripePaymentIntentDto stripePaymentIntentDto = convertToDto(stripePaymentIntent);
        Page<FullStripePaymentIntentDto> pageResult =
                new PageImpl<>(Collections.singletonList(stripePaymentIntentDto), pageable, 1);

        when(paginationService.getPageable(page, pageSize, direction)).thenReturn(pageable);
        when(stripePaymentIntentRepository.getStripePaymentIntentsBySearch(pageable, ""))
                .thenReturn(pageResult);

        stripePaymentIntentService.exportStripePaymentIntentsToPdf(response, search, page, pageSize,
                direction);

        verify(stripePaymentIntentRepository, times(1)).getStripePaymentIntentsBySearch(pageable,
                "");
        verify(pdfFileService, times(1)).generatePdfFile(eq(response),
                eq(List.of(stripePaymentIntentDto)));
    }

    @Test
    public void StripePaymentIntentService_ExportStripePaymentIntentsToCsv_ReturnNothing()
            throws IOException {
        int page = 0;
        int pageSize = 3;
        String direction = "next";
        String search = "all";
        Pageable pageable = Pageable.ofSize(pageSize);
        String fileName = "transactions.csv";
        Path expectedPath = Path.of(fileName);
        FullStripePaymentIntentDto stripePaymentIntentDto = convertToDto(stripePaymentIntent);
        Page<FullStripePaymentIntentDto> pageResult =
                new PageImpl<>(Collections.singletonList(stripePaymentIntentDto), pageable, 1);

        when(paginationService.getPageable(page, pageSize, direction)).thenReturn(pageable);
        when(stripePaymentIntentRepository.getStripePaymentIntentsBySearch(pageable, ""))
                .thenReturn(pageResult);

        when(csvFileService.generateCsvFile(fileName, List.of(stripePaymentIntentDto)))
                .thenReturn(expectedPath);
        Path actualPath = stripePaymentIntentService.exportStripePaymentIntentsToCsv(search, page,
                pageSize, direction);

        Assertions.assertThat(actualPath).isEqualTo(expectedPath);
        verify(csvFileService, times(1)).generateCsvFile(fileName, List.of(stripePaymentIntentDto));
    }


}
