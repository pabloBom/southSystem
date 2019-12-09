package br.com.south.service;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import br.com.south.controller.ProccessFile;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Configuration
@Log4j2
@Data
@Profile("!test")
public class DirectoryWatcherService {

	@Value("${sourceFolder}")
	private String sourceFolder;

	@Autowired
	private ProccessFile proccessFile;

	@Bean
	@Profile("!test")
	public boolean watch() {
		log.info("begin watch folder {}", getSourceFolder());

		try {
			Path path = getSourcePath();
			WatchService watchService = FileSystems.getDefault().newWatchService();

			path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

			callWatchEvent(watchService);
			return true;
		} catch (IOException e) {
			log.error("Fail to register service", e);
		} catch (InterruptedException e) {
			log.error("Fail to take event", e);
			Thread.currentThread().interrupt();
		}
		return false;
	}

	protected Path getSourcePath() {
		return Paths.get(getSourceFolder());
	}

	public boolean callWatchEvent(WatchService watchService) throws InterruptedException {
		WatchKey key;
		while ((key = watchService.take()) != null) {
			for (WatchEvent<?> event : key.pollEvents()) {
				String fileName = event.context().toString();
				log.info("Event kind: {}. File affected: {}.", event.kind(), fileName);
				getProccessFile().proccessFile(fileName);
			}
			log.info("end event");
			key.reset();
		}
		return true;
	}
}