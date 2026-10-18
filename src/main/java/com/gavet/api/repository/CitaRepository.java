package com.gavet.api.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gavet.api.entity.Cita;

public interface CitaRepository
        extends JpaRepository<Cita, Integer> {

    /*
     * Cuenta citas que se cruzan con el horario solicitado.
     */
    @Query(
        value =
            "SELECT COUNT(*) " +
            "FROM citas c " +
            "WHERE c.estado IN (" +
            "'PROXIMA', " +
            "'CONFIRMADA', " +
            "'EN_ATENCION'" +
            ") " +
            "AND TIMESTAMP(c.fecha, c.hora) < " +
            "TIMESTAMPADD(" +
            "MINUTE, " +
            ":duracionMinutos, " +
            "TIMESTAMP(:fecha, :hora)" +
            ") " +
            "AND TIMESTAMPADD(" +
            "MINUTE, " +
            "c.duracion_total_minutos, " +
            "TIMESTAMP(c.fecha, c.hora)" +
            ") > TIMESTAMP(:fecha, :hora)",
        nativeQuery = true
    )
    long contarConflictos(

        @Param("fecha")
        LocalDate fecha,

        @Param("hora")
        LocalTime hora,

        @Param("duracionMinutos")
        Integer duracionMinutos
    );

    /*
     * Obtiene todas las citas de las mascotas
     * pertenecientes al cliente.
     */
    @Query(
        value =
            "SELECT c.* " +
            "FROM citas c " +
            "INNER JOIN mascotas m " +
            "ON m.id_mascota = c.id_mascota " +
            "WHERE m.id_cliente = :idCliente " +
            "ORDER BY c.fecha ASC, c.hora ASC",
        nativeQuery = true
    )
    List<Cita> listarPorCliente(

        @Param("idCliente")
        Integer idCliente
    );

    /*
     * Busca una cita y confirma que pertenezca
     * al cliente indicado.
     */
    @Query(
        value =
            "SELECT c.* " +
            "FROM citas c " +
            "INNER JOIN mascotas m " +
            "ON m.id_mascota = c.id_mascota " +
            "WHERE c.id_cita = :idCita " +
            "AND m.id_cliente = :idCliente",
        nativeQuery = true
    )
    Optional<Cita> buscarPorIdYCliente(

        @Param("idCita")
        Integer idCita,

        @Param("idCliente")
        Integer idCliente
    );

    /*
     * Verifica si una mascota tiene una cita activa.
     */
    @Query(
        value =
            "SELECT COUNT(*) " +
            "FROM citas c " +
            "WHERE c.id_mascota = :idMascota " +
            "AND c.estado IN (" +
            "'PROXIMA', " +
            "'CONFIRMADA', " +
            "'EN_ATENCION'" +
            ")",
        nativeQuery = true
    )
    long contarCitasActivasPorMascota(

        @Param("idMascota")
        Integer idMascota
    );
}