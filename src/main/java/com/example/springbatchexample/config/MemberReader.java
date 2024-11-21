package com.example.springbatchexample.config;

import com.example.springbatchexample.entity.Member;
import com.example.springbatchexample.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import java.util.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

//@Component
//@RequiredArgsConstructor
//public class MemberReader {
//
//    private final MemberRepository memberRepository;
//    // Reader를 반환하는 메서드
//    @Getter
//    private RepositoryItemReader<Member> reader;
//
//    @PostConstruct
//    public void init() {
//        // 정렬 매개변수를 설정 (예: id로 오름차순 정렬)
//        Map<String, Direction> sortMap = new HashMap<>();
//        sortMap.put("idx", Sort.Direction.ASC); // 'id' 필드를 기준으로 오름차순 정렬
//
//        reader = new RepositoryItemReader<>();
//        reader.setRepository(memberRepository);  // 사용될 리포지토리 설정
//        reader.setMethodName("findAll");         // 호출할 리포지토리 메서드
//        reader.setSort(sortMap);                 // 정렬 방법 설정
//        reader.setPageSize(10);                  // 페이징 크기 설정
//    }
//
//}

@Component
@RequiredArgsConstructor
public class MemberReader extends JpaPagingItemReader<Member> {

    private final EntityManagerFactory entityManagerFactory;

    @PostConstruct
    public void init() {
        this.setEntityManagerFactory(entityManagerFactory);
        this.setQueryString("SELECT m FROM Member m");
        this.setPageSize(10);
    }

    //paging : 데이터를 읽는 단위
}


//@Component
//@RequiredArgsConstructor
//public class MemberReader {
//
//    private final EntityManagerFactory entityManagerFactory;
//
//    @Bean
//    public JpaPagingItemReader<Member> mreader() {
//        return new JpaPagingItemReaderBuilder<Member>()
//                .name("memberReader")
//                .entityManagerFactory(entityManagerFactory)
//                .queryString("SELECT m FROM Member m")
//                .pageSize(10)
//                .saveState(false)
//                .build();
//    }
//}
