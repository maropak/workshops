package com.example.integration.messaging.client;

import com.example.integration.model.News;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.subethamail.wiser.Wiser;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(locations = {"classpath*:*-config.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class NewsProcessorTests {

	@Autowired
	NewsProcessor newsProcessor;
	@Autowired
	ConfirmationLogger confirmationLogger;
	Wiser mailServer;

	@Before
	public void setup() {
		mailServer = new Wiser();
		mailServer.start();
	}

	@Test
	public void test() throws Exception {
		News news1 = new News("aaaa", "A1");
		News news2 = new News("bbb", "A2");
		News news3 = new News("cccccc", "A1");
		News news4 = new News("dddddd", "A3");
		News news5 = new News("eeeeee", "A2");

		newsProcessor.process(news1);
		newsProcessor.process(news2);
		newsProcessor.process(news3);
		newsProcessor.process(news4);
		newsProcessor.process(news5);

		Thread.sleep(1000);
		assertEquals(5, confirmationLogger.getConfirmations().size());
		
		//assertEquals(5, mailServer.getMessages().size());
		//assertEquals("marek@example.com", mailServer.getMessages().get(0).getEnvelopeReceiver());
	}

}
