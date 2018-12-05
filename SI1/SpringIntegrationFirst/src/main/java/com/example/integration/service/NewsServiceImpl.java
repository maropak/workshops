package com.example.integration.service;

import com.example.integration.model.News;
import com.example.integration.model.Confirmation;

import java.util.concurrent.atomic.AtomicInteger;

public class NewsServiceImpl implements NewsService {

    private static AtomicInteger counter = new AtomicInteger();

    public Confirmation process(News news) {
        // for example save message in db
        return new Confirmation("" + counter.incrementAndGet());
    }
}
