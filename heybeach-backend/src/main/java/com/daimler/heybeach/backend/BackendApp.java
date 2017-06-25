package com.daimler.heybeach.backend;

import com.daimler.heybeach.backend.storage.FilesystemStorage;
import com.daimler.heybeach.backend.storage.PictureStorage;
import com.daimler.heybeach.data.core.PersistenceConfig;
import com.daimler.heybeach.data.core.QueryExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@SpringBootApplication(scanBasePackages = "com.daimler.heybeach")
public class BackendApp {

    @Value("${entity.package}")
    private String entityPackage;

    @Value("${picture.storage.fs.path}")
    private String pictureStorageFsPath;

    @Bean
    @ConfigurationProperties("heybeach.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public PictureStorage pictureStorage() {
        return new FilesystemStorage(pictureStorageFsPath);
    }

    @Bean
    public QueryExecutor queryExecutor(DataSource dataSource) {
        PersistenceConfig persistenceConfig = new PersistenceConfig(entityPackage);
        QueryExecutor executor = new QueryExecutor(dataSource, persistenceConfig.createPersistenceContext());
        return executor;
    }

    public static void main(String[] args) {
        SpringApplication.run(BackendApp.class, args);
    }
}
