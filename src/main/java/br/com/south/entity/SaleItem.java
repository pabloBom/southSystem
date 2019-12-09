package br.com.south.entity;

import java.math.BigDecimal;

import br.com.south.exception.IntegrationException;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Data
@Log4j2
public class SaleItem {
	private String itemId;
	private int quantity;
	private BigDecimal priceItem;
	private BigDecimal totalItem;
	
	protected boolean validation(String[] itemData) {
		return itemData.length == 3;
	}
	public SaleItem(String item) throws IntegrationException {
		String[] itemData = decode(item);
		
		if(!validation(itemData)) {
        	log.warn("Call Item with invalid data.");
        	throw new IntegrationException("Call Item with invalid data.");
		}

		setItemId(itemData[0]);
		setQuantity(Integer.parseInt(itemData[1]));
		setPriceItem(new BigDecimal(itemData[2]));
		
		setTotalItem(getPriceItem().multiply(new BigDecimal(quantity)));
	}
	private String[] decode(String item) {
		return item.replaceAll("(\\[|\\])", "").split("-");
	}
}
