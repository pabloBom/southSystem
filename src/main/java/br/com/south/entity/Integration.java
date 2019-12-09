package br.com.south.entity;

import lombok.Data;

@Data
public abstract class Integration {
	private String type;
	
	protected abstract boolean validation(String[] args);
	
}
