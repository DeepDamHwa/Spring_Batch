package com.example.springbatchexample.config;

import com.example.springbatchexample.entity.Member;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class MemberCouponProcessor implements ItemProcessor<Member, Member> {
    @Override
    public Member process(Member member) throws Exception {
        int lank = member.getLank();
        int coupon = member.getCoupon();

        // lank에 따라 쿠폰 발급 수 증가
        if (lank == 1) {
            coupon+=1;
        } else if (lank == 2) {
            coupon+=2;
        } else if (lank == 3) {
            coupon+=3;
        }

        return Member.builder()
                .idx(member.getIdx())
                .lank(member.getLank())
                .totalConsume(member.getTotalConsume())
                .coupon(coupon)
                .build();
    }
}
