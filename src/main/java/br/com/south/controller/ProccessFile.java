package br.com.south.controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.south.entity.Client;
import br.com.south.entity.Integration;
import br.com.south.entity.IntegrationFactory;
import br.com.south.entity.Sale;
import br.com.south.entity.Seller;
import br.com.south.exception.IntegrationException;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Data
@Component
@Log4j2
public class ProccessFile implements IntegrationFile {
	
	@Value("${sourceFolder}")
	private String sourceFolder;
	
	private String regexSpliter = "(?=ç[0-9A-Z\\[])ç";
	
	@Value("${replacedFilename}")
	private String replacedFilename;
	
	@Value("${regexFileName}")
	private String regexFileName;
	
	@Value("${destinationFolder}")
	private String destinationFolder;

	public void proccessFile(String fileName) {
		if(fileName != null && fileName.endsWith(".dat")) {
			List<Integration> dataList = load(fileName);
			generateReport(fileName, getCountClients(dataList), getCountSellers(dataList), getMostExpensiveSale(dataList), getWorstSeller(dataList));
		}
	}
	
	public List<Integration> load(String fileName) {

		List<Integration> list = new ArrayList<>();

		Path filePath = resolvePathToFile(fileName);

		try (Stream<String> stream = fileRed(filePath)) {
			list = stream
					.filter(line -> (line.startsWith("001") || line.startsWith("002") || line.startsWith("003")))
					.map(this::decode).collect(Collectors.toList());

		} catch (IOException e) {
			log.warn( "Fail to open file: {}".concat(fileName), e);
		}
		
		return list;
	}

	protected Integration decode(String line) {
		try {
			String[] split = line.split(getRegexSpliter());
			return IntegrationFactory.decodeIntegration(split);
		} catch (IntegrationException e) {
			log.error("Line Error: ".concat(line), e);
		}
		return null;
	}

	protected boolean generateReport(String fileName, long countClients, long countSellers, String expensiveSale, String wrostSeller) {
		
		Path destinationepath = resolveDestinationReport(fileName);
		
		try (BufferedWriter writer = fileWrite(destinationepath)) {

            writer.write("Quantidade de clientes no arquivo de entrada: ".concat(Long.toString(countClients)) );
            writer.newLine();
            writer.write("Quantidade de vendedor no arquivo de entrada: ".concat(Long.toString(countSellers)) );
            writer.newLine();
            writer.write("ID da venda mais cara: ".concat(expensiveSale) );
            writer.newLine();
            writer.write("O pior vendedor: ".concat(wrostSeller) );
            return true;
        } catch (IOException e) {
        	log.error("Error to generate report: : ".concat(fileName), e);
        }
		return false;
	}


	@Override
	public long getCountClients(List<Integration> dataList) {
		return dataList.stream().filter(item -> item instanceof Client).count();
	}

	@Override
	public long getCountSellers(List<Integration> dataList) {
		return dataList.stream().filter(item -> item instanceof Seller).count();
	}

	@Override
	public String getMostExpensiveSale(List<Integration> dataList) {
		Comparator<Sale> comparator = Comparator.comparing(Sale::getTotalSale);

		Sale sale = dataList.stream().filter(item -> item instanceof Sale).map(item -> (Sale) item).max(comparator)
				.orElse(null);

		return sale != null ? sale.getSaleId() : "No has Sale";
	}

	@Override
	public String getWorstSeller(List<Integration> dataList) {
		Map<String, BigDecimal> saleBySeller = dataList.stream().filter(item -> item instanceof Sale)
				.map(item -> (Sale) item)
				.collect(Collectors.groupingBy(Sale::getSalesman, Collectors.mapping(Sale::getTotalSale, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));
		
		if (saleBySeller.isEmpty()) {
			return "No has Sale or Seller to compare";
		}
		
		Integration listOfSellers = dataList.stream()
				.filter(item -> item instanceof Seller && !saleBySeller.keySet().contains(((Seller)item).getName()))
				.findFirst().orElse(null);
		
		if(listOfSellers != null) {
			Seller sellerWidhoutSale = (Seller) listOfSellers;
			return sellerWidhoutSale.getName();
		}
		
		return saleBySeller.entrySet().stream().min(Map.Entry.comparingByValue()).get().getKey();
	}
	
	/**
	 * Used for mock test
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	protected Stream<String> fileRed(Path filePath) throws IOException {
		return Files.lines(filePath);
	}

	/**
	 * used for mock test
	 * @param fileName
	 * @return
	 */
	protected Path resolvePathToFile(String fileName) {
		return Paths.get(getSourceFolder()).resolve(fileName);
	}
	
	/**
	 * used for mock test
	 * @param fileName
	 * @return
	 */
	protected Path resolveDestinationReport(String fileName) {
		String fileDestinationName = fileName.replaceAll(getRegexFileName(), getReplacedFilename());
		return Paths.get(getDestinationFolder()).resolve(fileDestinationName);
	}
	/**
	 * used for mock test
	 * @param destinationepath
	 * @return
	 * @throws IOException
	 */
	protected BufferedWriter fileWrite(Path destinationepath) throws IOException {
		return Files.newBufferedWriter(destinationepath);
	}

}