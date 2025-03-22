package com.manna.batchservice.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class MatchingController {
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("matchingJob")
    private Job matchingJob;

    @GetMapping("/job")
    public String runJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        jobLauncher.run(
                matchingJob,
                new JobParametersBuilder()
                        .addString("timestamp", LocalDateTime.now().toString())
                        .toJobParameters()
        );
        return "OK";
    }
}