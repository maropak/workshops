package com.example.integration.service;

import com.example.integration.model.Confirmation;
import com.example.integration.model.News;
import com.example.integration.repository.ConfirmationRepository;
import org.springframework.util.Assert;

public class ConfirmationService {

	private final ConfirmationRepository confirmationRepository;
	private final ConfirmationProcessor confirmationProcessor;

	public ConfirmationService(ConfirmationRepository confirmationRepository,
                               ConfirmationProcessor confirmationProcessor) {
		Assert.notNull(confirmationRepository);
		Assert.notNull(confirmationProcessor);

		this.confirmationProcessor = confirmationProcessor;
		this.confirmationRepository = confirmationRepository;
	}


	public News sendConfirmationForExistingNews(News news) {
		Confirmation existingConfirmation = confirmationRepository.findByNews(news);
		if (existingConfirmation != null) {
			confirmationProcessor.process(existingConfirmation);
			return null; // prevent further processing of the news
		}
		return news;
	}
}
