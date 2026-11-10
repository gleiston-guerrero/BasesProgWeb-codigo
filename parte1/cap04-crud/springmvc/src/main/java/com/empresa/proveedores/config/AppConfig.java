package com.empresa.proveedores.config;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.*;
import org.springframework.orm.jpa.*;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
// Registra ProveedorService (y cualquier otro @Service) en el contexto raíz.
@ComponentScan("com.empresa.proveedores.service")
// Activa Spring Data JPA: genera la implementación de ProveedorRepository.
@EnableJpaRepositories("com.empresa.proveedores.repository")
// Activa @Transactional (Spring Boot lo hacía de forma automática).
@EnableTransactionManagement
public class AppConfig {

	// 1. Fuente de datos: H2 embebida en memoria (solo para desarrollo).
	@Bean
	public DataSource dataSource() {
		return new EmbeddedDatabaseBuilder()
		.setType(EmbeddedDatabaseType.H2)
		.build();
	}

	// 2. Fábrica de EntityManager: Hibernate como proveedor JPA,
	//    entidades buscadas en el paquete model.
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
		HibernateJpaVendorAdapter adaptador = new HibernateJpaVendorAdapter();
		adaptador.setGenerateDdl(true);   // crea las tablas a partir de las entidades
		LocalContainerEntityManagerFactoryBean fabrica = new LocalContainerEntityManagerFactoryBean();
		fabrica.setDataSource(dataSource);
		fabrica.setJpaVendorAdapter(adaptador);
		fabrica.setPackagesToScan("com.empresa.proveedores.model");
		return fabrica;
	}

	// 3. Gestor de transacciones JPA: lo usa @Transactional en ProveedorService.
	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		return new JpaTransactionManager(emf);
	}
}
