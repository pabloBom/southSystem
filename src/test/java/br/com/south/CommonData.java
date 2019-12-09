package br.com.south;

import java.util.ArrayList;
import java.util.List;

import br.com.south.entity.Client;
import br.com.south.entity.Integration;
import br.com.south.entity.Sale;
import br.com.south.entity.Seller;
import br.com.south.exception.IntegrationException;

public abstract class CommonData {
	
	protected static final String[] ARGS_CLIENT2 = new String[] {"002", "2345675433444345", "Eduardo Pereira", "Rural"};
	protected static final String[] ARGS_CLIENT1 = new String[] {"002", "2345675434544345", "Jose da Silva", "Rural"};
	protected static final String[] ARGS_SELLER2 = new String[] {"001", "3245678865434", "Paulo", "40000.99"};
	protected static final String[] ARGS_SELLER1 = new String[] {"001", "1234567891234", "Pedro", "50000"};
	protected static final String[] ARGS_SALE2 = new String[] {"003", "08", "[1-34-10,2-33-1.50,3-40-0.10]", "Paulo"};
	protected static final String[] ARGS_SALE1 = new String[] {"003", "10", "[1-10-100,2-30-2.50,3-40-3.10]", "Pedro"};
	protected static final String CLIENT_INVALID = "002ç2345675434544345çJose da Silva";
	protected static final String CLIENT_VALID = "002ç2345675434544345çJose da SilvaçRural";
	protected static final String EXTRA_CLIENT_VALID = "002ç2345675434544345çConceição da SilvaçSegurança";
	
	protected static final String SELLER_INVALID = "001ç1234567891234çPedro";
	protected static final String SELLER_VALID = "001ç1234567891234çPedroç50000";
	protected static final String EXTRA_SELLER_VALID = "001ç1234567891234çConceiçãoç50000";

	protected static final String SALE_INVALID = "003ç10ç[1-10-100,2-30-2.50,3-40-3.10]";
	protected static final String SALE_VALID = "003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro";
	protected static final String EXTRA_SALE_VALID = "003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çConceição";
	
	protected static Seller SELLER_WIDHOUT_SALE;
	protected static final List<Integration> VALID_CLIENT_LIST;
	protected static final List<Integration> VALID_SELLER_LIST;
	protected static final List<Integration> VALID_SALE_LIST;
	
	static {
		
		List<Integration> listClient = new ArrayList<>();
		List<Integration> listSeller = new ArrayList<>();
		List<Integration> listSale = new ArrayList<>();
		try {
			Client client1 = new Client(ARGS_CLIENT1);
			Client client2 = new Client(ARGS_CLIENT2);
			listClient.add(client1);
			listClient.add(client2);
			
			Seller seller1 = new Seller(ARGS_SELLER1);
			Seller seller2 = new Seller(ARGS_SELLER2);
			listSeller.add(seller1);
			listSeller.add(seller2);
		
			
			Sale sale1 = new Sale(ARGS_SALE1);
			Sale sale2 = new Sale(ARGS_SALE2);
			listSale.add(sale1);
			listSale.add(sale2);

			SELLER_WIDHOUT_SALE = new Seller(new String[] {"001", "1234567891234", "SellerWhidoutSale", "50000"});
			
		} catch (IntegrationException e) {
		}
		VALID_CLIENT_LIST = listClient;
		VALID_SELLER_LIST = listSeller;
		VALID_SALE_LIST = listSale;		
	}
}
