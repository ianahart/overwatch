package com.hart.overwatch.pagination;

import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentCaptor.forClass;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class PaginationServiceTest {

    @InjectMocks
    private PaginationService paginationService;

    @BeforeEach
    void setUp() {}


    @Test
    public void PaginationService_PaginateNext_ReturnInt() {
        int page = 0;
        String direction = "next";

        int result = paginationService.paginate(page, direction);

        Assertions.assertThat(result).isEqualTo(1);
    }

    @Test
    public void PaginationService_PaginatePrev_ReturnInt() {
        int page = 2;
        String direction = "prev";

        int result = paginationService.paginate(page, direction);

        Assertions.assertThat(result).isEqualTo(1);
    }

    @Test
    public void PaginationService_GetPageable_ReturnPageable() {
        int page = 1;
        int pageSize = 3;
        String direction = "next";

        Pageable pageable = paginationService.getPageable(page, pageSize, direction);

        Assertions.assertThat(pageable).isNotNull();
        Assertions.assertThat(pageable.getPageSize()).isEqualTo(pageSize);
        Assertions.assertThat(pageable.getPageNumber()).isEqualTo(page + 1);
    }
}


