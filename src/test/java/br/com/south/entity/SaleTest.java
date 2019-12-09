package br.com.south.entity;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import br.com.south.CommonData;
import br.com.south.exception.IntegrationException;

@ActiveProfiles("test")
class SaleTest extends CommonData{
    @Test
    public void shouldCreateClient() throws IntegrationException {
    	Integration integration = new Sale(ARGS_SALE1);
    	assertThat(integration, instanceOf(Sale.class));
    }
    
    @Test
    public void shouldErrorLessArgs() throws IntegrationException {
    	assertThrows(IntegrationException.class,  () -> {
    		new Sale(new String[] {"aaa","bbb"}); 
		});
    }
    
    @Test
    public void shouldError() throws IntegrationException {
    	assertThrows(IntegrationException.class,  () -> {
    		new Sale(new String[] {"aaa","bbb", "ccc", "ddd"}); 
    	});
    }
}
