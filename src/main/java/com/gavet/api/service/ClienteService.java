package com.gavet.api.service;

import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.gavet.api.dto.ActualizarClienteRequest;
import com.gavet.api.dto.CambiarPasswordRequest;
import com.gavet.api.dto.ClienteResponse;
import com.gavet.api.dto.MensajeResponse;
import com.gavet.api.entity.Cliente;
import com.gavet.api.repository.ClienteRepository;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(
            ClienteRepository clienteRepository
    ) {

        this.clienteRepository =
                clienteRepository;
    }

    /*
     * Obtiene los datos actuales del cliente.
     */
    @Transactional(readOnly = true)
    public ClienteResponse obtenerCliente(
            Integer idCliente
    ) {

        Cliente cliente =
                buscarClienteActivo(
                    idCliente
                );

        return convertirRespuesta(
            cliente
        );
    }

    /*
     * Actualiza nombre, correo y teléfono.
     */
    @Transactional
    public ClienteResponse actualizarCliente(
            Integer idCliente,
            ActualizarClienteRequest request
    ) {

        if (request == null) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Los datos del cliente son obligatorios"
            );
        }

        Cliente cliente =
                buscarClienteActivo(
                    idCliente
                );

        String nombre =
                limpiar(
                    request.getNombreCompleto()
                );

        String correo =
                limpiar(
                    request.getCorreo()
                ).toLowerCase(
                    Locale.ROOT
                );

        String telefono =
                limpiar(
                    request.getTelefono()
                );

        validarNombre(
            nombre
        );

        validarCorreo(
            correo
        );

        validarTelefono(
            telefono
        );

        boolean correoOcupado =
                clienteRepository
                    .existsByEmailIgnoreCaseAndIdClienteNot(
                        correo,
                        idCliente
                    );

        if (correoOcupado) {

            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "El correo pertenece a otro cliente"
            );
        }

        boolean telefonoOcupado =
                clienteRepository
                    .existsByTelefonoAndIdClienteNot(
                        telefono,
                        idCliente
                    );

        if (telefonoOcupado) {

            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "El teléfono pertenece a otro cliente"
            );
        }

        cliente.setNombreCompleto(
            nombre
        );

        cliente.setEmail(
            correo
        );

        cliente.setTelefono(
            telefono
        );

        Cliente clienteActualizado =
                clienteRepository.save(
                    cliente
                );

        return convertirRespuesta(
            clienteActualizado
        );
    }

    /*
     * Cambia la contraseña guardada en MySQL.
     */
    @Transactional
    public MensajeResponse cambiarPassword(
            Integer idCliente,
            CambiarPasswordRequest request
    ) {

        if (request == null) {

            return new MensajeResponse(
                false,
                "Los datos son obligatorios"
            );
        }

        Cliente cliente =
                buscarClienteActivo(
                    idCliente
                );

        String passwordActual =
                request.getPasswordActual() == null
                    ? ""
                    : request.getPasswordActual();

        String passwordNueva =
                request.getPasswordNueva() == null
                    ? ""
                    : request.getPasswordNueva();

        if (passwordActual.isEmpty()) {

            return new MensajeResponse(
                false,
                "Ingrese su contraseña actual"
            );
        }

        if (passwordNueva.length() < 6) {

            return new MensajeResponse(
                false,
                "La nueva contraseña debe tener mínimo 6 caracteres"
            );
        }

        if (
            !passwordActual.equals(
                cliente.getPassword()
            )
        ) {

            return new MensajeResponse(
                false,
                "La contraseña actual es incorrecta"
            );
        }

        if (
            passwordNueva.equals(
                passwordActual
            )
        ) {

            return new MensajeResponse(
                false,
                "La nueva contraseña debe ser diferente"
            );
        }

        cliente.setPassword(
            passwordNueva
        );

        clienteRepository.save(
            cliente
        );

        return new MensajeResponse(
            true,
            "Contraseña actualizada correctamente"
        );
    }

    /*
     * Elimina lógicamente la cuenta.
     *
     * No borra físicamente la fila para conservar
     * el historial de mascotas y citas.
     */
    @Transactional
    public MensajeResponse eliminarCuenta(
            Integer idCliente
    ) {

        Cliente cliente =
                buscarClienteActivo(
                    idCliente
                );

        cliente.setActivo(
            false
        );

        clienteRepository.save(
            cliente
        );

        return new MensajeResponse(
            true,
            "Tu cuenta fue eliminada correctamente"
        );
    }

    private Cliente buscarClienteActivo(
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

        Cliente cliente =
                clienteRepository
                    .findById(
                        idCliente
                    )
                    .orElseThrow(
                        () ->
                            new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Cliente no encontrado"
                            )
                    );

        if (
            !Boolean.TRUE.equals(
                cliente.getActivo()
            )
        ) {

            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "El cliente no se encuentra activo"
            );
        }

        return cliente;
    }

    private void validarNombre(
            String nombre
    ) {

        if (nombre.length() < 3) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Ingrese un nombre válido"
            );
        }

        if (
            !nombre.matches(
                "^[\\p{L}]+(?:[ '\\-][\\p{L}]+)*$"
            )
        ) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "El nombre solo debe contener letras"
            );
        }
    }

    private void validarCorreo(
            String correo
    ) {

        if (
            correo.isBlank() ||
            !correo.contains("@") ||
            !correo.contains(".")
        ) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Ingrese un correo válido"
            );
        }
    }

    private void validarTelefono(
            String telefono
    ) {

        if (
            !telefono.matches("\\d{9}") ||
            !telefono.startsWith("9")
        ) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Ingrese un teléfono válido de 9 dígitos"
            );
        }
    }

    private ClienteResponse convertirRespuesta(
            Cliente cliente
    ) {

        return new ClienteResponse(
            cliente.getIdCliente(),
            cliente.getNombreCompleto(),
            cliente.getEmail(),
            cliente.getTelefono()
        );
    }

    private String limpiar(
            String texto
    ) {

        return texto == null
            ? ""
            : texto.trim();
    }
}