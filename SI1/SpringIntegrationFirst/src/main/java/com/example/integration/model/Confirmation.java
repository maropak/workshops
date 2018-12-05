package com.example.integration.model;

import java.io.Serializable;

public class Confirmation implements Serializable {

	private String confirmationNumber;

	public Confirmation(String confirmationNumber) {
		this.confirmationNumber = confirmationNumber;
	}

	public String getConfirmationNumber() {
		return confirmationNumber;
	}

	@Override
	public String toString() {
		return confirmationNumber;
	}

	@Override
	public int hashCode() {
		return  confirmationNumber.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Confirmation)) {
			return false;
		}
		Confirmation other = (Confirmation) o;
		return confirmationNumber.equals(other.confirmationNumber);
	}
}