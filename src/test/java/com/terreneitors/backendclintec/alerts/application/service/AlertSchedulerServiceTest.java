package com.terreneitors.backendclintec.alerts.application.service;

import com.terreneitors.backendclintec.alerts.application.port.out.AlertRepositoryPort;
import com.terreneitors.backendclintec.alerts.domain.Alert;
import com.terreneitors.backendclintec.alerts.domain.TypeAlert;
import com.terreneitors.backendclintec.clients.application.port.out.ClientRepositoryPort;
import com.terreneitors.backendclintec.clients.domain.Client; // Ajusta según la ruta real de tu entidad/dominio Client
import com.terreneitors.backendclintec.opportunities.application.port.out.OpportunityRepositoryPort;
import com.terreneitors.backendclintec.opportunities.domain.Opportunity;
import com.terreneitors.backendclintec.opportunities.domain.StatusOpportunity;
import com.terreneitors.backendclintec.shared.email.EmailPort;
import com.terreneitors.backendclintec.users.application.port.out.UserRepositoryPort;
import com.terreneitors.backendclintec.users.domain.Rol;
import com.terreneitors.backendclintec.users.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertSchedulerServiceTest {

    @Mock
    private AlertRepositoryPort alertRepositoryPort;
    @Mock
    private UserRepositoryPort userRepositoryPort;
    @Mock
    private OpportunityRepositoryPort opportunityRepositoryPort;
    @Mock
    private ClientRepositoryPort clientRepositoryPort;
    @Mock
    private EmailPort emailPort;

    @InjectMocks
    private AlertSchedulerService alertSchedulerService;

    private User mockAsesor;
    private User mockGerente;
    private User mockAdmin;
    private Client mockCliente;
    private Opportunity mockOportunidad;
    private Alert mockAlertaGuardada;

    @BeforeEach
    void setUp() {
        // Mock de Usuarios
        mockAsesor = new User();
        mockAsesor.setId(2L);
        mockAsesor.setNombreUser("Asesor Carlos");
        mockAsesor.setEmail("asesor@clintec.com");
        mockAsesor.setRol(Rol.ASESOR);
        mockAsesor.setActivo(true);

        mockGerente = new User();
        mockGerente.setId(3L);
        mockGerente.setNombreUser("Gerente Fabiana");
        mockGerente.setEmail("gerente@clintec.com");
        mockGerente.setRol(Rol.GERENTE);
        mockGerente.setActivo(true);

        mockAdmin = new User();
        mockAdmin.setId(1L);
        mockAdmin.setNombreUser("Admin Root");
        mockAdmin.setEmail("admin@clintec.com");
        mockAdmin.setRol(Rol.ADMINISTRADOR);
        mockAdmin.setActivo(true);

        // Mock de Cliente
        mockCliente = new Client();
        mockCliente.setId(10L);
        mockCliente.setNombreCliente("Juan Pérez");
        mockCliente.setEmpresa("Terreneitors S.A.S.");
        mockCliente.setEmail("juan@cliente.com");
        mockCliente.setTelefono("3001234567");

        // Mock de Oportunidad
        mockOportunidad = new Opportunity();
        mockOportunidad.setIdOportunidad(100L);
        mockOportunidad.setAsesorId(2L);
        mockOportunidad.setEstado(StatusOpportunity.ACTIVA);
        mockOportunidad.setFechaCreacion(LocalDateTime.now().minusDays(20));
        mockOportunidad.setFechaEstimadaCierre(LocalDate.now().minusDays(2)); // Vencida
        mockOportunidad.setValorEstimado(BigDecimal.valueOf(5000.0));
        mockOportunidad.setDescripcion("Venta de Software");

        // Mock de Alerta de Retorno genérica
        mockAlertaGuardada = new Alert();
        mockAlertaGuardada.setId(55L);
        mockAlertaGuardada.setClienteId(10L);
    }

    @Test
    @DisplayName("1. Inactividad: Debe crear alerta y enviar correo SOLO al asesor")
    void verificarInactividad_ShouldAlertAndEmailOnlyAsesor() {
        // Arrange
        when(alertRepositoryPort.findClientesSinInteraccionesDesdeFecha(any(LocalDateTime.class)))
                .thenReturn(List.of(10L));
        when(alertRepositoryPort.existeAlertPendiente(10L, TypeAlert.INACTIVIDAD)).thenReturn(false);
        when(opportunityRepositoryPort.findByIdClient(10L)).thenReturn(List.of(mockOportunidad));
        when(alertRepositoryPort.save(any(Alert.class))).thenReturn(mockAlertaGuardada);
        when(clientRepositoryPort.findById(10L)).thenReturn(Optional.of(mockCliente));
        when(userRepositoryPort.findById(2L)).thenReturn(Optional.of(mockAsesor));

        // Act
        alertSchedulerService.ejecutarTodasLasAlertas();

        // Assert
        verify(alertRepositoryPort, times(1)).save(argThat(alerta -> alerta.getTipo() == TypeAlert.INACTIVIDAD));
        // Debe enviar correo al asesor
        verify(emailPort, times(1)).enviarAlertaConPlantilla(eq("asesor@clintec.com"), eq("Asesor Carlos"), eq(mockAlertaGuardada), anyMap());
        // NO debe enviar a gerentes (porque no se llama notificarGerentes en la inactividad)
        verify(emailPort, never()).enviarAlertaConPlantilla(eq("gerente@clintec.com"), anyString(), any(), anyMap());
    }

    @Test
    @DisplayName("2. Vencimiento: Debe alertar y notificar tanto al Asesor como a los Gerentes")
    void verificarOportunidadesVencidas_ShouldAlertAsesorAndGerentes() {
        // Arrange
        when(alertRepositoryPort.findOportunidadesVencidasActivas(any(LocalDate.class)))
                .thenReturn(List.of(10L));
        when(alertRepositoryPort.existeAlertPendiente(10L, TypeAlert.VENCIMIENTO)).thenReturn(false);
        when(opportunityRepositoryPort.findByIdClient(10L)).thenReturn(List.of(mockOportunidad));
        when(alertRepositoryPort.save(any(Alert.class))).thenReturn(mockAlertaGuardada);
        when(clientRepositoryPort.findById(10L)).thenReturn(Optional.of(mockCliente));

        // Mocks de usuarios para el flujo de envío y notificación general
        when(userRepositoryPort.findById(2L)).thenReturn(Optional.of(mockAsesor));
        when(userRepositoryPort.findAll()).thenReturn(List.of(mockAsesor, mockGerente, mockAdmin));

        // Act
        alertSchedulerService.ejecutarTodasLasAlertas();

        // Assert
        verify(alertRepositoryPort, times(1)).save(argThat(alerta -> alerta.getTipo() == TypeAlert.VENCIMIENTO));
        // Envío al Asesor
        verify(emailPort, times(1)).enviarAlertaConPlantilla(eq("asesor@clintec.com"), eq("Asesor Carlos"), eq(mockAlertaGuardada), anyMap());
        // Envío al Gerente (por medio de notificarGerentes())
        verify(emailPort, times(1)).enviarAlertaConPlantilla(eq("gerente@clintec.com"), eq("Gerente Fabiana"), eq(mockAlertaGuardada), anyMap());
        // NO debe enviar al administrador en este flujo
        verify(emailPort, never()).enviarAlertaConPlantilla(eq("admin@clintec.com"), anyString(), any(), anyMap());
    }

    @Test
    @DisplayName("3. Seguimiento: Debe alertar oportunidades estancadas a Asesor y Gerentes")
    void verificarOportunidadesEnNegociacion_ShouldAlertAsesorAndGerentes() {
        // Arrange
        when(alertRepositoryPort.findOportunidadesEnNegociacionSinCambios(any(LocalDateTime.class)))
                .thenReturn(List.of(10L));
        when(alertRepositoryPort.existeAlertPendiente(10L, TypeAlert.SEGUIMIENTO)).thenReturn(false);
        when(opportunityRepositoryPort.findByIdClient(10L)).thenReturn(List.of(mockOportunidad));
        when(alertRepositoryPort.save(any(Alert.class))).thenReturn(mockAlertaGuardada);
        when(clientRepositoryPort.findById(10L)).thenReturn(Optional.of(mockCliente));

        when(userRepositoryPort.findById(2L)).thenReturn(Optional.of(mockAsesor));
        when(userRepositoryPort.findAll()).thenReturn(List.of(mockAsesor, mockGerente));

        // Act
        alertSchedulerService.ejecutarTodasLasAlertas();

        // Assert
        verify(alertRepositoryPort, times(1)).save(argThat(alerta -> alerta.getTipo() == TypeAlert.SEGUIMIENTO));
        verify(emailPort, times(1)).enviarAlertaConPlantilla(eq("asesor@clintec.com"), eq("Asesor Carlos"), eq(mockAlertaGuardada), anyMap());
        verify(emailPort, times(1)).enviarAlertaConPlantilla(eq("gerente@clintec.com"), eq("Gerente Fabiana"), eq(mockAlertaGuardada), anyMap());
    }

    @Test
    @DisplayName("4. Oportunidad: Clientes sin oportunidades deben alertar SOLO a Administradores")
    void verificarClientesSinOportunidades_ShouldAlertOnlyAdministrators() {
        // Arrange
        when(alertRepositoryPort.findClientesSinOportunidadesDesdeFecha(any(LocalDateTime.class)))
                .thenReturn(List.of(10L));
        when(alertRepositoryPort.existeAlertPendiente(10L, TypeAlert.OPORTUNIDAD)).thenReturn(false);

        // Simular obtención de administrador base
        when(userRepositoryPort.findAll()).thenReturn(List.of(mockAsesor, mockGerente, mockAdmin));
        when(alertRepositoryPort.save(any(Alert.class))).thenReturn(mockAlertaGuardada);
        when(clientRepositoryPort.findById(10L)).thenReturn(Optional.of(mockCliente));

        // Act
        alertSchedulerService.ejecutarTodasLasAlertas();

        // Assert
        verify(alertRepositoryPort, times(1)).save(argThat(alerta -> alerta.getTipo() == TypeAlert.OPORTUNIDAD));
        // Verifica que se le envió al administrador activo
        verify(emailPort, times(1)).enviarAlertaConPlantilla(eq("admin@clintec.com"), eq("Admin Root"), eq(mockAlertaGuardada), anyMap());
        // No se le debe enviar ni al asesor ni al gerente en este flujo
        verify(emailPort, never()).enviarAlertaConPlantilla(eq("asesor@clintec.com"), anyString(), any(), anyMap());
        verify(emailPort, never()).enviarAlertaConPlantilla(eq("gerente@clintec.com"), anyString(), any(), anyMap());
    }

    @Test
    @DisplayName("5. Optimización: Si la alerta ya está PENDIENTE, no debe duplicarse ni reenviar correos")
    void cuandoExisteAlertaPendiente_ShouldSkipProcessing() {
        // Arrange
        when(alertRepositoryPort.findClientesSinInteraccionesDesdeFecha(any(LocalDateTime.class))).thenReturn(List.of(10L));
        // Simulamos que la alerta ya existe en la BD
        when(alertRepositoryPort.existeAlertPendiente(10L, TypeAlert.INACTIVIDAD)).thenReturn(true);

        // Act
        alertSchedulerService.ejecutarTodasLasAlertas();

        // Assert
        // El bucle hace un 'continue', por ende jamás llama al repositorio .save() ni al emailPort
        verify(alertRepositoryPort, never()).save(any(Alert.class));
        verify(emailPort, never()).enviarAlertaConPlantilla(anyString(), anyString(), any(), anyMap());
    }
}