// Archivo: src/main/java/com/comercializadora/sgpc/audit/Auditable.java
package com.comercializadora.sgpc.audit;

import java.lang.annotation.*;

// @Target: solo puede colocarse sobre métodos.
// @Retention(RUNTIME): la anotación se conserva en tiempo de ejecución,
// condición necesaria para que Spring AOP pueda detectarla.
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auditable {
}
