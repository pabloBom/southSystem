package br.com.south.entity;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import br.com.south.CommonData;
import br.com.south.exception.IntegrationException;

@ActiveProfiles("test")
class IntegrationFactoryTest extends CommonData{

    @Test
    public void shouldCreateSeller() throws IntegrationException {
    	Integration integration = IntegrationFactory.decodeIntegration(ARGS_SELLER2);
    	assertThat(integration, instanceOf(Seller.class));
    }
    
    @Test
    public void shouldCreateClient() throws IntegrationException {
    	Integration integration = IntegrationFactory.decodeIntegration(ARGS_CLIENT1);
    	assertThat(integration, instanceOf(Client.class));
    }
    
    @Test
    public void shouldCreateSale() throws IntegrationException {
    	Integration integration = IntegrationFactory.decodeIntegration(ARGS_SALE1);
    	assertThat(integration, instanceOf(Sale.class));
    }
    
    @Test
    public void shouldError() throws IntegrationException {
    	assertThrows(IntegrationException.class,  () -> {
    		IntegrationFactory.decodeIntegration(new String[] {"aaa","bbb"}); 
		});
    }
}
