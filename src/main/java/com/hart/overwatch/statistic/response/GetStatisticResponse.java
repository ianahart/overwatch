package com.hart.overwatch.statistic.response;

import com.hart.overwatch.statistic.dto.OverallStatDto;

public class GetStatisticResponse {

    private String message;

    private OverallStatDto data;


    public GetStatisticResponse() {

    }

    public GetStatisticResponse(String message, OverallStatDto data) {
        this.message = message;
        this.data  = data;
    }

    public String getMessage() {
        return message;
    }

    public OverallStatDto getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(OverallStatDto data) {
        this.data = data;
    }
}
