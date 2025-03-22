package com.manna.batchservice.batch.job;

import com.manna.batchservice.batch.reader.CustomMatchingReader;
import com.manna.batchservice.batch.reader.RapidMatchingReader;
import com.manna.batchservice.batch.writer.MatchingResultWriter;
import com.manna.batchservice.processor.CustomMatchingProcessor;
import com.manna.batchservice.processor.RapidMatchingProcessor;
import com.manna.batchservice.tingthing.entity.CustomMatching;
import com.manna.batchservice.tingthing.entity.MatchingResult;
import com.manna.batchservice.tingthing.entity.RapidMatching;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
public class CreateMatchingBatchJob {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private CustomMatchingReader customMatchingReader;

    @Autowired
    private RapidMatchingReader rapidMatchingReader;

    @Autowired
    private CustomMatchingProcessor customMatchingProcessor;

    @Autowired
    private RapidMatchingProcessor rapidMatchingProcessor;

    @Autowired
    private MatchingResultWriter matchingResultWriter;


    @Bean
    public Job matchingJob() {
        return new JobBuilder("matchingJob", jobRepository)
                .start(customMatchingStep())
                .next(rapidMatchingStep())
                .build();
    }

    public Step customMatchingStep() {
        return new StepBuilder("customMatchingStep", jobRepository)
                .<CustomMatching, MatchingResult>chunk(10, transactionManager)
                .reader(customMatchingReader.customMatchingReader(entityManagerFactory))
                .processor(customMatchingProcessor)
                .writer(matchingResultWriter.matchingResultWriter(entityManagerFactory))
                .build();
    }

    public Step rapidMatchingStep() {
        return new StepBuilder("rapidMatchingStep", jobRepository)
                .<RapidMatching, MatchingResult>chunk(10, transactionManager)
                .reader(rapidMatchingReader.rapidMatchingReader(entityManagerFactory))
                .processor(rapidMatchingProcessor)
                .writer(matchingResultWriter.matchingResultWriter(entityManagerFactory))
                .build();
    }
}
