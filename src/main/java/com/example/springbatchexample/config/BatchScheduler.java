package com.example.springbatchexample.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BatchScheduler {

    private final JobLauncher jobLauncher;
    private final Job memberJob;


    // 매시간 배치 작업을 실행하는 스케줄러 (cron 표현식으로 설정 가능)
    @Scheduled(cron = "30 * * * * ?")  // 2분마다 실행
    public void runBatchJob() {
        System.out.println("@@@@@시작@@@@@");
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())  // 매번 새로운 파라미터로 실행
                    .toJobParameters();
            jobLauncher.run(memberJob, params);  // 배치 작업 실행
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("@@@@@끝@@@@@");
    }

    //batch는 배치대로 만들어서 jar파일 만들어서
    //리눅스 서버로 옮겨서 crontab으로
}


