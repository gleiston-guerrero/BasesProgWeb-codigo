// Archivo: src/main/java/com/comercializadora/sgpc/audit/CronometroServicioAspect.java
// Ejercicio E2.3
package com.comercializadora.sgpc.audit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CronometroServicioAspect {

    private static final Logger log =
            LoggerFactory.getLogger(CronometroServicioAspect.class);

    // Todo método público de cualquier clase de service y sus subpaquetes
    @Around("execution(public * com.comercializadora.sgpc.service..*.*(..))")
    public Object medir(ProceedingJoinPoint pjp) throws Throwable {
        long inicio = System.nanoTime();
        try {
            return pjp.proceed();
        } finally {           // también si el método lanza una excepción
            long ms = (System.nanoTime() - inicio) / 1_000_000;
            log.info("[TIEMPO] {} {} ms", pjp.getSignature().toShortString(), ms);
        }
    }
}
