
package com.gavet.api.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.gavet.api.dto.*;
import com.gavet.api.entity.*;
import com.gavet.api.enums.EstadoCita;
import com.gavet.api.enums.RolEmpleado;
import com.gavet.api.enums.TipoServicio;
import com.gavet.api.repository.*;

@Service
public class PersonalGestionService {

    private final EmpleadoRepository empleados;
    private final CitaRepository citas;
    private final ServicioRepository servicios;
    private final ClienteRepository clientes;
    private final MascotaRepository mascotas;

    public PersonalGestionService(
            EmpleadoRepository empleados,
            CitaRepository citas,
            ServicioRepository servicios,
            ClienteRepository clientes,
            MascotaRepository mascotas
    ) {
        this.empleados = empleados;
        this.citas = citas;
        this.servicios = servicios;
        this.clientes = clientes;
        this.mascotas = mascotas;
    }

    // =========================================
    // FUNCIONES GENERALES
    // =========================================

    private ResponseStatusException error(
            HttpStatus estado,
            String mensaje
    ) {
        return new ResponseStatusException(
                estado,
                mensaje
        );
    }

    private LocalDate hoy() {
        return LocalDate.now(
                ZoneId.of("America/Lima")
        );
    }

    private long contar(
            List<Cita> lista,
            EstadoCita estado
    ) {
        return lista.stream()
                .filter(c ->
                        c.getEstado() == estado
                )
                .count();
    }

    // =========================================
    // CONVERTIR EMPLEADO PARA LA API
    // =========================================

    private EmpleadoVista convertirEmpleado(
            Empleado empleado
    ) {
        return new EmpleadoVista(
                empleado.getIdEmpleado(),
                empleado.getNombreCompleto(),
                empleado.getEmail(),
                empleado.getTelefono(),
                empleado.getRol().name(),
                Boolean.TRUE.equals(
                        empleado.getActivo()
                )
        );
    }

    // =========================================
    // GESTION DE ESTILISTAS
    // =========================================

    @Transactional(readOnly = true)
    public List<EmpleadoVista> listarEstilistas() {

        return empleados.findAll()
                .stream()
                .filter(e ->
                        e.getRol() == RolEmpleado.ESTILISTA
                )
                .sorted(
                        Comparator.comparing(
                                Empleado::getIdEmpleado
                        )
                )
                .map(this::convertirEmpleado)
                .toList();
    }

    // =========================================
    // REGISTRAR ESTILISTA
    // =========================================

    @Transactional
    public EmpleadoVista crearEstilista(
            EmpleadoGuardarRequest request
    ) {

        validarEmpleado(request, true);

        String correo = request.correo()
                .trim()
                .toLowerCase(Locale.ROOT);

        if (empleados.existsByEmailIgnoreCase(correo)) {

            throw error(
                    HttpStatus.CONFLICT,
                    "Ya existe un empleado con ese correo"
            );
        }

        validarCorreoNoCliente(correo);

        Empleado empleado = new Empleado();

        empleado.setRol(
                RolEmpleado.ESTILISTA
        );

        empleado.setNombreCompleto(
                request.nombre().trim()
        );

        empleado.setTelefono(
                request.telefono().trim()
        );

        empleado.setEmail(correo);

        empleado.setPassword(
                request.password()
        );

        empleado.setActivo(true);

        Empleado guardado = empleados.save(empleado);

        return convertirEmpleado(guardado);
    }

    // =========================================
    // EDITAR ESTILISTA
    // =========================================

    @Transactional
    public EmpleadoVista editarEstilista(
            Integer id,
            EmpleadoGuardarRequest request
    ) {

        Empleado empleado = buscarEstilista(id);

        validarEmpleado(request, false);

        String correo = request.correo()
                .trim()
                .toLowerCase(Locale.ROOT);

        empleados.findByEmailIgnoreCase(correo)
                .ifPresent(otro -> {

                    if (!otro.getIdEmpleado().equals(id)) {

                        throw error(
                                HttpStatus.CONFLICT,
                                "El correo ya está en uso"
                        );
                    }
                });

        validarCorreoNoCliente(correo);

        empleado.setNombreCompleto(
                request.nombre().trim()
        );

        empleado.setTelefono(
                request.telefono().trim()
        );

        empleado.setEmail(correo);

        if (
                request.password() != null
                        && !request.password().isBlank()
        ) {
            empleado.setPassword(
                    request.password()
            );
        }

        Empleado guardado = empleados.save(empleado);

        return convertirEmpleado(guardado);
    }

    // =========================================
    // ACTIVAR O DESACTIVAR ESTILISTA
    // =========================================

    @Transactional
    public EmpleadoVista cambiarActivoEstilista(
            Integer id,
            ActivoRequest request
    ) {

        if (
                request == null
                        || request.activo() == null
        ) {
            throw error(
                    HttpStatus.BAD_REQUEST,
                    "Indica si la cuenta estará activa"
            );
        }

        Empleado empleado = buscarEstilista(id);

        empleado.setActivo(
                request.activo()
        );

        Empleado guardado = empleados.save(empleado);

        return convertirEmpleado(guardado);
    }

    // =========================================
    // BUSCAR ESTILISTA
    // =========================================

    private Empleado buscarEstilista(
            Integer id
    ) {

        Empleado empleado = empleados.findById(id)
                .orElseThrow(() ->
                        error(
                                HttpStatus.NOT_FOUND,
                                "Estilista no encontrado"
                        )
                );

        if (empleado.getRol() != RolEmpleado.ESTILISTA) {

            throw error(
                    HttpStatus.BAD_REQUEST,
                    "La cuenta no pertenece a un estilista"
            );
        }

        return empleado;
    }

    // =========================================
    // VALIDAR CORREO
    // =========================================

    private void validarCorreoNoCliente(
            String correo
    ) {

        boolean usado = clientes.findAll()
                .stream()
                .anyMatch(cliente ->
                        correo.equalsIgnoreCase(
                                cliente.getEmail()
                        )
                );

        if (usado) {

            throw error(
                    HttpStatus.CONFLICT,
                    "El correo pertenece a un cliente"
            );
        }
    }

    // =========================================
    // VALIDAR DATOS DEL ESTILISTA
    // =========================================

    private void validarEmpleado(
            EmpleadoGuardarRequest request,
            boolean nuevo
    ) {

        if (
                request == null
                        || request.nombre() == null
                        || request.nombre().isBlank()
                        || request.nombre().trim().length() > 150
                        || request.correo() == null
                        || !request.correo().trim().matches(
                        "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"
                )
                        || request.telefono() == null
                        || !request.telefono().trim().matches("[0-9]{9}")
        ) {

            throw error(
                    HttpStatus.BAD_REQUEST,
                    "Verifica nombre, correo y teléfono"
            );
        }

        boolean necesitaPassword =
                nuevo
                        || (
                        request.password() != null
                                && !request.password().isBlank()
                );

        if (
                necesitaPassword
                        && (
                        request.password() == null
                                || request.password().length() < 6
                                || request.password().length() > 100
                )
        ) {

            throw error(
                    HttpStatus.BAD_REQUEST,
                    "La contraseña debe tener entre 6 y 100 caracteres"
            );
        }
    }

    // =========================================
    // DASHBOARD ADMINISTRADOR
    // =========================================

    @Transactional(readOnly = true)
    public ResumenAdmin resumenAdmin() {

        List<Cita> lista = citas.findAll();

        LocalDate fechaHoy = hoy();

        // Cuenta todas las citas del día,
        // incluyendo las completadas.
        long citasHoy = lista.stream()
                .filter(c ->
                        fechaHoy.equals(c.getFecha())
                )
                .count();

        // Cuenta solamente citas próximas o
        // confirmadas de hoy y fechas futuras.
        // Las citas antiguas no se incluyen.
        long pendientes = lista.stream()
                .filter(c ->
                        !c.getFecha().isBefore(fechaHoy)
                )
                .filter(c ->
                        c.getEstado() == EstadoCita.PROXIMA
                                || c.getEstado() == EstadoCita.CONFIRMADA
                )
                .count();

        // Citas actualmente en atención.
        long enAtencion = lista.stream()
                .filter(c ->
                        c.getEstado() == EstadoCita.EN_ATENCION
                )
                .count();

        // Total histórico de citas completadas.
        long completadas = lista.stream()
                .filter(c ->
                        c.getEstado() == EstadoCita.COMPLETADA
                )
                .count();

        return new ResumenAdmin(
                citasHoy,
                pendientes,
                enAtencion,
                completadas
        );
    }

    // =========================================
    // DASHBOARD ESTILISTA
    // =========================================

    @Transactional(readOnly = true)
    public ResumenEstilista resumenEstilista(
            Integer idEmpleado
    ) {

        LocalDate fechaHoy = hoy();

        // Obtener las citas asignadas
        // únicamente a este estilista.
        List<Cita> lista = citas.findAll()
                .stream()
                .filter(c ->
                        idEmpleado.equals(
                                c.getIdEmpleado()
                        )
                )
                .toList();

        // Contar las citas de hoy
        // que todavía necesitan atención.
        // COMPLETADA y CANCELADA no cuentan.
        long citasAsignadasHoy = lista.stream()
                .filter(c ->
                        fechaHoy.equals(c.getFecha())
                )
                .filter(c ->
                        c.getEstado() == EstadoCita.PROXIMA
                                || c.getEstado() == EstadoCita.CONFIRMADA
                                || c.getEstado() == EstadoCita.EN_ATENCION
                )
                .count();

        // Contar todas las atenciones
        // completadas por el estilista.
        long atencionesFinalizadas = lista.stream()
                .filter(c ->
                        c.getEstado() == EstadoCita.COMPLETADA
                )
                .count();

        return new ResumenEstilista(
                citasAsignadasHoy,
                atencionesFinalizadas
        );
    }

    // =========================================
    // LISTAR TODAS LAS CITAS
    // =========================================

    @Transactional(readOnly = true)
    public List<CitaPanel> listarTodasCitas() {

        return ordenarCitas(
                citas.findAll()
        )
                .stream()
                .map(this::vistaCita)
                .toList();
    }

    // =========================================
    // LISTAR CITAS DEL ESTILISTA
    // =========================================

    @Transactional(readOnly = true)
    public List<CitaPanel> citasEstilista(
            Integer idEmpleado,
            String modo
    ) {

        String filtro = modo == null
                ? "TODAS"
                : modo.toUpperCase(Locale.ROOT);

        List<Cita> lista = citas.findAll()
                .stream()
                .filter(c ->
                        idEmpleado.equals(
                                c.getIdEmpleado()
                        )
                )
                .filter(c -> switch (filtro) {

                    case "HOY" ->
                            hoy().equals(c.getFecha());

                    case "HISTORIAL" ->
                            c.getEstado() == EstadoCita.COMPLETADA;

                    case "TODAS" ->
                            true;

                    default ->
                            throw error(
                                    HttpStatus.BAD_REQUEST,
                                    "Filtro de citas no reconocido"
                            );
                })
                .toList();

        return ordenarCitas(lista)
                .stream()
                .map(this::vistaCita)
                .toList();
    }

    // =========================================
    // ORDENAR CITAS POR FECHA Y HORA
    // =========================================

    private List<Cita> ordenarCitas(
            List<Cita> lista
    ) {

        return lista.stream()
                .sorted(
                        Comparator.comparing(
                                Cita::getFecha
                        ).thenComparing(
                                Cita::getHora
                        )
                )
                .toList();
    }

    // =========================================
    // BUSCAR CITA
    // =========================================

    private Cita buscarCita(
            Integer id
    ) {

        return citas.findById(id)
                .orElseThrow(() ->
                        error(
                                HttpStatus.NOT_FOUND,
                                "Cita no encontrada"
                        )
                );
    }

    // =========================================
    // CONVERTIR CITA PARA ANDROID
    // =========================================

    private CitaPanel vistaCita(
            Cita cita
    ) {

        Mascota mascota = mascotas
                .findById(
                        cita.getIdMascota()
                )
                .orElse(null);

        Servicio servicio = servicios
                .findById(
                        cita.getIdServicioPrincipal()
                )
                .orElse(null);

        Cliente cliente = mascota == null
                ? null
                : clientes.findById(
                mascota.getIdCliente()
        ).orElse(null);

        Empleado empleado =
                cita.getIdEmpleado() == null
                        ? null
                        : empleados.findById(
                        cita.getIdEmpleado()
                ).orElse(null);

        String infoMascota = mascota == null
                ? "Mascota no disponible"
                : mascota.getNombre()
                + " (" + mascota.getRaza() + ")"
                + (
                mascota.getNotas() == null
                        || mascota.getNotas().isBlank()
                        ? ""
                        : " · Notas: " + mascota.getNotas()
        );

        return new CitaPanel(
                cita.getIdCita(),
                cita.getFecha().toString(),
                cita.getHora().toString(),
                cita.getEstado().name(),
                infoMascota,
                cliente == null
                        ? "Cliente no disponible"
                        : cliente.getNombreCompleto(),
                servicio == null
                        ? "Servicio no disponible"
                        : servicio.getNombre(),
                cita.getIdEmpleado(),
                empleado == null
                        ? "Sin asignar"
                        : empleado.getNombreCompleto(),
                cita.getObservaciones(),
                cita.getPrecioTotal(),
                cita.getDuracionTotalMinutos()
        );
    }

    // =========================================
    // ASIGNAR CITA A ESTILISTA
    // =========================================

    @Transactional
    public CitaPanel asignarCita(
            Integer idCita,
            AsignarCitaRequest request
    ) {

        if (
                request == null
                        || request.idEstilista() == null
        ) {

            throw error(
                    HttpStatus.BAD_REQUEST,
                    "Selecciona un estilista"
            );
        }

        Cita cita = buscarCita(idCita);

        Empleado estilista = buscarEstilista(
                request.idEstilista()
        );

        if (
                !Boolean.TRUE.equals(
                        estilista.getActivo()
                )
        ) {

            throw error(
                    HttpStatus.CONFLICT,
                    "El estilista está desactivado"
            );
        }

        if (
                cita.getEstado() != EstadoCita.PROXIMA
                        && cita.getEstado() != EstadoCita.CONFIRMADA
        ) {

            throw error(
                    HttpStatus.CONFLICT,
                    "Solo puedes asignar citas próximas o confirmadas"
            );
        }

        LocalDateTime comienzo = LocalDateTime.of(
                cita.getFecha(),
                cita.getHora()
        );

        LocalDateTime fin = comienzo.plusMinutes(
                cita.getDuracionTotalMinutos()
        );

        // Evitar dos citas que se crucen
        // en el horario de un mismo estilista.
        for (Cita otra : citas.findAll()) {

            if (
                    otra.getIdCita().equals(idCita)
                            || !request.idEstilista().equals(
                            otra.getIdEmpleado()
                    )
                            || (
                            otra.getEstado() != EstadoCita.PROXIMA
                                    && otra.getEstado() != EstadoCita.CONFIRMADA
                                    && otra.getEstado() != EstadoCita.EN_ATENCION
                    )
            ) {
                continue;
            }

            LocalDateTime inicioOtra = LocalDateTime.of(
                    otra.getFecha(),
                    otra.getHora()
            );

            LocalDateTime finOtra =
                    inicioOtra.plusMinutes(
                            otra.getDuracionTotalMinutos()
                    );

            if (
                    comienzo.isBefore(finOtra)
                            && fin.isAfter(inicioOtra)
            ) {

                throw error(
                        HttpStatus.CONFLICT,
                        "El estilista ya tiene una cita en ese horario"
                );
            }
        }

        cita.setIdEmpleado(
                estilista.getIdEmpleado()
        );

        cita.setEstado(
                EstadoCita.CONFIRMADA
        );

        return vistaCita(
                citas.save(cita)
        );
    }

    // =========================================
    // CAMBIAR ESTADO DE CITA ADMINISTRADOR
    // =========================================

    @Transactional
    public CitaPanel cambiarEstadoAdmin(
            Integer idCita,
            CambiarEstadoRequest request
    ) {

        Cita cita = buscarCita(idCita);

        EstadoCita nuevo = estadoSolicitado(
                request
        );

        boolean permitido =
                (
                        cita.getEstado() == EstadoCita.PROXIMA
                                && (
                                nuevo == EstadoCita.CONFIRMADA
                                        || nuevo == EstadoCita.CANCELADA
                        )
                )
                        || (
                        cita.getEstado() == EstadoCita.CONFIRMADA
                                && nuevo == EstadoCita.CANCELADA
                );

        if (!permitido) {

            throw error(
                    HttpStatus.CONFLICT,
                    "Cambio de estado no permitido"
            );
        }

        cita.setEstado(nuevo);

        return vistaCita(
                citas.save(cita)
        );
    }

    // =========================================
    // CAMBIAR ESTADO DE CITA ESTILISTA
    // =========================================

    @Transactional
    public CitaPanel cambiarEstadoEstilista(
            Integer idEmpleado,
            Integer idCita,
            CambiarEstadoRequest request
    ) {

        Cita cita = buscarCita(idCita);

        if (
                !idEmpleado.equals(
                        cita.getIdEmpleado()
                )
        ) {

            throw error(
                    HttpStatus.FORBIDDEN,
                    "La cita no está asignada a este estilista"
            );
        }

        EstadoCita nuevo = estadoSolicitado(
                request
        );

        boolean permitido =
                (
                        (
                                cita.getEstado() == EstadoCita.CONFIRMADA
                                        || cita.getEstado() == EstadoCita.PROXIMA
                        )
                                && nuevo == EstadoCita.EN_ATENCION
                )
                        || (
                        cita.getEstado() == EstadoCita.EN_ATENCION
                                && nuevo == EstadoCita.COMPLETADA
                );

        if (!permitido) {

            throw error(
                    HttpStatus.CONFLICT,
                    "No se puede aplicar ese cambio de estado"
            );
        }

        cita.setEstado(nuevo);

        return vistaCita(
                citas.save(cita)
        );
    }

    // =========================================
    // VALIDAR NUEVO ESTADO DE CITA
    // =========================================

    private EstadoCita estadoSolicitado(
            CambiarEstadoRequest request
    ) {

        if (
                request == null
                        || request.estado() == null
        ) {

            throw error(
                    HttpStatus.BAD_REQUEST,
                    "Indica el estado de la cita"
            );
        }

        try {

            return EstadoCita.valueOf(
                    request.estado()
                            .trim()
                            .toUpperCase(Locale.ROOT)
            );

        } catch (IllegalArgumentException ex) {

            throw error(
                    HttpStatus.BAD_REQUEST,
                    "Estado de cita desconocido"
            );
        }
    }

    // =========================================
    // LISTAR SERVICIOS
    // =========================================

    @Transactional(readOnly = true)
    public List<Servicio> listarServicios() {

        return servicios.findAll()
                .stream()
                .sorted(
                        Comparator.comparing(
                                Servicio::getIdServicio
                        )
                )
                .toList();
    }

    // =========================================
    // REGISTRAR O EDITAR SERVICIO
    // =========================================

    @Transactional
    public Servicio guardarServicio(
            Integer id,
            ServicioGuardarRequest request
    ) {

        if (
                request == null
                        || request.nombre() == null
                        || request.nombre().isBlank()
                        || request.nombre().trim().length() > 150
                        || request.descripcion() == null
                        || request.descripcion().isBlank()
                        || request.precio() == null
                        || request.precio().signum() < 0
                        || request.duracionMinutos() == null
                        || request.duracionMinutos() <= 0
                        || request.tipo() == null
        ) {

            throw error(
                    HttpStatus.BAD_REQUEST,
                    "Revisa los datos del servicio"
            );
        }

        TipoServicio tipo;

        try {

            tipo = TipoServicio.valueOf(
                    request.tipo()
                            .trim()
                            .toUpperCase(Locale.ROOT)
            );

        } catch (IllegalArgumentException ex) {

            throw error(
                    HttpStatus.BAD_REQUEST,
                    "El tipo debe ser BANO o ADICIONAL"
            );
        }

        boolean duplicado = servicios.findAll()
                .stream()
                .anyMatch(s ->
                        s.getNombre().equalsIgnoreCase(
                                request.nombre().trim()
                        )
                                && (
                                id == null
                                        || !id.equals(
                                        s.getIdServicio()
                                )
                        )
                );

        if (duplicado) {

            throw error(
                    HttpStatus.CONFLICT,
                    "Ya existe ese nombre de servicio"
            );
        }

        Servicio servicio = id == null
                ? new Servicio()
                : servicios.findById(id)
                .orElseThrow(() ->
                        error(
                                HttpStatus.NOT_FOUND,
                                "Servicio no encontrado"
                        )
                );

        servicio.setNombre(
                request.nombre().trim()
        );

        servicio.setDescripcion(
                request.descripcion().trim()
        );

        servicio.setPrecio(
                request.precio()
        );

        servicio.setDuracionMinutos(
                request.duracionMinutos()
        );

        servicio.setTipo(tipo);

        if (id == null) {
            servicio.setActivo(true);
        }

        return servicios.save(servicio);
    }

    // =========================================
    // ACTIVAR O DESACTIVAR SERVICIO
    // =========================================

    @Transactional
    public Servicio cambiarActivoServicio(
            Integer id,
            ActivoRequest request
    ) {

        if (
                request == null
                        || request.activo() == null
        ) {

            throw error(
                    HttpStatus.BAD_REQUEST,
                    "Indica si el servicio estará activo"
            );
        }

        Servicio servicio = servicios.findById(id)
                .orElseThrow(() ->
                        error(
                                HttpStatus.NOT_FOUND,
                                "Servicio no encontrado"
                        )
                );

        servicio.setActivo(
                request.activo()
        );

        return servicios.save(servicio);
    }

    // =========================================
    // CONSULTAR CLIENTES Y MASCOTAS
    // =========================================

    @Transactional(readOnly = true)
    public List<ClientePanel> listarClientes() {

        List<Mascota> listaMascotas =
                mascotas.findAll();

        return clientes.findAll()
                .stream()
                .sorted(
                        Comparator.comparing(
                                Cliente::getIdCliente
                        )
                )
                .map(cliente ->
                        new ClientePanel(
                                cliente.getIdCliente(),
                                cliente.getNombreCompleto(),
                                cliente.getEmail(),
                                cliente.getTelefono(),
                                cliente.getActivo(),
                                listaMascotas.stream()
                                        .filter(m ->
                                                cliente.getIdCliente()
                                                        .equals(
                                                                m.getIdCliente()
                                                        )
                                        )
                                        .map(m ->
                                                new MascotaPanel(
                                                        m.getIdMascota(),
                                                        m.getNombre(),
                                                        m.getEspecie().name(),
                                                        m.getRaza(),
                                                        m.getEdadAnios(),
                                                        m.getNotas(),
                                                        m.getActivo()
                                                )
                                        )
                                        .toList()
                        )
                )
                .toList();
    }

    // =========================================
    // REPORTES DE ATENCIONES
    // =========================================

    @Transactional(readOnly = true)
    public ReporteAtenciones reporte(
            LocalDate desde,
            LocalDate hasta
    ) {

        if (
                desde == null
                        || hasta == null
                        || hasta.isBefore(desde)
                        || hasta.isAfter(
                        desde.plusYears(2)
                )
        ) {

            throw error(
                    HttpStatus.BAD_REQUEST,
                    "Rango de fechas no válido"
            );
        }

        List<Cita> lista = citas.findAll()
                .stream()
                .filter(c ->
                        !c.getFecha().isBefore(desde)
                                && !c.getFecha().isAfter(hasta)
                )
                .toList();

        BigDecimal ingresos = lista.stream()
                .filter(c ->
                        c.getEstado() == EstadoCita.COMPLETADA
                )
                .map(Cita::getPrecioTotal)
                .filter(p ->
                        p != null
                )
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );

        return new ReporteAtenciones(
                desde.toString(),
                hasta.toString(),
                lista.size(),
                contar(
                        lista,
                        EstadoCita.PROXIMA
                ),
                contar(
                        lista,
                        EstadoCita.CONFIRMADA
                ),
                contar(
                        lista,
                        EstadoCita.EN_ATENCION
                ),
                contar(
                        lista,
                        EstadoCita.COMPLETADA
                ),
                contar(
                        lista,
                        EstadoCita.CANCELADA
                ),
                ingresos
        );
    }
}
