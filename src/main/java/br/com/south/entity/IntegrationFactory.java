package br.com.south.entity;

import br.com.south.exception.IntegrationException;

public class IntegrationFactory {
	
	private IntegrationFactory() {
		
	}
	
	public static Integration decodeIntegration(String[] data) throws IntegrationException {
		switch (data[0]) {
		case "001":
			return new Seller(data);
		case "002":
			return new Client(data);
		case "003":
			return new Sale(data);
		default:
			 throw new  IntegrationException("Call with invalid data.");
		}
	}
}
