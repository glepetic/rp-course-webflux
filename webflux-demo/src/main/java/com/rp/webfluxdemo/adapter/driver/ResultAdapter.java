package com.rp.webfluxdemo.adapter.driver;

import com.rp.webfluxdemo.infrastructure.dto.response.ResultResponse;

import java.time.LocalDateTime;

public class ResultAdapter {

    public ResultResponse to(Long result) {
        return new ResultResponse(LocalDateTime.now(), result);
    }

}
