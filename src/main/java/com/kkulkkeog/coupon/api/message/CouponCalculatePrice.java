package com.kkulkkeog.coupon.api.message;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CouponCalculatePrice {
    private List<Long> couponNos;

    private long orderTotalPrice;
}
