// Archivo: src/main/java/com/comercializadora/sgpc/audit/ErroresServicioAspect.java
// Ejercicio E2.7: registra las excepciones que lanza la capa de servicio
package com.comercializadora.sgpc.audit;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ErroresServicioAspect {

    private static final Logger log =
            LoggerFactory.getLogger(ErroresServicioAspect.class);

    // throwing = "ex" enlaza la excepción con el parámetro ex; su tipo
    // (RuntimeException) restringe además a qué ejecuciones se aplica
    @AfterThrowing(
            pointcut = "execution(public * com.comercializadora.sgpc.service..*.*(..))",
            throwing = "ex")
    public void registrar(JoinPoint jp, RuntimeException ex) {
        log.warn("[ERROR] {} -> {}: {}", jp.getSignature().toShortString(),
                ex.getClass().getSimpleName(), ex.getMessage());
        // no captura la excepción: al terminar, sigue propagándose
    }
}
