package com.kkulkkeog.order.v1.api.web;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetOrderRequest {
    private Long userNo;
}
