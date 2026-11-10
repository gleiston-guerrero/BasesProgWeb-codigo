// Archivo: src/main/java/com/comercializadora/sgpc/audit/AuditoriaAspect.java
package com.comercializadora.sgpc.audit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;     // @Aspect, @Around
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.Arrays;

// @Aspect declara la clase como un aspecto; @Component la registra como bean.
@Aspect
@Component
public class AuditoriaAspect {

    private static final Logger log = LoggerFactory.getLogger(AuditoriaAspect.class);

    // @Around envuelve los métodos con @Auditable (tipo con su nombre completo);
    // el parámetro ProceedingJoinPoint representa la llamada interceptada.
    @Around("@annotation(com.comercializadora.sgpc.audit.Auditable)")
    public Object auditar(ProceedingJoinPoint pjp) throws Throwable {
        long inicio = System.currentTimeMillis();            // ANTES
        String resultado = "error";
        try {
            Object r = pjp.proceed();                         // EJECUTA el método real
            resultado = "ok";
            return r;
        } finally {
            // DESPUÉS: se ejecuta aunque el método lance una excepción
            log.info("[AUDITORIA] usuario={} operacion={} argumentos={} "
                    + "resultado={} duracion={} ms",
                    usuarioActual(), pjp.getSignature().toShortString(),
                    Arrays.toString(pjp.getArgs()), resultado,
                    System.currentTimeMillis() - inicio);
        }
    }

    // En la Parte II aún no hay autenticación; el Capítulo 12 sustituye
    // este método por la consulta del usuario autenticado.
    private String usuarioActual() {
        return "anónimo";
    }
}
