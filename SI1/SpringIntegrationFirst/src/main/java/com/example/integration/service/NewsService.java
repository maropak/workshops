package com.example.integration.service;

import com.example.integration.model.News;
import com.example.integration.model.Confirmation;

public interface NewsService {

    public Confirmation process(News news);
}
