package com.manna.batchservice.batch.reader;

import com.manna.batchservice.tingthing.entity.RapidMatching;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManagerFactory;

@Component
public class RapidMatchingReader {

    public JpaPagingItemReader<RapidMatching> rapidMatchingReader(EntityManagerFactory entityManagerFactory) {
        JpaPagingItemReader<RapidMatching> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("SELECT rm FROM RapidMatching rm WHERE rm.status = 'PENDING'");
        reader.setPageSize(10);
        reader.setName("rapidMatchingReader");
        return reader;
    }
}