package com.manna.batchservice.batch.writer;

import com.manna.batchservice.tingthing.entity.MatchingResult;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManagerFactory;

@Component
public class MatchingResultWriter {

    public JpaItemWriter<MatchingResult> matchingResultWriter(EntityManagerFactory entityManagerFactory) {
        JpaItemWriter<MatchingResult> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }
}