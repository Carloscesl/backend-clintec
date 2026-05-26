package com.terreneitors.backendclintec.shared.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoggingAspectTest {

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private MethodSignature methodSignature;

    @InjectMocks
    private LoggingAspect loggingAspect;

    @BeforeEach
    void setUp() {
        // Configuramos los mocks para que devuelvan una firma de método simulada
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getDeclaringType()).thenReturn(SampleService.class);
        when(methodSignature.getName()).thenReturn("executeAction");
    }

    @Test
    @DisplayName("Debe registrar inicio, fin y retornar el resultado cuando el método es exitoso")
    void logAround_ShouldLogAndReturnResult_WhenExecutionIsSuccessful() throws Throwable {
        // Arrange
        String expectedResult = "Success";
        when(joinPoint.proceed()).thenReturn(expectedResult);

        // Act
        Object result = loggingAspect.logAround(joinPoint);

        // Assert
        assertEquals(expectedResult, result);
        verify(joinPoint, times(1)).proceed();
    }

    @Test
    @DisplayName("Debe registrar el fallo y relanzar la excepción cuando el método falla")
    void logAround_ShouldLogAndThrowException_WhenExecutionFails() throws Throwable {
        // Arrange
        RuntimeException exception = new RuntimeException("Error simulado");
        when(joinPoint.proceed()).thenThrow(exception);

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            loggingAspect.logAround(joinPoint);
        });

        assertEquals("Error simulado", thrown.getMessage());
        verify(joinPoint, times(1)).proceed();
    }

    // Clase interna dummy usada únicamente para que el mock obtenga un tipo de clase
    private static class SampleService {}
}