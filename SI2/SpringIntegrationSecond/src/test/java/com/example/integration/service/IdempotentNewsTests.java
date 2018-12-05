package com.example.integration.service;

import com.example.integration.model.Confirmation;
import com.example.integration.model.News;
import com.example.integration.repository.ConfirmationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@ContextConfiguration("classpath:test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class IdempotentNewsTests {

	@Autowired
	ConfirmationRepository confirmationRepository;
	@Autowired
	NewsProcessor newsProcessor;
	@Autowired MessagingTemplate template;

	@Test
	public void checkIdempotence() {
		News news = new News("aaaa", "A1");

		Confirmation confirmation = new Confirmation("10");
		when(newsProcessor.processNews(news)).thenReturn(confirmation);

		// we are relying on default null-returning behavior of ConfirmationRepository mock here
		template.convertAndSend("newsChannel", news);
		
		Confirmation firstConfirmation = template.receiveAndConvert("confirmations", Confirmation.class);
		
		// this time the repository will find an existing confirmation
		when(confirmationRepository.findByNews(news)).thenReturn(firstConfirmation);
		template.convertAndSend("newsChannel", news);
		
		Confirmation secondConfirmation = template.receiveAndConvert("confirmations", Confirmation.class);
		
		assertEquals(secondConfirmation, firstConfirmation);
		verify(newsProcessor, times(1)).processNews(news);
	}
}
