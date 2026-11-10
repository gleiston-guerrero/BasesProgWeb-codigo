// Archivo: src/main/java/com/comercializadora/componentes/ciclo/TrazadorPostProcessor.java
package com.comercializadora.componentes.ciclo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

// Posprocesador: el contenedor le entrega CADA bean antes y después de inicializarlo
@Component
public class TrazadorPostProcessor implements BeanPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(TrazadorPostProcessor.class);

    @Override
    public Object postProcessBeforeInitialization(Object bean, String nombre) {
        if (bean instanceof BeanObservado) {
            log.info("4. BeanPostProcessor: antes de la inicialización");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String nombre) {
        if (bean instanceof BeanObservado) {
            log.info("7. BeanPostProcessor: después de la inicialización");
        }
        return bean;   // aquí podría devolverse otro objeto, por ejemplo un proxy
    }
}
