package com.empresa.proveedores.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

// El contenedor de servlets (Tomcat, WildFly...) detecta esta clase al desplegar
// el WAR y, a través de ella, crea y registra el DispatcherServlet.
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	// Contexto raíz: beans compartidos (datos, transacciones, servicios).
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { AppConfig.class };
	}

	// Contexto del DispatcherServlet: controladores y configuración MVC.
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] { WebConfig.class };
	}

	// El DispatcherServlet atiende todas las URL de la aplicación.
	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}
}
