package com.example.integration.service;

import com.example.integration.model.News;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.integration.MessageRejectedException;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ContextConfiguration("classpath:test-context-with-bridge.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ErrorChannelBridgeTests {

	@Autowired
	NewsProcessor newsProcessor;
	@Autowired
	MessagingTemplate template;

	@Test
	public void invalidNews_exceptionMessageFromFilter() {

		News invalidNews = new News(null, null);

		template.convertAndSend("newsChannel", invalidNews);
		
		assertThat(template.receiveAndConvert("errorTestChannel", MessageRejectedException.class), is(MessageRejectedException.class));
	}

	@Test
	public void validNews_exceptionMessageFromNewsProcessor() {
		News validNews = new News("aaaa", "A1");

		// mock throw exception in processor
		when(newsProcessor.processNews(validNews)).thenThrow(new EmptyResultDataAccessException(1));
 		template.convertAndSend("newsChannel", validNews);

		MessageHandlingException exception = template.receiveAndConvert("errorTestChannel", MessageHandlingException.class);

		// checked that exception is EmptyResultDataAccessException
		assertTrue(exception.getRootCause() instanceof EmptyResultDataAccessException);

		verify(newsProcessor, times(1)).processNews(validNews);
	}
}
