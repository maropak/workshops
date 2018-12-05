package com.example.integration.service;

import com.example.integration.model.News;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.MessageRejectedException;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@ContextConfiguration({"classpath:test-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class ErrorChannelTests {

	@Autowired
	MessagingTemplate template;
	@Autowired
	PublishSubscribeChannel errorChannel;
	
	@Test
	public void sendInvalidNews() throws Exception {
		ErrorHandler handler = new ErrorHandler();
		errorChannel.subscribe(handler);

		News invalidNews = new News(null, null);
		template.convertAndSend("newsChannel", invalidNews);

		Thread.sleep(1000);
		assertNotNull(handler.msg);
		assertTrue(handler.msg.getPayload() instanceof MessageRejectedException);
	}

	static class ErrorHandler implements MessageHandler {
		volatile Message<?> msg;

		public void handleMessage(Message<?> message) throws MessagingException {
			msg = message;
		}
	}

}
