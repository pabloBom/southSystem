package br.com.south.controller;
import java.util.List;

import br.com.south.entity.Integration;


public interface IntegrationFile {

	public abstract List<Integration> load(String fileName);
	
	public abstract void proccessFile(String fileName);
	
	public long getCountClients(List<Integration> dataList);
	public long getCountSellers(List<Integration> dataList);
	public String getMostExpensiveSale(List<Integration> dataList);
	public String getWorstSeller(List<Integration> dataList);

}