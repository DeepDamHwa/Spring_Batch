package com.example.springbatchexample.config;

import com.example.springbatchexample.entity.Member;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class MemberLankProcessor implements ItemProcessor<Member, Member> {
    @Override
    public Member process(Member member) throws Exception {
        int totalConsume = member.getTotalConsume();
        int lank=0;

        if(totalConsume < 30){
            lank = 0;
        }else if(30 <= totalConsume && totalConsume <60){
            lank = 1;
        }else if(60 <= totalConsume && totalConsume <90){
            lank = 2;
        }else{
            lank = 3;
        }

        return Member.builder()
                .idx(member.getIdx())
                .lank(lank)
                .totalConsume(member.getTotalConsume())
                .coupon(member.getCoupon())
                .build();
    }
}
