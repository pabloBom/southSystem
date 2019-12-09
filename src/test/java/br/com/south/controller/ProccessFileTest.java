package br.com.south.controller;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.south.CommonData;
import br.com.south.entity.Client;
import br.com.south.entity.Integration;
import br.com.south.entity.Sale;
import br.com.south.entity.Seller;

@SpringBootTest
@ActiveProfiles("test")
public class ProccessFileTest extends CommonData{


	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
    @Test
    public void shouldVerifyIfIsValidFile() {
    	ProccessFile proccessFile = spy(ProccessFile.class);
    	String fileName = "aaa.dat";
    	
    	doReturn(new ArrayList<IntegrationFile>()).when(proccessFile).load(fileName);
    	doReturn(1L).when(proccessFile).getCountClients(anyList());
    	doReturn(1L).when(proccessFile).getCountSellers(anyList());
     	doReturn("1").when(proccessFile).getMostExpensiveSale(anyList());
    	doReturn("a").when(proccessFile).getWorstSeller(anyList());
    	doReturn(true).when(proccessFile).generateReport(anyString(), anyLong(), anyLong(), anyString(), anyString());
    	
    	proccessFile.proccessFile(fileName);
    	
		verify(proccessFile).load(fileName);
		verify(proccessFile).generateReport(anyString(), anyLong(), anyLong(), anyString(), anyString());
    }
    
    @Test
    public void shouldVerifyIfIsInalidFile() {
    	ProccessFile proccessFile = spy(ProccessFile.class);
    	String fileName = "aaa.aaa";
    	
    	proccessFile.proccessFile(fileName);
    	
    	verify(proccessFile, never()).load(fileName);
    	verify(proccessFile, never()).generateReport(anyString(), anyLong(), anyLong(), anyString(), anyString());
    }
    
    @Test
    public void shouldVerifyIfIsNullFile() {
    	ProccessFile proccessFile = spy(ProccessFile.class);
    	
    	proccessFile.proccessFile(null);
    	
    	verify(proccessFile, never()).load(null);
    	verify(proccessFile, never()).generateReport(anyString(), anyLong(), anyLong(), anyString(), anyString());
    }
    
    @Test
    public void shouldLoadEmptyFile() throws IOException {
    	
    	ProccessFile proccessFile = spy(ProccessFile.class);
    	Path path = mock(Path.class);
    	doReturn(path).when(proccessFile).resolvePathToFile(anyString());
    	
    	List<String> lines = new ArrayList<>();
    	
        doReturn(lines.stream()).when(proccessFile).fileRed(path);
    	
    	List<Integration> returnedLines = proccessFile.load("");
    	
    	verify(proccessFile, never()).decode(anyString());
    	assertThat(returnedLines.isEmpty(), is(true));
    }
    
    @Test
    public void shouldErrorLoadFile() throws IOException {
    	
    	ProccessFile proccessFile = spy(ProccessFile.class);
    	Path path = mock(Path.class);
    	doReturn(path).when(proccessFile).resolvePathToFile(anyString());
    	
    	doThrow(IOException.class).when(proccessFile).fileRed(path);
    	
    	List<Integration> returnedLines = proccessFile.load("");
    	
    	verify(proccessFile, never()).decode(anyString());
    	assertThat(returnedLines.isEmpty(), is(true));
    }
    
    @Test
    public void shouldLoadInvalidContentFile() throws IOException {
    	
    	ProccessFile proccessFile = spy(ProccessFile.class);
    	Path path = mock(Path.class);
    	doReturn(path).when(proccessFile).resolvePathToFile(anyString());
    	
    	List<String> lines = new ArrayList<>();
    	lines.add("a");
    	
        doReturn(lines.stream()).when(proccessFile).fileRed(path);
    	
    	List<Integration> returnedLines = proccessFile.load("");
    	
    	verify(proccessFile, never()).decode(anyString());
    	assertThat(returnedLines.isEmpty(), is(true));
    }
    
    @Test
    public void shouldLoadCallDecode() throws IOException {
    	
    	ProccessFile proccessFile = spy(ProccessFile.class);
    	Path path = mock(Path.class);
    	doReturn(path).when(proccessFile).resolvePathToFile(anyString());
    	
    	List<String> lines = new ArrayList<>();
    	lines.add("001");
    	lines.add("001");
    	lines.add("002");
    	lines.add("003");
    	lines.add("003");
    	lines.add("a");
    	
        doReturn(lines.stream()).when(proccessFile).fileRed(path);
        doReturn(null).when(proccessFile).decode(anyString());
    	
    	List<Integration> returnedLines = proccessFile.load("");
    	
    	// 5 it's a number of valid register on list
    	verify(proccessFile, times(5)).decode(anyString());
    	assertThat(returnedLines.size(), is(5));
    }

    @Test
    public void shouldCreateInvalidSeller() {
    	ProccessFile proccessFile = spy(ProccessFile.class);
    	String strSeller = SELLER_INVALID;
    	Integration expectASeller = proccessFile.decode(strSeller);
    	assertThat(expectASeller, nullValue());
    }

    
    @Test
    public void shouldCreateSeller() {
    	ProccessFile proccessFile = spy(ProccessFile.class);
    	String strSeller = SELLER_VALID;
    	Integration expectASeller = proccessFile.decode(strSeller);
    	assertThat(expectASeller, instanceOf(Seller.class));
    }
    
    @Test
    public void shouldCreateSellerExtraTest() {
    	ProccessFile proccessFile = spy(ProccessFile.class);
    	String strSeller = EXTRA_SELLER_VALID;
    	Integration expectASeller = proccessFile.decode(strSeller);
    	assertThat(expectASeller, instanceOf(Seller.class));
    }

    @Test
    public void shouldCreateInvalidClient() {
    	ProccessFile proccessFile = spy(ProccessFile.class);
    	String strClient = CLIENT_INVALID;
    	Integration expectAClient = proccessFile.decode(strClient);
    	assertThat(expectAClient, nullValue());
    }

    
    @Test
    public void shouldCreateClient() {
    	ProccessFile proccessFile = spy(ProccessFile.class);
    	String strClient = CLIENT_VALID;
    	Integration expectAClient = proccessFile.decode(strClient);
    	assertThat(expectAClient, instanceOf(Client.class));
    }
    
    @Test
    public void shouldCreateClientExtraTest() {
    	ProccessFile proccessFile = spy(ProccessFile.class);
    	String strClient = EXTRA_CLIENT_VALID;
    	Integration expectAClient = proccessFile.decode(strClient);
    	assertThat(expectAClient, instanceOf(Client.class));
    }

    @Test
    public void shouldCreateInvalidSale() {
    	ProccessFile proccessFile = spy(ProccessFile.class);
    	String strSale = SALE_INVALID;
    	Integration expectASale = proccessFile.decode(strSale);
    	assertThat(expectASale, nullValue());
    }

    
    @Test
    public void shouldCreateSale() {
    	ProccessFile proccessFile = spy(ProccessFile.class);
    	String strSale = SALE_VALID;
    	Integration expectASale = proccessFile.decode(strSale);
    	assertThat(expectASale, instanceOf(Sale.class));
    }
    
    @Test
    public void shouldCreateSaleExtraTest() {
    	ProccessFile proccessFile = spy(ProccessFile.class);
    	String strSale = EXTRA_SALE_VALID;
    	Integration expectASale = proccessFile.decode(strSale);
    	assertThat(expectASale, instanceOf(Sale.class));
    }

    @Test
    public void shouldNumberOfClients() {
    	ProccessFile proccessFile = spy(ProccessFile.class);
    	
    	List<Integration> validList= new ArrayList<>();
    	validList.addAll(VALID_CLIENT_LIST);
    	validList.addAll(VALID_SALE_LIST);
    	validList.addAll(VALID_SELLER_LIST);
    	
		long numberOfClient= proccessFile.getCountClients(validList);
    	assertThat(numberOfClient, is(2L));
    }

    @Test
    public void shouldNotHasClient() {
    	ProccessFile proccessFile = spy(ProccessFile.class);
    	
    	List<Integration> validList= new ArrayList<>();
    	validList.addAll(VALID_SALE_LIST);
    	validList.addAll(VALID_SELLER_LIST);
    	
		long numberOfClient= proccessFile.getCountClients(validList);
    	assertThat(numberOfClient, is(0L));
    }
    
    @Test
    public void shouldNumberOfSellers() {
    	ProccessFile proccessFile = spy(ProccessFile.class);
    	
    	List<Integration> validList= new ArrayList<>();
    	validList.addAll(VALID_CLIENT_LIST);
    	validList.addAll(VALID_SALE_LIST);
    	validList.addAll(VALID_SELLER_LIST);
    	
    	long numberOfSeller= proccessFile.getCountSellers(validList);
    	assertThat(numberOfSeller, is(2L));
    }
    
    @Test
    public void shouldNotHasSeller() {
    	ProccessFile proccessFile = spy(ProccessFile.class);
    	
    	List<Integration> validList= new ArrayList<>();
    	validList.addAll(VALID_SALE_LIST);
    	validList.addAll(VALID_CLIENT_LIST);
    	
    	long numberOfSeller= proccessFile.getCountSellers(validList);
    	assertThat(numberOfSeller, is(0L));
    }

    @Test
    public void shouldMostExpensiveSale() {
    	ProccessFile proccessFile = spy(ProccessFile.class);
    	
    	List<Integration> validList= new ArrayList<>();
    	validList.addAll(VALID_CLIENT_LIST);
    	validList.addAll(VALID_SALE_LIST);
    	validList.addAll(VALID_SELLER_LIST);
    	
    	String mostExpensiveSale = proccessFile.getMostExpensiveSale(validList);
    	assertThat(mostExpensiveSale, is("10"));
    }
    
    @Test
    public void shouldNotHasSale() {
    	ProccessFile proccessFile = spy(ProccessFile.class);
    	
    	List<Integration> validList= new ArrayList<>();
    	validList.addAll(VALID_CLIENT_LIST);
    	validList.addAll(VALID_SELLER_LIST);
    	
    	String mostExpensiveSale = proccessFile.getMostExpensiveSale(validList);
    	assertThat(mostExpensiveSale, is("No has Sale"));
    }
    
    @Test
    public void shouldWorstSeller() {
    	ProccessFile proccessFile = spy(ProccessFile.class);
    	
    	List<Integration> validList= new ArrayList<>();
    	validList.addAll(VALID_CLIENT_LIST);
    	validList.addAll(VALID_SALE_LIST);
    	validList.addAll(VALID_SELLER_LIST);
    	
    	String mostExpensiveSale = proccessFile.getWorstSeller(validList);
    	assertThat(mostExpensiveSale, is("Paulo"));
    }
    
    @Test
    public void shouldWorstSellerWidhoutSales() {
    	ProccessFile proccessFile = spy(ProccessFile.class);
    	
    	List<Integration> validList= new ArrayList<>();
    	validList.addAll(VALID_CLIENT_LIST);
    	validList.addAll(VALID_SELLER_LIST);
    	
    	String mostExpensiveSale = proccessFile.getWorstSeller(validList);
    	assertThat(mostExpensiveSale, is("No has Sale or Seller to compare"));
    }
    
    @Test
    public void shouldWorstSellerWidhoutSellers() {
    	ProccessFile proccessFile = spy(ProccessFile.class);
    	
    	List<Integration> validList= new ArrayList<>();
    	validList.addAll(VALID_CLIENT_LIST);
    	
    	String mostExpensiveSale = proccessFile.getWorstSeller(validList);
    	assertThat(mostExpensiveSale, is("No has Sale or Seller to compare"));
    }
    
    @Test
    public void shouldSellerWidhoutSale() {
    	ProccessFile proccessFile = spy(ProccessFile.class);
    	
    	List<Integration> validList= new ArrayList<>();
    	validList.addAll(VALID_CLIENT_LIST);
    	validList.addAll(VALID_SALE_LIST);
    	validList.addAll(VALID_SELLER_LIST);
    	validList.add(SELLER_WIDHOUT_SALE);
    	
    	String mostExpensiveSale = proccessFile.getWorstSeller(validList);
    	assertThat(mostExpensiveSale, is("SellerWhidoutSale"));
    }
    
    @Test
    public void shouldGenerateReport() throws IOException {
    	ProccessFile proccessFile = spy(ProccessFile.class);
    	Path path = mock(Path.class);
    	BufferedWriter bw = mock(BufferedWriter.class);
    	doReturn(path).when(proccessFile).resolveDestinationReport(anyString());
    	doReturn(bw).when(proccessFile).fileWrite(path);
    	
    	assertThat(proccessFile.generateReport("a", 1L, 1L, "1", "a"), is(true));
    	
    }
    
    @Test
    public void shouldErrorGenerateReport() throws IOException {
    	ProccessFile proccessFile = spy(ProccessFile.class);
    	Path path = mock(Path.class);
    	doReturn(path).when(proccessFile).resolveDestinationReport(anyString());
    	doThrow(IOException.class).when(proccessFile).fileWrite(path);
    	
    	assertThat(proccessFile.generateReport("a", 1L, 1L, "1", "a"), is(false));
    	
    }

}
