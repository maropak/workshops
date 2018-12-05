package com.example.integration.messaging.client;

import com.example.integration.model.Confirmation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple logger for news confirmations.
 */
public class ConfirmationLogger {

	private static final Log logger = LogFactory.getLog(ConfirmationLogger.class);

	private List<Confirmation> confirmations = new ArrayList<Confirmation>();

	public void log(Confirmation confirmation) {
		this.confirmations.add(confirmation);
		if (logger.isInfoEnabled()) {
			logger.info("received confirmation !!!!!!!!!!!!!!!!!! : " + confirmation);
		}
	}

	public List<Confirmation> getConfirmations() {
		return Collections.unmodifiableList(confirmations);
	}
}
