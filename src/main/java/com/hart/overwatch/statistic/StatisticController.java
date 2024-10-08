package com.hart.overwatch.statistic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.statistic.response.GetStatisticResponse;

@RestController
@RequestMapping(path = "/api/v1/statistics")
public class StatisticController {

    private final StatisticService statisticService;

    @Autowired
    public StatisticController(StatisticService statisticService) {
         this.statisticService = statisticService;
    }

    @GetMapping("")
    public ResponseEntity<GetStatisticResponse> getStatistics(
            @RequestParam("reviewerId") Long reviewerId) {
        return ResponseEntity.status(HttpStatus.OK).body(new GetStatisticResponse("success",        statisticService.getStatistics(reviewerId)
));
    }
}
