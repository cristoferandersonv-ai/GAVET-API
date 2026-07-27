package com.gavet.api.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.gavet.api.dto.MascotaRequest;
import com.gavet.api.dto.MascotaResponse;
import com.gavet.api.entity.Cliente;
import com.gavet.api.entity.Mascota;
import com.gavet.api.enums.EspecieMascota;
import com.gavet.api.enums.SexoMascota;
import com.gavet.api.repository.ClienteRepository;
import com.gavet.api.repository.MascotaRepository;

@Service
public class MascotaService {

    private final MascotaRepository mascotaRepository;
    private final ClienteRepository clienteRepository;

    public MascotaService(
            MascotaRepository mascotaRepository,
            ClienteRepository clienteRepository
    ) {
        this.mascotaRepository =
                mascotaRepository;

        this.clienteRepository =
                clienteRepository;
    }

    /*
     * Lista las mascotas activas del cliente.
     */
    public List<MascotaResponse> listarPorCliente(
            Integer idCliente
    ) {

        validarCliente(idCliente);

        return mascotaRepository
            .findByIdClienteAndActivoTrueOrderByIdMascotaAsc(
                idCliente
            )
            .stream()
            .map(this::convertirRespuesta)
            .toList();
    }

    /*
     * Obtiene una mascota específica.
     */
    public MascotaResponse obtenerPorId(
            Integer idCliente,
            Integer idMascota
    ) {

        validarCliente(idCliente);

        Mascota mascota =
                buscarMascota(
                    idCliente,
                    idMascota
                );

        return convertirRespuesta(
            mascota
        );
    }

    /*
     * Registra una mascota en MySQL.
     */
    @Transactional
    public MascotaResponse registrar(
            Integer idCliente,
            MascotaRequest request
    ) {

        validarCliente(idCliente);
        validarDatos(request);

        Mascota mascota =
                new Mascota();

        mascota.setIdCliente(
            idCliente
        );

        asignarDatos(
            mascota,
            request
        );

        mascota.setActivo(
            true
        );

        Mascota mascotaGuardada =
                mascotaRepository.save(
                    mascota
                );

        return convertirRespuesta(
            mascotaGuardada
        );
    }

    /*
     * Actualiza una mascota existente.
     */
    @Transactional
    public MascotaResponse actualizar(
            Integer idCliente,
            Integer idMascota,
            MascotaRequest request
    ) {

        validarCliente(idCliente);
        validarDatos(request);

        Mascota mascota =
                buscarMascota(
                    idCliente,
                    idMascota
                );

        asignarDatos(
            mascota,
            request
        );

        Mascota mascotaActualizada =
                mascotaRepository.save(
                    mascota
                );

        return convertirRespuesta(
            mascotaActualizada
        );
    }

    private void asignarDatos(
            Mascota mascota,
            MascotaRequest request
    ) {

        mascota.setNombre(
            request.getNombre().trim()
        );

        mascota.setEspecie(
            convertirEspecie(
                request.getEspecie()
            )
        );

        mascota.setRaza(
            request.getRaza().trim()
        );

        mascota.setEdadAnios(
            request.getEdad()
        );

        mascota.setSexo(
            convertirSexo(
                request.getSexo()
            )
        );

        mascota.setPeso(
            request.getPeso()
        );
    }

    private void validarDatos(
            MascotaRequest request
    ) {

        if (request == null) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Los datos de la mascota son obligatorios"
            );
        }

        String nombre =
                request.getNombre() == null
                    ? ""
                    : request.getNombre().trim();

        String raza =
                request.getRaza() == null
                    ? ""
                    : request.getRaza().trim();

        if (nombre.length() < 2) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Ingrese un nombre válido"
            );
        }

        if (request.getEspecie() == null) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Seleccione la especie"
            );
        }

        if (raza.isBlank()) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Ingrese la raza"
            );
        }

        if (
            request.getEdad() == null ||
            request.getEdad() < 0 ||
            request.getEdad() > 40
        ) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "La edad debe estar entre 0 y 40 años"
            );
        }

        if (request.getSexo() == null) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Seleccione el sexo"
            );
        }

        if (
            request.getPeso() == null ||
            request.getPeso()
                .compareTo(BigDecimal.ZERO) <= 0 ||
            request.getPeso()
                .compareTo(new BigDecimal("200")) > 0
        ) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "El peso debe ser mayor que 0 y menor o igual que 200"
            );
        }

        /*
         * Verifica también que los textos correspondan
         * a valores válidos.
         */
        convertirEspecie(
            request.getEspecie()
        );

        convertirSexo(
            request.getSexo()
        );
    }

    private Cliente validarCliente(
            Integer idCliente
    ) {

        if (idCliente == null || idCliente <= 0) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "El identificador del cliente no es válido"
            );
        }

        Cliente cliente =
                clienteRepository
                    .findById(idCliente)
                    .orElseThrow(
                        () ->
                            new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Cliente no encontrado"
                            )
                    );

        if (!Boolean.TRUE.equals(cliente.getActivo())) {

            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "El cliente no se encuentra activo"
            );
        }

        return cliente;
    }

    private Mascota buscarMascota(
            Integer idCliente,
            Integer idMascota
    ) {

        return mascotaRepository
            .findByIdMascotaAndIdClienteAndActivoTrue(
                idMascota,
                idCliente
            )
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Mascota no encontrada"
                    )
            );
    }

    private EspecieMascota convertirEspecie(
            String especie
    ) {

        try {

            return EspecieMascota.valueOf(
                especie
                    .trim()
                    .toUpperCase(Locale.ROOT)
            );

        } catch (Exception error) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "La especie debe ser PERRO, GATO u OTRO"
            );
        }
    }

    private SexoMascota convertirSexo(
            String sexo
    ) {

        try {

            return SexoMascota.valueOf(
                sexo
                    .trim()
                    .toUpperCase(Locale.ROOT)
            );

        } catch (Exception error) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "El sexo debe ser MACHO o HEMBRA"
            );
        }
    }

    private MascotaResponse convertirRespuesta(
            Mascota mascota
    ) {

        return new MascotaResponse(
            mascota.getIdMascota(),
            mascota.getNombre(),
            mascota.getEspecie().name(),
            mascota.getRaza(),
            mascota.getEdadAnios(),
            mascota.getSexo().name(),
            mascota.getPeso()
        );
    }
}