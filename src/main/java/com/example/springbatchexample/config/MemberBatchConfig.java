package com.example.springbatchexample.config;

import com.example.springbatchexample.entity.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class MemberBatchConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final MemberReader reader;
    private final MemberCouponProcessor couponProcessor;
    private final MemberLankProcessor lankProcessor;
    private final MemberWriter writer;

    //transactionManager : 트랜잭션을 시작, 커밋, 롤백하는 등의 작업을 관리
    //PlatformTransactionManager : 트랜젝션매니저 인터페이스. 각 환경에 맞는 트랜잭션 매니저 구현체를 사용함
    //JpaTransactionManager: PlatformTransactionManager의 구현체. JPA를 사용하는 애플리케이션에서 트랜잭션을 관리.
    //그외에 DataSourceTransactionManager(JDBC를 사용), HibernateTransactionManager(Hibernate를 직접 사용) 등의 구현체가 있음

    //job repository :
    //배치 작업에 관련된 모든 정보를 저장하고 관리하는 메커니즘.
    //Job 실행정보(JobExecution), Step 실행정보(StepExecution), Job 파라미터(JobParameters)등을 저장하고 관리.
    //Job이 실행될 때, JobRepository는 새로운 JobExecution과 StepExecution을 생성하고, 이를 통해 실행 상태를 추적.
    //JobRepository는 애플리케이션 실행 내내 상태와 실행 이력을 유지해야 하므로 의존성 주입을 통해 관리해야함

    //JobExecution :
    //Spring Batch에서 Job이 실행될 때마다 생성되는 객체로, 해당 Job 실행에 대한 상태, 시작 시간, 종료 시간, 결과 등을 포함.

    //JobBuilder :
    // Job을 생성하고, jobRepository를 통해 Job을 관리함
    //객체를 생성할 때 내부에서만 잠시 사용되고, 필요할 때마다 새로 생성하는 빌더 패턴이므로 의존성 주입 x

    //JobBuilder 의 주요 메서드
    //start(Step step): Job이 시작할 첫 번째 Step을 지정합니다.
    //next(Step step): 이전 Step이 성공적으로 완료되면 다음 Step을 실행하도록 지정합니다.
    //on(String exitStatus): 특정 Step의 종료 상태에 따라 다음 실행 흐름을 결정합니다.
    //end(): Job의 실행이 완료됨을 정의합니다.
    //build(): Job 구성이 완료되면 최종적으로 Job 인스턴스를 빌드하고 반환

    @Bean
    public Job  updateMemberCouponJob() {
        return new JobBuilder("updateMemberCouponJob", jobRepository)
                .start(updateMemberCouponStep())  // 첫 번째 Step 실행
                .build();
    }

    //chunk방식 이란 :
    //대량의 데이터를 일정한 크기(Chunk)로 나누어 읽고, 처리하고, 쓰는 작업을 설정
    // 이 방식은 ItemReader, ItemProcessor, ItemWriter 세 가지 주요 구성 요소를 사용함
    //ve Tasklet 방식

    //chunk방식으로 step만들기
    //1. reader, processor, writer을 작성
        // 방법 1. 인터페이스 이용
        // 방법 2. 람다식 이용
    //2. stepBuilder로 step을 생성 - chunk크기 설정+ reader,processor,writer 설정


    //Spring Batch는 청크 기반 처리(Chunk-Oriented Processing) 구조를 통해 데이터를 여러 단위(청크)로 묶어서 처리
    //Spring Batch는 내부적으로 ChunkOrientedTasklet을 사용하여 Step을 실행
    //ChunkProvider와 ChunkProcessor: ChunkProvider는 ItemReader를 호출하여 데이터를 청크 단위로 수집하고, ChunkProcessor는 이 데이터를 ItemProcessor에 전달하여 처리
    //구체적인 과정
    //1. ChunkOrientedTasklet이 실행되면, ChunkProvider가 호출되어 데이터를 읽음
    //2. 이 과정에서 ItemReader.read()가 호출되어 데이터가 하나씩 반환
    //3. ChunkProvider.provide()가 ItemReader.read()를 호출하여 데이터를 읽어와서 Chunk<I>에 저장
    //4. ChunkProcessor는 ItemProcessor.process()를 호출하여 데이터를 처리
    //5. chunk.getItems()를 통해 ItemReader에서 데이터를 읽고,
    //6. itemProcessor.process(item) 메서드가 호출되면서 ItemReader에서 반환된 데이터(item)가 process()의 파라미터로 전달


    //paging , chunk 차이?
    //paging은 데이터를 읽는 단위(reader에서 수행)
    //chunk는 데이터를 처리하는 단위(processor에서 수행)
    //만약 page 10, chunk 5일 경우,
    //1. 10개의 데이터를 읽어온다.
    //2. 5개의 데이터를 처리(processor)한 후에 저장(writer)
    //3. 아직 10개의 데이터중 처리 안된 데이터가 5개 남았으므로, 2번과 같은 과정 수행
    //4. 읽어온 10개의 데이터가 모두 처리됐으므로 다시 10개 읽어옴 (읽을 데이터가 없을 때까지 반복)


    //reader는 시작하자마자 실행? 디버깅에 안걸림
    @Bean
    public Step updateMemberCouponStep() {
        return new StepBuilder("updateMemberCouponStep", jobRepository)
                .<Member, Member>chunk(10, transactionManager)   //청크에 트랜젝션 메니저를 넣어야하는 이유 + <> 이거 안에꺼 이해하
                //chunk 단위로 트렌잭션 관리
                .reader(reader)
                .processor(couponProcessor)
                .writer(writer)
                .build();
    }

    //Step도 하나의 인터페이스!
    //Step 구현체 종류
    //1. TaskletStep (대표적인 구현체) : Chunk-Oriented 기반 처리를 지원 => ChunkOrientedTasklet
    //2. PartitionStep
    //3. FlowStep
    //4. JobStep
    @Bean
    public Step updateMemberLankStep() {
        return new StepBuilder("updateMemberLankStep", jobRepository)
                .<Member, Member>chunk(10, transactionManager)
                .reader(reader)
                .processor(lankProcessor)
                .writer(writer)
                .transactionManager(transactionManager)
                .build();
    }
}
