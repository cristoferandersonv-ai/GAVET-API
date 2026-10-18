package com.gavet.api.service;

import java.util.Locale;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gavet.api.dto.MensajeResponse;
import com.gavet.api.dto.RecuperarPasswordRequest;
import com.gavet.api.dto.VerificarCorreoRequest;
import com.gavet.api.entity.Cliente;
import com.gavet.api.repository.ClienteRepository;

@Service
public class RecuperacionPasswordService {

    private final ClienteRepository clienteRepository;

    public RecuperacionPasswordService(
            ClienteRepository clienteRepository
    ) {
        this.clienteRepository =
                clienteRepository;
    }

    /*
     * Verifica que el correo pertenezca
     * a un cliente activo.
     */
    @Transactional(readOnly = true)
    public MensajeResponse verificarCorreo(
            VerificarCorreoRequest request
    ) {

        if (
            request == null ||
            request.getCorreo() == null
        ) {

            return new MensajeResponse(
                false,
                "Ingrese su correo electrónico"
            );
        }

        String correo =
                normalizarCorreo(
                    request.getCorreo()
                );

        if (!esCorreoValido(correo)) {

            return new MensajeResponse(
                false,
                "Ingrese un correo electrónico válido"
            );
        }

        boolean existe =
                clienteRepository
                    .findByEmailIgnoreCaseAndActivoTrue(
                        correo
                    )
                    .isPresent();

        if (!existe) {

            return new MensajeResponse(
                false,
                "No existe una cuenta activa con este correo"
            );
        }

        return new MensajeResponse(
            true,
            "Correo verificado correctamente"
        );
    }

    /*
     * Establece una nueva contraseña en MySQL.
     */
    @Transactional
    public MensajeResponse recuperarPassword(
            RecuperarPasswordRequest request
    ) {

        if (request == null) {

            return new MensajeResponse(
                false,
                "Los datos de recuperación son obligatorios"
            );
        }

        String correo =
                normalizarCorreo(
                    request.getCorreo()
                );

        String passwordNueva =
                request.getPasswordNueva() == null
                    ? ""
                    : request.getPasswordNueva();

        if (!esCorreoValido(correo)) {

            return new MensajeResponse(
                false,
                "El correo electrónico no es válido"
            );
        }

        if (passwordNueva.length() < 6) {

            return new MensajeResponse(
                false,
                "La contraseña debe tener mínimo 6 caracteres"
            );
        }

        Optional<Cliente> clienteEncontrado =
                clienteRepository
                    .findByEmailIgnoreCaseAndActivoTrue(
                        correo
                    );

        if (clienteEncontrado.isEmpty()) {

            return new MensajeResponse(
                false,
                "No existe una cuenta activa con este correo"
            );
        }

        Cliente cliente =
                clienteEncontrado.get();

        if (
            passwordNueva.equals(
                cliente.getPassword()
            )
        ) {

            return new MensajeResponse(
                false,
                "La nueva contraseña debe ser diferente a la anterior"
            );
        }

        /*
         * Para esta práctica académica se guarda
         * directamente, según la estructura actual
         * del proyecto.
         */
        cliente.setPassword(
            passwordNueva
        );

        clienteRepository.save(
            cliente
        );

        return new MensajeResponse(
            true,
            "Contraseña recuperada correctamente"
        );
    }

    private String normalizarCorreo(
            String correo
    ) {

        if (correo == null) {
            return "";
        }

        return correo
            .trim()
            .toLowerCase(
                Locale.ROOT
            );
    }

    private boolean esCorreoValido(
            String correo
    ) {

        return !correo.isBlank() &&
            correo.contains("@") &&
            correo.contains(".") &&
            !correo.contains(" ");
    }
}