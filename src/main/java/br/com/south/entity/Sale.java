package br.com.south.entity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import br.com.south.exception.IntegrationException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;

@Data
@EqualsAndHashCode(callSuper = false)
@Log4j2
public class Sale extends Integration {
	private static final String ORDER_TYPE = "003";

	private String saleId;
	private String salesman;
	private List<SaleItem> items;
	private BigDecimal totalSale = new BigDecimal(0);

	protected boolean validation(String[] args) {
		return args.length == 4 && getType().equals(args[0]);
	}

	public Sale(String[] args) throws IntegrationException {
		setType(ORDER_TYPE);
		if (!validation(args)) {
			log.warn("Call Order with invalid data.");
			throw new IntegrationException("Call Order with invalid data.");
		}
		setSaleId(args[1]);
		setItems(decodeList(args[2]));
		setSalesman(args[3]);
	}

	private List<SaleItem> decodeList(String itemsList) {
		return Arrays.asList(itemsList.split(",")).stream().map(this::resolveItem).collect(Collectors.toList());
	}
	private SaleItem resolveItem(String itemData) {
		try {
			SaleItem saleItem = new SaleItem(itemData);
			setTotalSale(getTotalSale().add(saleItem.getTotalItem())); 
			return saleItem;
		} catch (IntegrationException e) {
			log.warn("Call Item with invalid data.");
		}
		return null;
	}
}
