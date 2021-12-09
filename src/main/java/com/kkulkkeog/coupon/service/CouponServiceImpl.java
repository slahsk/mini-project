package com.kkulkkeog.coupon.service;


import com.kkulkkeog.coupon.api.exception.CouponNotFoundException;
import com.kkulkkeog.coupon.api.exception.CouponValidationException;
import com.kkulkkeog.coupon.api.message.CouponCalculatePrice;
import com.kkulkkeog.coupon.api.message.CouponValidation;
import com.kkulkkeog.coupon.domain.Coupon;
import com.kkulkkeog.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CouponServiceImpl implements CouponService {
    private final CouponRepository couponRepository;

    @Override
    public Flux<Coupon> findAllCoupon(Example<Coupon> example){
        return Flux.fromIterable(couponRepository.findAll(example));
    }

    @Override
    public Mono<Coupon> saveCoupon(Coupon coupon) {
        return Mono.just(couponRepository.save(coupon));
    }

    @Override
    public Mono<Void> deleteCoupon(long couponNo) {
        return findCoupon(couponNo)
        .doOnNext(coupon -> coupon.setDeleted(true)).then();
    }

    @Override
    public Mono<Coupon> findCoupon(long couponNo) {
        Optional<Coupon> coupon = couponRepository.findById(couponNo);
        return Mono.just(coupon.orElseThrow(() -> new CouponNotFoundException(couponNo)));
    }

    @Override
    public Mono<Boolean> validationOrderCoupon(List<CouponValidation> couponValidations) {
        return Flux.fromIterable(couponValidations)
                .flatMap( couponValidation -> {
                    List<Long> couponNos = couponValidations.stream().map(CouponValidation::getCouponNo).collect(Collectors.toList());
                    Map<Long, Coupon> couponMap = couponRepository.findAllById(couponNos).stream().collect(Collectors.toMap(Coupon::getCouponNo, Function.identity()));

                    Coupon coupon = couponMap.get(couponValidation.getCouponNo());

                    boolean orderAvailableCoupon = coupon.isOrderAvailableCoupon(couponValidation.getShopNo(), couponValidation.getMemberNo());
                   return Mono.just(orderAvailableCoupon);
                })
                .filter( b -> b)
                .as(booleanFlux -> booleanFlux.count().map(i ->{

                    if( i != couponValidations.size()){
                        throw new CouponValidationException(couponValidations.toString());
                    }

                    return true;
                }));
    }

    @Override
    public Mono<Long> calculatePrice(CouponCalculatePrice couponCalculatePrice) {
        return Mono.just(couponCalculatePrice).map( o -> {
            long sum = couponRepository.findAllById(couponCalculatePrice.getCouponNos()).stream()
                    .mapToLong(Coupon::getPrice).sum();
            return  couponCalculatePrice.getOrderTotalPrice() - sum;
        });
    }

}
