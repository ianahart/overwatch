package com.hart.overwatch.pagination;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Test
    public void PaginationService_GetSortedPageableAscending_ReturnPageable() {
        int page = 1;
        int pageSize = 3;
        String direction = "next";
        String order = "asc";

        Pageable sortedPageable =
                paginationService.getSortedPageable(page, pageSize, direction, order);
        Pageable expectedSortedPageable =
                PageRequest.of(2, pageSize, Sort.by("createdAt").ascending());


        Assertions.assertThat(sortedPageable).isNotNull();
        Assertions.assertThat(sortedPageable.getPageSize()).isEqualTo(pageSize);
        Assertions.assertThat(sortedPageable.getPageNumber()).isEqualTo(page + 1);
        Assertions.assertThat(sortedPageable.getSort()).isEqualTo(expectedSortedPageable.getSort());
    }

    @Test
    public void PaginationService_GetSortedPageableDescending_ReturnPageable() {
        int page = 1;
        int pageSize = 3;
        String direction = "next";
        String order = "desc";

        Pageable sortedPageable =
                paginationService.getSortedPageable(page, pageSize, direction, order);
        Pageable expectedSortedPageable =
                PageRequest.of(2, pageSize, Sort.by("createdAt").descending());


        Assertions.assertThat(sortedPageable).isNotNull();
        Assertions.assertThat(sortedPageable.getPageSize()).isEqualTo(pageSize);
        Assertions.assertThat(sortedPageable.getPageNumber()).isEqualTo(page + 1);
        Assertions.assertThat(sortedPageable.getSort()).isEqualTo(expectedSortedPageable.getSort());
    }

}


