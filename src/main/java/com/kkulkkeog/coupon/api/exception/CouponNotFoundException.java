package com.kkulkkeog.coupon.api.exception;

public class CouponNotFoundException extends RuntimeException{
    public CouponNotFoundException(long couponNo) {
        super(String.format("%d 쿠폰 을 찾을 수 없습니다.",couponNo));
    }
}
