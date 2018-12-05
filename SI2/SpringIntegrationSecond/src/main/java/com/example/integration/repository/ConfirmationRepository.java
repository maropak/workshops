package com.example.integration.repository;

import com.example.integration.model.Confirmation;
import com.example.integration.model.News;

public interface ConfirmationRepository {

    Confirmation findByNews(News news);
}
