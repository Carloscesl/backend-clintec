package com.terreneitors.backendclintec.shared.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
    @Pointcut(
            "within(com.terreneitors.backendclintec..application.service..*)" +
                    " || within(com.terreneitors.backendclintec..infrastructure.web..*)"
    )
    public void capaAplicacion() {}

    @Around("capaAplicacion()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String clase  = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String metodo = joinPoint.getSignature().getName();

        log.info("→ [{}.{}]", clase, metodo);
        long inicio = System.currentTimeMillis();

        try {
            Object resultado = joinPoint.proceed();
            long duracion = System.currentTimeMillis() - inicio;
            log.info("← [{}.{}] {}ms", clase, metodo, duracion);
            return resultado;

        } catch (Exception ex) {
            long duracion = System.currentTimeMillis() - inicio;
            log.error("✗ [{}.{}] falló en {}ms | {}",
                    clase, metodo, duracion, ex.getMessage());
            throw ex; // relanza para que llegue al GlobalExceptionHandler
        }
    }
}
