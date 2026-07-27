package com.gavet.api.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.gavet.api.dto.CitaDetalleResponse;
import com.gavet.api.dto.CitaRequest;
import com.gavet.api.dto.CitaResponse;
import com.gavet.api.dto.DisponibilidadResponse;
import com.gavet.api.entity.Cita;
import com.gavet.api.entity.CitaAdicional;
import com.gavet.api.entity.Mascota;
import com.gavet.api.entity.Servicio;
import com.gavet.api.enums.EstadoCita;
import com.gavet.api.enums.TipoServicio;
import com.gavet.api.repository.CitaAdicionalRepository;
import com.gavet.api.repository.CitaRepository;
import com.gavet.api.repository.MascotaRepository;
import com.gavet.api.repository.ServicioRepository;

@Service
public class CitaService {

    private final CitaRepository citaRepository;

    private final CitaAdicionalRepository
            citaAdicionalRepository;

    private final MascotaRepository mascotaRepository;

    private final ServicioRepository servicioRepository;

    public CitaService(
            CitaRepository citaRepository,
            CitaAdicionalRepository citaAdicionalRepository,
            MascotaRepository mascotaRepository,
            ServicioRepository servicioRepository
    ) {

        this.citaRepository =
                citaRepository;

        this.citaAdicionalRepository =
                citaAdicionalRepository;

        this.mascotaRepository =
                mascotaRepository;

        this.servicioRepository =
                servicioRepository;
    }

    /*
     * Verifica si un horario se encuentra disponible.
     */
    @Transactional(readOnly = true)
    public DisponibilidadResponse verificarDisponibilidad(
            LocalDate fecha,
            LocalTime hora,
            int duracionMinutos
    ) {

        if (fecha == null) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "La fecha es obligatoria"
            );
        }

        if (hora == null) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "La hora es obligatoria"
            );
        }

        if (duracionMinutos <= 0) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "La duración debe ser mayor que cero"
            );
        }

        long conflictos =
                citaRepository.contarConflictos(
                    fecha,
                    hora,
                    duracionMinutos
                );

        boolean disponible =
                conflictos == 0;

        String mensaje =
                disponible
                    ? "Horario disponible"
                    : "El horario seleccionado ya está ocupado";

        return new DisponibilidadResponse(
            disponible,
            mensaje,
            conflictos
        );
    }

    /*
     * Obtiene todas las citas que pertenecen
     * al cliente indicado.
     */
    @Transactional(readOnly = true)
    public List<CitaDetalleResponse> listarPorCliente(
            Integer idCliente
    ) {

        if (
            idCliente == null ||
            idCliente <= 0
        ) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "El identificador del cliente no es válido"
            );
        }

        List<Cita> citas =
                citaRepository.listarPorCliente(
                    idCliente
                );

        List<CitaDetalleResponse> respuestas =
                new ArrayList<>();

        for (Cita cita : citas) {

            respuestas.add(
                convertirDetalle(
                    cita
                )
            );
        }

        return respuestas;
    }

    /*
     * Permite que el cliente cancele únicamente
     * sus propias citas próximas o confirmadas.
     */
    @Transactional
    public CitaDetalleResponse cancelarCita(
            Integer idCliente,
            Integer idCita
    ) {

        if (
            idCliente == null ||
            idCliente <= 0
        ) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "El identificador del cliente no es válido"
            );
        }

        if (
            idCita == null ||
            idCita <= 0
        ) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "El identificador de la cita no es válido"
            );
        }

        /*
         * Esta consulta también comprueba que la cita
         * pertenezca al cliente que inició sesión.
         */
        Cita cita =
                citaRepository
                    .buscarPorIdYCliente(
                        idCita,
                        idCliente
                    )
                    .orElseThrow(
                        () ->
                            new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "La cita no existe o no pertenece al cliente"
                            )
                    );

        if (
            cita.getEstado() ==
                EstadoCita.CANCELADA
        ) {

            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "La cita ya se encuentra cancelada"
            );
        }

        if (
            cita.getEstado() ==
                EstadoCita.COMPLETADA
        ) {

            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "No se puede cancelar una cita completada"
            );
        }

        if (
            cita.getEstado() ==
                EstadoCita.EN_ATENCION
        ) {

            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "No se puede cancelar una cita que ya está en atención"
            );
        }

        if (
            cita.getEstado() !=
                EstadoCita.PROXIMA &&
            cita.getEstado() !=
                EstadoCita.CONFIRMADA
        ) {

            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "La cita no puede ser cancelada en su estado actual"
            );
        }

        cita.setEstado(
            EstadoCita.CANCELADA
        );

        Cita citaActualizada =
                citaRepository.save(
                    cita
                );

        return convertirDetalle(
            citaActualizada
        );
    }

    /*
     * Registra una cita nueva en MySQL.
     */
    @Transactional
    public CitaResponse registrarCita(
            CitaRequest request
    ) {

        validarRequest(
            request
        );

        Mascota mascota =
                buscarMascotaActiva(
                    request.getIdMascota()
                );

        Servicio servicioPrincipal =
                buscarServicioPrincipal(
                    request.getIdServicioPrincipal()
                );

        List<Servicio> adicionales =
                buscarAdicionales(
                    request.getServiciosAdicionales()
                );

        BigDecimal precioTotal =
                servicioPrincipal.getPrecio();

        int duracionTotal =
                servicioPrincipal
                    .getDuracionMinutos();

        for (Servicio adicional : adicionales) {

            precioTotal =
                    precioTotal.add(
                        adicional.getPrecio()
                    );

            duracionTotal +=
                    adicional.getDuracionMinutos();
        }

        /*
         * El servidor vuelve a verificar el horario
         * antes de guardar la cita.
         */
        long conflictos =
                citaRepository.contarConflictos(
                    request.getFecha(),
                    request.getHora(),
                    duracionTotal
                );

        if (conflictos > 0) {

            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "El horario seleccionado ya está ocupado"
            );
        }

        Cita cita =
                new Cita();

        cita.setIdMascota(
            mascota.getIdMascota()
        );

        cita.setIdServicioPrincipal(
            servicioPrincipal.getIdServicio()
        );

        cita.setIdEmpleado(
            null
        );

        cita.setFecha(
            request.getFecha()
        );

        cita.setHora(
            request.getHora()
        );

        cita.setEstado(
            EstadoCita.PROXIMA
        );

        cita.setObservaciones(
            limpiarObservaciones(
                request.getObservaciones()
            )
        );

        cita.setPrecioServicioPrincipal(
            servicioPrincipal.getPrecio()
        );

        cita.setPrecioTotal(
            precioTotal
        );

        cita.setDuracionTotalMinutos(
            duracionTotal
        );

        Cita citaGuardada =
                citaRepository.save(
                    cita
                );

        /*
         * Guarda los servicios adicionales
         * asociados con la cita.
         */
        for (Servicio adicional : adicionales) {

            CitaAdicional citaAdicional =
                    new CitaAdicional();

            citaAdicional.setIdCita(
                citaGuardada.getIdCita()
            );

            citaAdicional.setIdServicio(
                adicional.getIdServicio()
            );

            citaAdicional.setPrecioAplicado(
                adicional.getPrecio()
            );

            citaAdicional
                .setDuracionAplicadaMinutos(
                    adicional.getDuracionMinutos()
                );

            citaAdicionalRepository.save(
                citaAdicional
            );
        }

        return new CitaResponse(
            true,
            "Cita registrada correctamente",
            citaGuardada.getIdCita(),
            citaGuardada.getPrecioTotal(),
            citaGuardada.getDuracionTotalMinutos()
        );
    }

    /*
     * Convierte una entidad Cita en la respuesta
     * que necesita la aplicación Android.
     */
    private CitaDetalleResponse convertirDetalle(
            Cita cita
    ) {

        Mascota mascota =
                mascotaRepository
                    .findById(
                        cita.getIdMascota()
                    )
                    .orElseThrow(
                        () ->
                            new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Mascota no encontrada"
                            )
                    );

        Servicio servicioPrincipal =
                servicioRepository
                    .findById(
                        cita.getIdServicioPrincipal()
                    )
                    .orElseThrow(
                        () ->
                            new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Servicio principal no encontrado"
                            )
                    );

        boolean cortePelo =
                false;

        boolean corteUnas =
                false;

        boolean limpiezaOidos =
                false;

        boolean cepilladoEspecial =
                false;

        boolean corteHigienico =
                false;

        List<CitaAdicional> adicionales =
                citaAdicionalRepository
                    .findByIdCitaOrderByIdCitaAdicionalAsc(
                        cita.getIdCita()
                    );

        for (
            CitaAdicional citaAdicional :
                adicionales
        ) {

            Servicio adicional =
                    servicioRepository
                        .findById(
                            citaAdicional.getIdServicio()
                        )
                        .orElse(
                            null
                        );

            if (adicional == null) {
                continue;
            }

            String nombre =
                    adicional.getNombre()
                        .trim()
                        .toLowerCase(
                            Locale.ROOT
                        );

            if (
                nombre.equals(
                    "corte de pelo"
                )
            ) {

                cortePelo =
                        true;

            } else if (
                nombre.equals(
                    "corte de uñas"
                )
            ) {

                corteUnas =
                        true;

            } else if (
                nombre.equals(
                    "limpieza de oídos"
                )
            ) {

                limpiezaOidos =
                        true;

            } else if (
                nombre.equals(
                    "cepillado especial"
                )
            ) {

                cepilladoEspecial =
                        true;

            } else if (
                nombre.equals(
                    "corte higiénico"
                )
            ) {

                corteHigienico =
                        true;
            }
        }

        String observaciones =
                cita.getObservaciones() == null
                    ? ""
                    : cita.getObservaciones();

        return new CitaDetalleResponse(
            cita.getIdCita(),
            cita.getIdMascota(),
            mascota.getNombre(),
            servicioPrincipal.getNombre(),
            cortePelo,
            corteUnas,
            limpiezaOidos,
            cepilladoEspecial,
            corteHigienico,
            cita.getFecha().toString(),
            cita.getHora().toString(),
            observaciones,
            cita.getPrecioTotal(),
            convertirEstadoVisible(
                cita.getEstado()
            )
        );
    }

    /*
     * Convierte el enum de MySQL en un texto
     * visible para Android.
     */
    private String convertirEstadoVisible(
            EstadoCita estado
    ) {

        return switch (estado) {

            case PROXIMA ->
                "Próxima";

            case CONFIRMADA ->
                "Confirmada";

            case EN_ATENCION ->
                "En atención";

            case COMPLETADA ->
                "Completada";

            case CANCELADA ->
                "Cancelada";
        };
    }

    /*
     * Valida los datos recibidos al registrar
     * una cita.
     */
    private void validarRequest(
            CitaRequest request
    ) {

        if (request == null) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Los datos de la cita son obligatorios"
            );
        }

        if (
            request.getIdMascota() == null ||
            request.getIdMascota() <= 0
        ) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Seleccione una mascota válida"
            );
        }

        if (
            request.getIdServicioPrincipal() == null ||
            request.getIdServicioPrincipal() <= 0
        ) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Seleccione un servicio principal"
            );
        }

        if (request.getFecha() == null) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "La fecha es obligatoria"
            );
        }

        if (request.getHora() == null) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "La hora es obligatoria"
            );
        }
    }

    /*
     * Busca una mascota y comprueba que esté activa.
     */
    private Mascota buscarMascotaActiva(
            Integer idMascota
    ) {

        Mascota mascota =
                mascotaRepository
                    .findById(
                        idMascota
                    )
                    .orElseThrow(
                        () ->
                            new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Mascota no encontrada"
                            )
                    );

        if (
            !Boolean.TRUE.equals(
                mascota.getActivo()
            )
        ) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "La mascota no se encuentra activa"
            );
        }

        return mascota;
    }

    /*
     * Comprueba que el servicio principal sea
     * un servicio de tipo BANO.
     */
    private Servicio buscarServicioPrincipal(
            Integer idServicio
    ) {

        Servicio servicio =
                buscarServicioActivo(
                    idServicio
                );

        if (
            servicio.getTipo() !=
                TipoServicio.BANO
        ) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "El servicio principal debe ser un baño"
            );
        }

        return servicio;
    }

    /*
     * Obtiene y valida los servicios adicionales.
     */
    private List<Servicio> buscarAdicionales(
            List<Integer> idsRecibidos
    ) {

        if (
            idsRecibidos == null ||
            idsRecibidos.isEmpty()
        ) {

            return new ArrayList<>();
        }

        /*
         * Evita guardar el mismo servicio
         * adicional más de una vez.
         */
        Set<Integer> idsSinDuplicar =
                new LinkedHashSet<>(
                    idsRecibidos
                );

        List<Servicio> adicionales =
                new ArrayList<>();

        for (Integer idServicio : idsSinDuplicar) {

            if (
                idServicio == null ||
                idServicio <= 0
            ) {

                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Existe un servicio adicional inválido"
                );
            }

            Servicio servicio =
                    buscarServicioActivo(
                        idServicio
                    );

            if (
                servicio.getTipo() !=
                    TipoServicio.ADICIONAL
            ) {

                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El servicio seleccionado no es adicional"
                );
            }

            adicionales.add(
                servicio
            );
        }

        return adicionales;
    }

    /*
     * Busca un servicio y comprueba que se
     * encuentre activo.
     */
    private Servicio buscarServicioActivo(
            Integer idServicio
    ) {

        Servicio servicio =
                servicioRepository
                    .findById(
                        idServicio
                    )
                    .orElseThrow(
                        () ->
                            new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Servicio no encontrado"
                            )
                    );

        if (
            !Boolean.TRUE.equals(
                servicio.getActivo()
            )
        ) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "El servicio no está disponible"
            );
        }

        return servicio;
    }

    /*
     * Limpia y valida las observaciones.
     */
    private String limpiarObservaciones(
            String observaciones
    ) {

        if (observaciones == null) {
            return null;
        }

        String texto =
                observaciones.trim();

        if (texto.isEmpty()) {
            return null;
        }

        if (texto.length() > 300) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Las observaciones no deben superar 300 caracteres"
            );
        }

        return texto;
    }
}