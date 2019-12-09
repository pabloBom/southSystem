package br.com.south.entity;

import java.math.BigDecimal;

import br.com.south.exception.IntegrationException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;

@Data
@EqualsAndHashCode(callSuper=false)
@Log4j2
public class Seller extends Integration {
	private static final String SELLET_TYPE = "001";

	
	private String cpf;
	private String name;
	private BigDecimal salary;
	
	protected boolean validation(String[] args) {
		return args.length == 4 && getType().equals(args[0]);
	}
	public Seller(String[] args) throws IntegrationException {
		setType(SELLET_TYPE);
		if(!validation(args)) {
        	log.warn("Call Seller with invalid data.");
        	throw new IntegrationException("Call Seller with invalid data.");
		}
		
		setCpf(args[1]);
		setName(args[2]);
		setSalary(new BigDecimal(args[3]));
	}
}
