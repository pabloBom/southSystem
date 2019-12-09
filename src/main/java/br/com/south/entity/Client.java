package br.com.south.entity;

import br.com.south.exception.IntegrationException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;

@Data
@EqualsAndHashCode(callSuper=false)
@Log4j2
public class Client extends Integration {
	private static final String CLIENT_TYPE = "002";
	
	private String cnpj;
	private String name;
	private String businessArea; 
	
	protected boolean validation(String[] args) {
		return args.length == 4 && getType().equals(args[0]); 
	}
	
	public Client(String[] args) throws IntegrationException {
		setType(CLIENT_TYPE);
		if(!validation(args)) {
        	log.warn("Call Client with invalid data.");
        	throw new IntegrationException("Call Client with invalid data.");
		}
		setCnpj(args[1]);
		setName(args[2]);
		setBusinessArea(args[3]);
	}

}
