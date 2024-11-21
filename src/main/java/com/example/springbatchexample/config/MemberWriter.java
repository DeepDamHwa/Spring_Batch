package com.example.springbatchexample.config;

import com.example.springbatchexample.entity.Member;
import com.example.springbatchexample.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberWriter implements ItemWriter<Member> {
    private final MemberRepository memberRepository;

    @Override
    public void write(Chunk<? extends Member> members) throws Exception { //<? extends Member>이거를 꼭 써야하는 이유?
        memberRepository.saveAll(members);
    }
}
