// Archivo: src/main/java/com/comercializadora/componentes/ciclo/BeanObservado.java
package com.comercializadora.componentes.ciclo;

import com.comercializadora.componentes.beans.ValidadorRuc;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// Bean que anuncia cada fase de su ciclo de vida
@Component
public class BeanObservado implements BeanNameAware, InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(BeanObservado.class);

    public BeanObservado() {
        log.info("1. Instanciación: constructor");
    }

    @Autowired   // inyección por setter, para que la fase sea visible
    public void setValidador(ValidadorRuc validador) {
        log.info("2. Inyección de dependencias: setValidador");
    }

    @Override    // BeanNameAware: el contenedor le comunica su nombre
    public void setBeanName(String nombre) {
        log.info("3. Interfaz Aware: setBeanName(\"{}\")", nombre);
    }

    @PostConstruct
    public void iniciar() {
        log.info("5. Inicialización: método @PostConstruct");
    }

    @Override    // InitializingBean
    public void afterPropertiesSet() {
        log.info("6. Inicialización: afterPropertiesSet()");
    }

    public void usar() {
        log.info("8. En uso: el bean atiende llamadas");
    }

    @PreDestroy
    public void liberar() {
        log.info("9. Destrucción: método @PreDestroy");
    }
}
