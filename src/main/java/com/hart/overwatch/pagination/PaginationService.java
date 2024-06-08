package com.hart.overwatch.pagination;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PaginationService {

    public int paginate(int page, String direction) {
        int currentPage = page;

        if (direction.equals("next")) {
            currentPage = currentPage + 1;
        }

        if (direction.equals("prev") && page > 0) {
            currentPage = currentPage - 1;
        }

        return currentPage;
    }

    public Pageable getPageable(int page, int pageSize, String direction) {
        int currentPage = paginate(page, direction);

        return PageRequest.of(currentPage, pageSize);
    }
}
