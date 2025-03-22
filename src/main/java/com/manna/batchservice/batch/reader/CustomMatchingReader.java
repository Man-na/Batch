package com.manna.batchservice.batch.reader;

import com.manna.batchservice.tingthing.entity.CustomMatching;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManagerFactory;

@Component
public class CustomMatchingReader {

    public JpaPagingItemReader<CustomMatching> customMatchingReader(EntityManagerFactory entityManagerFactory) {
        JpaPagingItemReader<CustomMatching> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("SELECT cm FROM CustomMatching cm WHERE cm.status = 'PENDING'");
        reader.setPageSize(10);
        reader.setName("customMatchingReader");
        System.out.println("reader = " + reader);
        return reader;
    }
}