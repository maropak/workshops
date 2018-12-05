package com.example.integration.service;

import com.example.integration.model.Confirmation;
import com.example.integration.model.News;

public interface NewsProcessor {

	// here we save News in db using NewsRepository
	Confirmation processNews(News news);

	Confirmation processNews2(News news);
}