package com.example.integration.messaging.client;

import com.example.integration.model.News;

public interface NewsProcessor {

    void process(News news);

}
